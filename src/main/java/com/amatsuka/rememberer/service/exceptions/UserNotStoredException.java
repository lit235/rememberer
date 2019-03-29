package com.amatsuka.rememberer.service.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotStoredException extends RuntimeException {
    public UserNotStoredException(Throwable cause) {
        super(cause);
    }
}
