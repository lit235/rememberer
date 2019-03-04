package com.amatsuka.rememberer.sevices.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotStoredException extends RuntimeException {
    public UserNotStoredException(Throwable cause) {
        super(cause);
    }
}
