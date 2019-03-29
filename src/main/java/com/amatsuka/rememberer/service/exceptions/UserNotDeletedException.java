package com.amatsuka.rememberer.service.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotDeletedException extends RuntimeException {
    public UserNotDeletedException(Throwable cause) {
        super(cause);
    }
}
