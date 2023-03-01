package com.exlab.incubator.exception;

import com.exlab.incubator.entity.UserAccount;

public class UserAccountNotFoundException extends RuntimeException {

    public UserAccountNotFoundException() {
        super();
    }

    public UserAccountNotFoundException(String message) {
        super(message);
    }

    public UserAccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAccountNotFoundException(Throwable cause) {
        super(cause);
    }
}
