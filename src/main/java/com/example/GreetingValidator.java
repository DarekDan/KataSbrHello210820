package com.example;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class GreetingValidator implements Validator {
    public static final String MESSAGE_MUST_NOT_BE_NULL_TO_BE_REVERSED = "'message' must not be null to be reversed";

    @Override
    public boolean supports(Class<?> clazz) {
        return Greeting.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "message", MESSAGE_MUST_NOT_BE_NULL_TO_BE_REVERSED);
    }
}
