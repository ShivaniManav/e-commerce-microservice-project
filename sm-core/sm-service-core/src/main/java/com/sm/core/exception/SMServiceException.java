package com.sm.core.exception;

public class SMServiceException extends RuntimeException {

    private Integer responseCode;

    private Integer errorCode;

    public SMServiceException(String message) {
        super(message);
    }

    public SMServiceException(String message, Integer responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public SMServiceException(String message, Integer responseCode, Integer errorCode) {
        super(message);
        this.responseCode = responseCode;
        this.errorCode = errorCode;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
