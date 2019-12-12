package com.revolut.challenge.exception;

/**
 * Created by Fazel on 12/11/2019.
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
