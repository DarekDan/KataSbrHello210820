package com.example;

import org.springframework.stereotype.Service;

@Service
public class GreetingServiceImpl implements GreetingService {

    @Override
    public Greeting getDefault(){
        return new Greeting("Hello, Spring!");
    }

    @Override
    public Greeting fromString(String message){
        return new Greeting(message);
    }
}
