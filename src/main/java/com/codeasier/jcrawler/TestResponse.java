package com.codeasier.jcrawler;

public class TestResponse {
    private String retcode;
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getMessage() {
        return message;
    }

    public String getRetcode() {
        return retcode;
    }
}
