package com.amatsuka.rememberer.service.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApiClientNotStoredException extends RuntimeException {
    public ApiClientNotStoredException(Throwable cause) {
        super(cause);
    }
}
