package com.epam.esm.exception.alreadyexist;

public class UserAlreadyExistException extends RuntimeException {
    private final String email;

    public UserAlreadyExistException(String message, String email) {
        super(message);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
