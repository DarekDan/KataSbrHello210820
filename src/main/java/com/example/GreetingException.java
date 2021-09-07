package com.example;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;

public class GreetingException extends ResponseStatusException {
    private Errors errors = null;

    public GreetingException(HttpStatus status, String message, Errors errors) {
        super(status, message);
        this.errors = errors;
    }

    public GreetingException(String message, Errors errors) {
        this(HttpStatus.BAD_REQUEST, message, errors);
    }

    public String[] getErrors() {
        if (errors == null || !errors.hasErrors()) return new String[]{};
        return errors.getAllErrors().stream().map(m -> m.getCodes().length == 0 ? "Unknown" : m.getCodes()[m.getCodes().length - 1]).toArray(String[]::new);
    }
}
