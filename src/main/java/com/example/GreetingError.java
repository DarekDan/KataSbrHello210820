package com.example;

public class GreetingError {
    private String[] errors;
    private String message;

    private GreetingError() {
    }

    public static GreetingError from(GreetingException exception) {
        GreetingError ge = new GreetingError();
        ge.setErrors(exception.getErrors());
        ge.setMessage(exception.getLocalizedMessage());
        return ge;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getErrors() {
        return errors;
    }

    private void setErrors(String[] errors) {
        this.errors = errors;
    }
}
