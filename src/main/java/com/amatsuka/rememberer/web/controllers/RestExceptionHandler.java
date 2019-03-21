package com.amatsuka.rememberer.web.controllers;

import com.amatsuka.rememberer.sevices.exceptions.RecordNotStoredException;
import com.amatsuka.rememberer.sevices.exceptions.UserNotDeletedException;
import com.amatsuka.rememberer.sevices.exceptions.UserNotStoredException;
import com.amatsuka.rememberer.sevices.exceptions.UserNotUpdatedException;
import com.amatsuka.rememberer.web.exceptions.BadRequestException;
import com.amatsuka.rememberer.web.exceptions.ResourceNotFoundException;
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
            ResourceNotFoundException.class
    })
    protected void handleConflict(RuntimeException ex, WebRequest request) {
        throw new BadRequestException();
    }
}
