package com.epam.esm.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private final LocalDateTime localDateTime;
    private final String code;
    private final String error;
    private final String message;
    private String path;


    public ExceptionResponse(String code, String error, String message, String path) {
        this.code = code;
        this.error = error;
        this.message = message;
        this.path = path;
        localDateTime = LocalDateTime.now(ZoneOffset.UTC);
    }

    public ExceptionResponse(String code, String error, String message) {
        this.code = code;
        this.error = error;
        this.message = message;
        localDateTime = LocalDateTime.now(ZoneOffset.UTC);
    }
}
