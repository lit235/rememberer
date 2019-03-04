package com.amatsuka.rememberer.sevices.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotDeletedException extends RuntimeException {
    public UserNotDeletedException(Throwable cause) {
        super(cause);
    }
}
