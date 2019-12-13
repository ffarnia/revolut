package com.revolut.challenge.exception;

/**
 * @author Fazel Farnia
 *         Check and validate availablity of entities in repository
 */
public class NotFoundException extends RevolutException {
    public NotFoundException(int code, String message) {
        super(code, message);
    }
}
