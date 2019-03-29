package com.amatsuka.rememberer.service.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RecordNotStoredException extends RuntimeException {
    public RecordNotStoredException(Throwable cause) {
        super(cause);
    }
}
