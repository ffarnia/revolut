package com.revolut.challenge.model;

/**
 * Created by Fazel on 12/11/2019.
 */
public enum ResponseStatus {
    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    INTERNAL_ERROR(500),
    METHOD_NOT_ALLOWED(405);

    private int code;

    ResponseStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
