package com.amatsuka.rememberer.service.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotUpdatedException extends RuntimeException {
    public UserNotUpdatedException(Throwable cause) {
        super(cause);
    }
}
