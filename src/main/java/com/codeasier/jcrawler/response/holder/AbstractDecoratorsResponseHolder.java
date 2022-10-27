package com.codeasier.jcrawler.response.holder;

import com.codeasier.jcrawler.response.component.ResponseStatus;

import java.io.InputStream;

public abstract class AbstractDecoratorsResponseHolder extends BasicResponseHolder{
    private BasicResponseHolder basicHolder;

    public AbstractDecoratorsResponseHolder(BasicResponseHolder basicHolder){
        this.basicHolder = basicHolder;
    }

    @Override
    public InputStream getResponseStream() {
        return basicHolder.getResponseStream();
    }

    @Override
    public String getErrorMessage() {
        return basicHolder.getErrorMessage();
    }

    @Override
    public Exception getException() {
        return basicHolder.getException();
    }

    @Override
    public ResponseStatus getStatus() {
        return basicHolder.getStatus();
    }

    @Override
    public int getResponseCode() {
        return basicHolder.getResponseCode();
    }
}
