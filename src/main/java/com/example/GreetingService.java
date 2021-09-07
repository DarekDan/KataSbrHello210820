package com.example;

public interface GreetingService {
    Greeting getDefault();

    Greeting fromString(String message);
}
