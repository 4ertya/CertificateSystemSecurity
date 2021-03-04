package com.epam.esm.exception.notfound;

public class TagNotFoundException extends EntityNotFoundException {
    public TagNotFoundException(String errorCode, String id) {
        super(errorCode, id);
    }
}
