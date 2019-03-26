package com.amatsuka.rememberer.services.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotDeletedException extends RuntimeException {
    public UserNotDeletedException(Throwable cause) {
        super(cause);
    }
}
