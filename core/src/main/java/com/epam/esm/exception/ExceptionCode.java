package com.epam.esm.exception;

public enum ExceptionCode {

    SHOULD_BE_POSITIVE("10101"),
    CANNOT_BE_NULL("10102"),
    CANNOT_BE_EMPTY("10103"),

    INVALID_ORDER_BY_VALUE("20201"),
    DATA_TYPE_DOES_NOT_MATCH_REQUIRED("20202"),
    PAGE_IS_GREATER_THAN_TOTAL_AMOUNT_OF_PAGES("20203"),
    PRICE_TOO_HIGH("20204"),
    DURATION_CANNOT_BE_MORE_THAN_YEAR("20205"),

    TAG_ALREADY_EXIST("30101"),

    NON_EXISTING_TAG("40101"),
    NON_EXISTING_CERTIFICATE("40102"),
    NON_EXISTING_USER("20103"),
    NON_EXISTING_ORDER("20104");


    private final String errorCode;

    ExceptionCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
