package com.epam.esm.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class AppExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionResponse> handleValidatorException(ValidationException e,
                                                                      HttpServletRequest request) {
        String localizedMessage = messageSource.getMessage(
                e.getMessage(), new Object[]{}, request.getLocale());

        ExceptionResponse response = new ExceptionResponse(e.getMessage(), e.getClass().getSimpleName(),
                buildErrorMessage(localizedMessage, e.getDescription()), request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e,
                                                                           HttpServletRequest request) {
        String localizedMessage = messageSource.getMessage(
                e.getMessage(), new Object[]{}, request.getLocale());
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), e.getClass().getSimpleName(),
                buildErrorMessage(localizedMessage, e.getId()), request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleMediaTypeException(HttpMediaTypeNotSupportedException e,
                                                                      HttpServletRequest request) {
        String errorMessage = messageSource.getMessage(
                "50101", new Object[]{}, request.getLocale());
        ExceptionResponse response = new ExceptionResponse("50101", e.getClass().getSimpleName(),
                errorMessage, request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e, WebRequest wr) {
        String errorMessage = messageSource.getMessage(
                "50103", new Object[]{}, wr.getLocale());
        ExceptionResponse response = new ExceptionResponse("50103", e.getClass().getSimpleName(), errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                                                   HttpServletRequest request) {
        String errorMessage = messageSource.getMessage(
                "50104", new Object[]{}, request.getLocale());
        ExceptionResponse response = new ExceptionResponse("50104", e.getClass().getSimpleName(), errorMessage,
                request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String errorMessage = messageSource.getMessage(
                "50105", new Object[]{}, request.getLocale());
        ExceptionResponse response = new ExceptionResponse("50105", e.getClass().getSimpleName(), errorMessage,
                request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionResponse> handleThrowable(
            Throwable e, HttpServletRequest request) {
        log.error(e.getMessage(),e);
        String errorMessage = messageSource.getMessage(
                "5000", new Object[]{}, request.getLocale());
        ExceptionResponse response = new ExceptionResponse("5000", e.getClass().getSimpleName(), errorMessage,
                request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private String buildErrorMessage(String localizedMessage, String param) {
        return param == null ? localizedMessage : localizedMessage + " " + param;
    }
}
