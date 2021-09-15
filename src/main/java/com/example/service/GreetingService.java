package com.example.service;

import com.example.model.Greeting;

public interface GreetingService {
    Greeting getDefault();

    Greeting fromString(String message);

    Greeting process(String message) throws GreetingException;
}
