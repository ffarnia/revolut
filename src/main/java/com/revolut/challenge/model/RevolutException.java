package com.revolut.challenge.model;

/**
 * Created by Fazel on 12/11/2019.
 */
public class RevolutException extends RuntimeException {

    private int code;
    private String message;

    public RevolutException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
