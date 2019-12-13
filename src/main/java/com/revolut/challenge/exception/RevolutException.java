package com.revolut.challenge.exception;

/**
 * @author Fazel Farnia
 *         Custumize all exceptions all application in supersclass and it is a subclass of RuntimeException
 *         Every exception has a code and message for better understanding
 */
public class RevolutException extends RuntimeException {

    private int code;

    public RevolutException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
