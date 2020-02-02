package com.revolut.challenge.model;

/**
 * @author Fazel Farnia
 */
public enum ResponseStatus {
    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    UNPROCCESSABLE(422),
    METHOD_NOT_ALLOWED(405);

    private int code;

    ResponseStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
