package com.epam.esm.exception.notfound;

public class OrderNotFoundException extends EntityNotFoundException {
    public OrderNotFoundException(String errorCode, String id) {
        super(errorCode, id);
    }
}
