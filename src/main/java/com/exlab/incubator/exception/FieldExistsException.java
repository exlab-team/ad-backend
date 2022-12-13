package com.exlab.incubator.exception;


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
