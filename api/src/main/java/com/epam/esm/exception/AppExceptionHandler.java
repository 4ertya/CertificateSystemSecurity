package com.epam.esm.exception;

import com.epam.esm.exception.alreadyexist.UserAlreadyExistException;
import com.epam.esm.exception.notfound.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    private final static String BAD_CREDENTIALS = "BadCredentialsException";
    private final MessageSource messageSource;

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException e,
                                                                         HttpServletRequest request,
                                                                         Authentication authentication) {
        String errorCode = ExceptionCode.ACCESS_IS_DENIED.getErrorCode();
        if (authentication == null) {
            errorCode = ExceptionCode.UNAUTHORIZED.getErrorCode();
        }


        String localizedMessage = messageSource.getMessage(
                errorCode, new Object[]{}, request.getLocale());

        ExceptionResponse response = new ExceptionResponse(errorCode, e.getClass().getSimpleName(),
                localizedMessage, request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
    public ResponseEntity<ExceptionResponse> handleBadCredentialException(HttpServletRequest request) {
        String errorCode = ExceptionCode.INVALID_USERNAME_OR_PASSWORD.getErrorCode();
        String localizedMessage = messageSource.getMessage(errorCode, new Object[]{}, request.getLocale());

        ExceptionResponse response = new ExceptionResponse(errorCode, BAD_CREDENTIALS,
                localizedMessage, request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException e,
                                                                           HttpServletRequest request) {
        log.error(e.getMessage(), e);
        String localizedMessage = messageSource.getMessage(
                e.getMessage(), new Object[]{}, request.getLocale());

        ExceptionResponse response = new ExceptionResponse(e.getMessage(), e.getClass().getSimpleName(),
                localizedMessage, request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionResponse> handleValidatorException(ValidationException e,
                                                                      HttpServletRequest request) {
        String localizedMessage = messageSource.getMessage(
                e.getMessage(), new Object[]{}, request.getLocale());

        ExceptionResponse response = new ExceptionResponse(e.getMessage(), e.getClass().getSimpleName(),
                buildErrorMessage(localizedMessage, e.getDescription()), request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExistException(UserAlreadyExistException e,
                                                                             HttpServletRequest request) {
        String localizedMessage = messageSource.getMessage(
                e.getMessage(), new Object[]{e.getEmail()}, request.getLocale());
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), e.getClass().getSimpleName(),
                localizedMessage, request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e,
                                                                           HttpServletRequest request) {
        String localizedMessage = messageSource.getMessage(
                e.getErrorCode(), new Object[]{e.getParam()}, request.getLocale());
        ExceptionResponse response = new ExceptionResponse(e.getErrorCode(), e.getClass().getSimpleName(),
                localizedMessage, request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleMediaTypeException(HttpMediaTypeNotSupportedException e,
                                                                      HttpServletRequest request) {
        String errorCode = ExceptionCode.INVALID_MEDIA_TYPE.getErrorCode();
        String errorMessage = messageSource.getMessage(
                errorCode, new Object[]{}, request.getLocale());
        ExceptionResponse response = new ExceptionResponse(errorCode, e.getClass().getSimpleName(),
                errorMessage, request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e, WebRequest wr) {
        String errorCode = ExceptionCode.INVALID_URL_REQUEST.getErrorCode();
        String errorMessage = messageSource.getMessage(
                errorCode, new Object[]{}, wr.getLocale());
        ExceptionResponse response = new ExceptionResponse(errorCode, e.getClass().getSimpleName(), errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                                                   HttpServletRequest request) {
        String errorCode = ExceptionCode.INVALID_DATA_TYPES_PASSED.getErrorCode();
        String errorMessage = messageSource.getMessage(
                errorCode, new Object[]{}, request.getLocale());
        ExceptionResponse response = new ExceptionResponse(errorCode, e.getClass().getSimpleName(), errorMessage,
                request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String errorCode = ExceptionCode.METHOD_NOT_ALLOWED.getErrorCode();
        String errorMessage = messageSource.getMessage(
                errorCode, new Object[]{}, request.getLocale());
        ExceptionResponse response = new ExceptionResponse(errorCode, e.getClass().getSimpleName(), errorMessage,
                request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionResponse> handleThrowable(
            Throwable e, HttpServletRequest request) {
        String errorCode = ExceptionCode.THROWABLE.getErrorCode();
        log.error(e.getMessage(), e);
        String errorMessage = messageSource.getMessage(
                errorCode, new Object[]{}, request.getLocale());
        ExceptionResponse response = new ExceptionResponse(errorCode, e.getClass().getSimpleName(), errorMessage,
                request.getServletPath());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private String buildErrorMessage(String localizedMessage, String param) {
        return param == null ? localizedMessage : localizedMessage + " " + param;
    }
}
