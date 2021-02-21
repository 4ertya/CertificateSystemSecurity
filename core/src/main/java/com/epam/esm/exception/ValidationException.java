package com.epam.esm.exception;

public class ValidationException extends RuntimeException {

    private String description;

    public ValidationException(String message, String description) {
        super(message);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
