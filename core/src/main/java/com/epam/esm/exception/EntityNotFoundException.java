package com.epam.esm.exception;

public class EntityNotFoundException extends RuntimeException {
    private final String id;

    public EntityNotFoundException(String errorCode, String id) {
        super(errorCode);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
