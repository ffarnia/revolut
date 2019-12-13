package com.revolut.challenge.exception;

/**
 * @author Fazel Farnia
 *         Checks and validate all input requests
 */
public class InvalidRequestException extends RevolutException {
    public InvalidRequestException(int code, String message) {
        super(code, message);
    }
}
