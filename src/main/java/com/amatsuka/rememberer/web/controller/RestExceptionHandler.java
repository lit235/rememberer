package com.amatsuka.rememberer.web.controller;

import com.amatsuka.rememberer.service.exceptions.RecordNotStoredException;
import com.amatsuka.rememberer.service.exceptions.UserNotDeletedException;
import com.amatsuka.rememberer.service.exceptions.UserNotStoredException;
import com.amatsuka.rememberer.service.exceptions.UserNotUpdatedException;
import com.amatsuka.rememberer.web.exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            UserNotStoredException.class, UserNotUpdatedException.class, UserNotDeletedException.class,
            RecordNotStoredException.class,
    })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {
            ResourceNotFoundException.class
    })
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
