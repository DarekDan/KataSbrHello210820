package com.example;

public class GreetingException extends RuntimeException {
    public static final String MESSAGE_MUST_NOT_BE_NULL_TO_BE_REVERSED = "Message must not be null to be reversed";

    public GreetingException(String message) {
        super(message);
    }

    public static class GreetingError {
        private String error;

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
}
