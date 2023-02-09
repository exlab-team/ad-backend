package com.exlab.incubator.exception;

public class IncorrectFieldException extends RuntimeException{

    public IncorrectFieldException() {
        super();
    }

    public IncorrectFieldException(String message) {
        super(message);
    }

    public IncorrectFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectFieldException(Throwable cause) {
        super(cause);
    }
}
