# Getting Started
This project is a programming kata with Spring Boot WebFlux. 

## Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.4/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.4/maven-plugin/reference/html/#build-image)

Extended kata example
from [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/#scratch)

## Learned lessons

Spring Boot is quite opinionated how things ought to be done.

### Always check the date of the article

This [article](https://spring.io/blog/2016/09/22/new-in-spring-5-functional-web-framework), although quite fun reading,
has invalid code samples.

### Functional Endpoints

This [anchor](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-fn)
demonstrates how to create routing functions.

### Error handling

#### Handling errors by throwing RunTime exception
We don't have to capture and handle errors on the API. If you don't like the action, throw a RuntimeException and let
the system deal with the outcome. While this method is convenient, we lose control over the HTTP status returned, 
and it will always be 500, which usually is not desirable.  

#### Handling errors by capturing any data quality or processing issues
While this method allows us to control HTTP Status and content returned, quite often it brings business rules into the
controller or request handler code. This results in violating separation of concerns and makes the code harder to migrate 
in the future.

#### Handling errors with Validators and Exception handlers
This method provides the most control over the results returned by the API and separates business logic from the controller
code, while observing the reactive programming style.
```java
public Mono<ServerResponse> helloWithContentReversed(ServerRequest request) {
    return request.body(BodyExtractors.toMono(Greeting.class))
        .doOnNext(this::validate)
        .flatMap(m -> ok().bodyValue(greetingService.fromString(StringUtils.reverse(m.getMessage()))))
        .onErrorResume(GreetingException.class, e -> badRequest().bodyValue(GreetingError.from(e)));
}

private void validate(Greeting greeting) {
    Errors errors = new BeanPropertyBindingResult(greeting, "greeting");
    greetingValidator.validate(greeting, errors);
    if (errors.hasErrors()) {
        throw new GreetingException("Greeting validation failed", errors);
    }
}
```
This approach allows us to invoke a business rules validation from a dedicated  business class and return API specific status 
code and customized response content. The API will return:
* HTTP 200 OK with result of processing supplied data
* HTTP 400 BAD REQUEST if the supplied data was invalid
* HTTP 500 in all other error scenarios

### Unit testing with `@SpringBootTest` is da bomb

Just look at it, three lines of code, including the `@Autowired` WebClient and off you go on a random port.

### Body Inserters and Extractors are scary useful

Need to produce a `Mono<T>` from the received request? Or produce a body from a long-running independent thread? Life
could not be simpler.

### You can hit the endpoint with extra properties

Hitting `/hello` with a JSON payload that includes extra properties will succeed, as demonstrated by
the `testHelloNamePlus` test.

### PIOMIN does excellent work

Simple way to add request and response
logging [just add the reactive dependency](https://piotrminkowski.com/2019/10/15/reactive-logging-with-spring-webflux-and-logstash/)
```xml
        <dependency>
            <groupId>com.github.piomin</groupId>
            <artifactId>reactive-logstash-logging-spring-boot-starter</artifactId>
            <version>1.3.0.RELEASE</version>
        </dependency>
```

## Docker and K8s 

### Building Docker image with Dockerfile

Create a `Docker` file with the proper packaging options (i.e. source image) then run:
```shell
docker build -t kata-sbr-hello:210820 .
```

The `-t` parameter allows for the image to be tagged, including a version spec after the `:`.

[How to build a SpringBoot Docker image](https://spring.io/guides/gs/spring-boot-docker/)

### Building Docker image with Fabric8io Maven plug-in
The Fabric8io plugin allows to build a docker image while leveraging unique Maven option, like artifact names and version
specified in the `pom.xml` file.

Sample plugin configuration
```xml
<plugin>
    <groupId>io.fabric8</groupId>
    <artifactId>docker-maven-plugin</artifactId>
    <version>0.37.0</version>
    <configuration>
        <images>
            <image>
                <name>darekdan/${project.artifactId}</name>
                <build>
                    <from>adoptopenjdk:11-jre-openj9</from>
                    <tags>
                        <tag>latest</tag>
                        <tag>${project.version}</tag>
                    </tags>
                    <ports>
                        <port>8080</port>
                    </ports>
                    <entryPoint>
                        <exec>
                            <arg>java</arg>
                            <arg>-jar</arg>
                            <arg>/app/${project.artifactId}-${project.version}.jar</arg>
                        </exec>
                    </entryPoint>
                    <assemblies>
                        <assembly>
                            <descriptorRef>artifact</descriptorRef>
                            <targetDir>/app</targetDir>
                        </assembly>
                    </assemblies>
                </build>
            </image>
        </images>
    </configuration>
</plugin>
```
This simplifies the Docker image creation as one has to invoke only `mvn docker:build` to get local catalog refreshed 
with the latest version of a successful build.  

[Fabric8io plug-in documentation](https://dmp.fabric8.io/)

### To deploy a Docker image into K8s cluster

Create a K8s deployment file specifying the Docker image to be used in the pod template 
definitions. Do not forget to create a load-balancing service, which will distribute the requests across
3 (or more) pods. Be mindful of the selectors to be able to match selectable objects.  
Deploy to K8s cluster using:
```shell
kubectl apply -f kata-sbr-hello.deployment.yaml
```

Note that ZSH has a decent extension for docker and K8s. the above command is aliased to:
```shell
kaf kata-sbr-hello.deployment.yaml
```

To get status, issue `kubectl get all` or `kga` (ZSH). To delete the deployment
```shell
kubectl delete -f kata-sbr-hello.deployment.yaml
```
or (ZSH)
```shell
kdelf kata-sbr-hello.deployment.yaml
```

After deployment, use `curl http://localhost:8088/hello --verbose --trace-time` 
to test the service (verbose and trace-time options are optional).  
To test a POST message with `curl` use:
```shell
curl http://localhost:8088/helloReversed --header "Content-Type: application/json" --request POST --data '{ "message": "Darek"}'
```

### Testing K8s deployment with Gatling
[Gatling](https://gatling.io/) is a load testing engine written in scala. To simplify, 
from developer's perspective, only one dependency and one plugin is [needed](https://gatling.io/docs/gatling/reference/current/extensions/maven_plugin/). 
The tests are written in [scala](https://www.scala-lang.org/), which runs on the JVM
(Java Virtual Machine). The tests are separated into scenarios and a sample one, to test
two APIs with random pause between them, have been created: `src/test/scala/GreetingSimulation.scala`. 

To run the test 
simply use:
```shell
mvn gatling:test
```

Please note that this is different from running unit tests using `@SpringBootTest` and might involve
URLs that are not defined, nor part of the application you are working on. In the provided
example we are testing the deployment created by the K8s deployment file. On a six-core i7
macOS laptop, increasing the number of pods above three, did not provide the expected boost
and resulted in large number of failures under load of 100,000 users ramped up over one minute.