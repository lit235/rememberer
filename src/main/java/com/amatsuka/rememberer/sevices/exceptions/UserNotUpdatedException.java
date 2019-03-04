package com.amatsuka.rememberer.sevices.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotUpdatedException extends RuntimeException {
    public UserNotUpdatedException(Throwable cause) {
        super(cause);
    }
}
