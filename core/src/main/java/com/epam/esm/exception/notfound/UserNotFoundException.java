package com.epam.esm.exception.notfound;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(String errorCode, String param) {
        super(errorCode, param);
    }
}
