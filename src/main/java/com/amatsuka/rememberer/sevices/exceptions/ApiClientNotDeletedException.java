package com.amatsuka.rememberer.sevices.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApiClientNotDeletedException extends RuntimeException {
    public ApiClientNotDeletedException(Throwable cause) {
        super(cause);
    }
}
