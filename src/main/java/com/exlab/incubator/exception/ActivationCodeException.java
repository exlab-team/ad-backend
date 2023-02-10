package com.exlab.incubator.exception;

public class ActivationCodeException extends RuntimeException {

    public ActivationCodeException() {
        super();
    }

    public ActivationCodeException(String message) {
        super(message);
    }

    public ActivationCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivationCodeException(Throwable cause) {
        super(cause);
    }
}
