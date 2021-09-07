package com.example;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@Component
public class GreetingHandler {

    private final GreetingService greetingService;
    private final Validator greetingValidator = new GreetingValidator();
    Logger logger = LoggerFactory.getLogger(GreetingHandler.class);

    @Autowired
    public GreetingHandler(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public Mono<ServerResponse> hello(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(greetingService.getDefault()));
    }

    public Mono<ServerResponse> helloWithContent(ServerRequest request) {
        var greeting = request.body(BodyExtractors.toMono(Greeting.class));
        return ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromProducer(greeting, Greeting.class));
    }

    public Mono<ServerResponse> helloWithContentReversed(ServerRequest request) {
        var greeting = request.body(BodyExtractors.toMono(Greeting.class)).doOnNext(this::validate);

        return ok().body(BodyInserters.fromProducer(greeting.doOnError(e -> logger.warn(e.getMessage(), e)).map(m -> greetingService.fromString(StringUtils.reverse(m.getMessage()))), Greeting.class));
/*
        return greeting.flatMap(m -> {
            if (m.getMessage() == null)
                return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(GreetingError.of(GreetingException.MESSAGE_MUST_NOT_BE_NULL_TO_BE_REVERSED));
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(greetingService.fromString(StringUtils.reverse(m.getMessage())));
        });
*/
/*
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromProducer(greeting.map(m -> {
                    if (m.getMessage() == null)
                        throw new GreetingException(GreetingException.MESSAGE_MUST_NOT_BE_NULL_TO_BE_REVERSED);
                    return new Greeting(StringUtils.reverse(m.getMessage()));
                }), Greeting.class));
*/

    }

    private void validate(Greeting greeting) {
        Errors errors = new BeanPropertyBindingResult(greeting, "greeting");
        greetingValidator.validate(greeting, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}
