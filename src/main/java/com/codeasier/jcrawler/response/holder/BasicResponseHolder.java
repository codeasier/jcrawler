package com.codeasier.jcrawler.response.holder;

import com.codeasier.jcrawler.response.component.ResponseStatus;

import java.io.InputStream;

public abstract class BasicResponseHolder implements ResponseHolder {
    private ResponseStatus status;
    private int response_code;

    private InputStream response_stream;
    private String error_message;
    private Exception exception;

    public BasicResponseHolder() {

    }

    public ResponseHolder setResponseStream(InputStream in) {
        this.response_stream = in;
        return this;
    }

    @Override
    public String getErrorMessage(){
        if (status == ResponseStatus.Exception) this.error_message = exception.getMessage();
        return this.error_message;
    }

    @Override
    public InputStream getResponseStream() {
        return this.response_stream;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public ResponseStatus getStatus() {
        return this.status;
    }

    @Override
    public int getResponseCode() {
        return this.response_code;
    }

    @Override
    public void finish() {
        onComplete();
    }

    protected abstract void onComplete();

    public BasicResponseHolder setException(Exception exception) {
        this.exception = exception;
        return this;
    }


    public ResponseHolder setErrorMessage(String errorMessage) {
        this.error_message = errorMessage;
        return this;
    }

    public BasicResponseHolder setStatus(ResponseStatus status) {
        this.status = status;
        return this;
    }

    public BasicResponseHolder setResponseCode(int response_code) {
        this.response_code = response_code;
        return this;
    }
}
