package com.example;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GreetingHandler {

    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(new Greeting("Hello, Spring!")));
    }

    public Mono<ServerResponse> helloWithContent(ServerRequest request) {
        var greeting = request.body(BodyExtractors.toMono(Greeting.class));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromProducer(greeting, Greeting.class));
    }

    public Mono<ServerResponse> helloWithContentReversed(ServerRequest request) {
        var greeting = request.body(BodyExtractors.toMono(Greeting.class));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromProducer(greeting.map(m -> new Greeting(StringUtils.reverse(m.getMessage()))), Greeting.class));
    }
}
