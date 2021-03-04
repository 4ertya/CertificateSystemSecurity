package com.epam.esm.exception.notfound;

public class EntityNotFoundException extends RuntimeException {
    private final String param;
    private final String errorCode;

    public EntityNotFoundException(String errorCode, String param) {
        this.param = param;
        this.errorCode = errorCode;
    }

    public String getParam() {
        return param;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
