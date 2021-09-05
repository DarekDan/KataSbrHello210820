# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.4/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.4/maven-plugin/reference/html/#build-image)

Extended kata example
from [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/#scratch)

### Learned lessons

Spring Boot is quite opinionated how things ought to be done.

#### Always check the date of the article

This [article](https://spring.io/blog/2016/09/22/new-in-spring-5-functional-web-framework), although quite fun reading,
has invalid code samples.

#### Functional Endpoints

This [anchor](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-fn)
demonstrates how to create routing functions.

#### Error handling comes "free"

We don't have to capture and handle errors on the API. If you don't like the action, throw a RuntimeException and let
the system deal with the outcome.

#### Unit testing with `@SpringBootTest` is da bomb

Just look at it, three lines of code, including the `@Autowired` WebClient and off you go on a random port.

#### Body Inserters and Extractors are scary useful

Need to produce a `Mono<T>` from the received request? Or produce a body from a long-running independent thread? Life
could not be simpler.

#### You can hit the endpoint with extra properties

Hitting `/hello` with a JSON payload that includes extra properties will succeed, as demonstrated by
the `testHelloNamePlus` test.

#### PIOMIN does excellent work

Simple way to add request and response
logging [just add the reactive dependency](https://piotrminkowski.com/2019/10/15/reactive-logging-with-spring-webflux-and-logstash/)
```xml
        <dependency>
            <groupId>com.github.piomin</groupId>
            <artifactId>reactive-logstash-logging-spring-boot-starter</artifactId>
            <version>1.3.0.RELEASE</version>
        </dependency>
```

### Docker and K8s 

#### To build a Docker image

Create a `Docker` file with the proper packaging options (i.e. source image) then run:
```shell
docker build -t kata-sbr-hello:210820 .
```

The `-t` parameter allows for the image to be tagged, including a version spec after the `:`.

[How to build a SpringBoot Docker image](https://spring.io/guides/gs/spring-boot-docker/)

#### To deploy a Docker image into K8s cluster

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