package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

@Service
public class GreetingServiceImpl implements GreetingService {

    private final Validator greetingValidator = new GreetingValidator();

    @Override
    public Greeting getDefault() {
        return new Greeting("Hello, Spring!");
    }

    @Override
    public Greeting fromString(String message) {
        return new Greeting(message);
    }

    @Override
    public Greeting process(String message) throws GreetingException {

        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            Greeting greeting = objectMapper.readValue(message, Greeting.class);
            Errors errors = new BeanPropertyBindingResult(greeting, "greeting");
            greetingValidator.validate(greeting, errors);
            if (errors.hasErrors()) {
                throw new GreetingException("Greeting validation failed", errors);
            }
            return new Greeting(StringUtils.reverse(greeting.getMessage()));
        } catch (JsonProcessingException jme) {

            final MapBindingResult errors = new MapBindingResult(Map.of(), "JSON message");
            final ObjectError objectError = new ObjectError("body", new String[]{jme.getMessage(), jme.getLocalizedMessage()}, new Object[]{message}, jme.getOriginalMessage());
            errors.addError(objectError);
            throw new GreetingException("Greeting mapping failed", errors);
        }
    }


}
