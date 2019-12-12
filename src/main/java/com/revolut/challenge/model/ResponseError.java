package com.revolut.challenge.model;

/**
 * Created by Fazel on 12/11/2019.
 */
public class ResponseError {

    private int errorCode;
    private String errorMessage;

    public ResponseError() {
    }

    public ResponseError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
