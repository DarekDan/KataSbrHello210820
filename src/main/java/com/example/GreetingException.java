package com.example;

public class GreetingException extends RuntimeException {
    public static final String MESSAGE_MUST_NOT_BE_NULL_TO_BE_REVERSED = "Message must not be null to be reversed";

    public GreetingException(String message) {
        super(message);
    }

}
