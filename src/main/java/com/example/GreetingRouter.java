package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class GreetingRouter {

    Logger logger = LoggerFactory.getLogger(GreetingRouter.class);

    @Bean
    public RouterFunction<ServerResponse> route(GreetingHandler greetingHandler) {
        /*
        RouterFunctions.route() starts a Builder, but route(RequestPredicate, RouterFunction) returns a RouterFunction.
        RouterFunction.and() would chain two different functions for one route.
        RouterFunction.andRoute() "adds" additional route if the prior is not matching for the specified predicate.
        RouterFunction.andOther() would execute secondary function if prior return no result.
        RouterFunction.andNest() returns composite routing function.
        */

        /*
        return RouterFunctions
                .route(GET("/hello").and(accept(MediaType.APPLICATION_JSON)), greetingHandler::hello)
                .andRoute(POST("/hello").and(accept(MediaType.APPLICATION_JSON)), greetingHandler::helloWithContent)
                .andRoute(POST("/helloReversed").and(accept(MediaType.APPLICATION_JSON)), greetingHandler::helloWithContentReversed);
        */

        return RouterFunctions.route()
                .GET("/hello", accept(MediaType.APPLICATION_JSON), greetingHandler::hello)
                .POST("/hello", accept(MediaType.APPLICATION_JSON), greetingHandler::helloWithContent)
                .POST("/helloReversed", accept(MediaType.APPLICATION_JSON), greetingHandler::helloWithContentReversed)
                .build();
    }

}
