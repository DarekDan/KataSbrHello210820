package com.example;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GreetingHandler {

    private GreetingService greetingService;

    @Autowired
    public GreetingHandler(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(greetingService.getDefault()));
    }

    public Mono<ServerResponse> helloWithContent(ServerRequest request) {
        var greeting = request.body(BodyExtractors.toMono(Greeting.class));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromProducer(greeting, Greeting.class));
    }

    public Mono<ServerResponse> helloWithContentReversed(ServerRequest request) {
        var greeting = request.body(BodyExtractors.toMono(Greeting.class));

        return greeting.flatMap(m -> {
            if (m.getMessage() == null)
                return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(GreetingError.of(GreetingException.MESSAGE_MUST_NOT_BE_NULL_TO_BE_REVERSED));
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(greetingService.fromString(StringUtils.reverse(m.getMessage())));
        });
/*
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromProducer(greeting.map(m -> {
                    if (m.getMessage() == null)
                        throw new GreetingException(GreetingException.MESSAGE_MUST_NOT_BE_NULL_TO_BE_REVERSED);
                    return new Greeting(StringUtils.reverse(m.getMessage()));
                }), Greeting.class));
*/

    }
}
