package com.revolut.challenge.exception;

/**
 * Created by Fazel on 12/12/2019.
 */
public class NotFoundException extends RevolutException {
    public NotFoundException(int code, String message) {
        super(code, message);
    }
}
