package com.epam.esm.exception.alreadyexist;

import lombok.Data;

@Data
public class UserAlreadyExistException extends RuntimeException {
    private String email;

    public UserAlreadyExistException(String message, String email) {
        super(message);
        this.email=email;
    }
}
