package com.example.startup;

import com.example.service.GreetingService;
import com.example.service.GreetingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KataSbrHelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(KataSbrHelloApplication.class, args);
    }

    @Bean
    public GreetingService greetingService(){
        return new GreetingServiceImpl();
    }
}
