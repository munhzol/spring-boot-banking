package com.eva.banking.exception;

public class NoUserLoggedInException extends RuntimeException {

    public NoUserLoggedInException(String message) {
        super(message);
    }
}
