package com.example;

public class GreetingError {
    private String error;

    private GreetingError(){}

    public static GreetingError of(String errorMessage) {
        GreetingError ge = new GreetingError();
        ge.setError(errorMessage);
        return ge;
    }

    public String getError() {
        return error;
    }

    private void setError(String error) {
        this.error = error;
    }
}
