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

### Check the date of the article

This [article](https://spring.io/blog/2016/09/22/new-in-spring-5-functional-web-framework), although quite fun reading,
has invalid code samples.

#### Error handling comes "free"

We don't have to capture and handle errors on the API. If you don't like the action, throw a RuntimeException and let
the system deal with the outcome.

#### Unit testing with `@SpringBootTest` is da bomb

Just look at it, three lines of code, including the `@Autowired` WebClient and off you go on a random port.

#### Body Inserters and Extractors are scary useful

Need to produce a `Mono<T>` from the received request? Or produce a body from a long-running independent thread? Life
could not be simpler. 
