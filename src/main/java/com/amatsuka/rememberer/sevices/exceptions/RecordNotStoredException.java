package com.amatsuka.rememberer.sevices.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RecordNotStoredException extends RuntimeException {
    public RecordNotStoredException(Throwable cause) {
        super(cause);
    }
}
