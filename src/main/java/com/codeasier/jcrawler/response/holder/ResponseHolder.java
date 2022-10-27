package com.codeasier.jcrawler.response.holder;

import com.codeasier.jcrawler.response.component.ResponseStatus;

import java.io.InputStream;


public interface ResponseHolder{
    Exception getException();
    ResponseStatus getStatus();
    int getResponseCode();
    String getErrorMessage();
    InputStream getResponseStream();
    void finish();
}
