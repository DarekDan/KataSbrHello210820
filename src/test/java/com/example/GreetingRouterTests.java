package com.example;

import com.example.model.Greeting;
import com.example.model.GreetingPlus;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.main.web-application-type=reactive")
class GreetingRouterTests {

    Logger log = LoggerFactory.getLogger(GreetingRouterTests.class);

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testHello() {
        webTestClient.get().uri("/hello").accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk()
                .expectBody(Greeting.class).value(greeting -> assertThat(greeting.getMessage()).isEqualTo("Hello, Spring!"));
    }

    @Test
    void testHelloBadMedia() {
        webTestClient.get().uri("/hello").accept(MediaType.TEXT_PLAIN).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void testHelloName() {
        final String message = "Hello, Darek.";
        webTestClient.post().uri("/hello").body(BodyInserters.fromValue(new Greeting(message)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Greeting.class).value(greeting -> assertThat(greeting.getMessage()).isEqualTo(message));
    }

    @Test
    void testHelloReversedName() {
        final String message = "Hello, Darek.";
        webTestClient.post().uri("/helloReversed").body(BodyInserters.fromValue(new Greeting(message)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Greeting.class).value(greeting -> assertThat(greeting.getMessage()).isEqualTo(StringUtils.reverse(message)));
    }

    @Test
    void testHelloReversedNameWithNull() {
        webTestClient.post().uri("/helloReversed").body(BodyInserters.fromValue(new Greeting()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError().expectBody()
                .consumeWith(result -> log.info(new String(result.getResponseBody(), StandardCharsets.UTF_8)));

        // Server must not crash
        webTestClient.post().uri("/helloReversed").body(BodyInserters.fromValue(new Greeting("Anything")))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk().expectBody()
                .consumeWith(result -> log.info(new String(result.getResponseBody(), StandardCharsets.UTF_8)));

    }

    @Test
    void testHelloNamePlus() {
        final String message = "Hello, Darek+.";
        webTestClient.post().uri("/hello").body(BodyInserters.fromValue(new GreetingPlus(message)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Greeting.class).value(greeting -> assertThat(greeting.getMessage()).isEqualTo(message));
    }
}
