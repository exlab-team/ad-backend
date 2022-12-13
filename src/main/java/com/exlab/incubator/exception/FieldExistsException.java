package com.exlab.incubator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FieldExistsException extends RuntimeException{

    public FieldExistsException() {
        super();
    }

    public FieldExistsException(String message) {
        super(message);
    }

    public FieldExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldExistsException(Throwable cause) {
        super(cause);
    }
}
