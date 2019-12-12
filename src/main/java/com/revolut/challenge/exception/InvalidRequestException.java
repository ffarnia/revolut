package com.revolut.challenge.exception;

import com.revolut.challenge.config.RevolutApplication;

/**
 * Created by Fazel on 12/12/2019.
 */
public class InvalidRequestException extends RevolutException {
    public InvalidRequestException(int code, String message) {
        super(code, message);
    }
}
