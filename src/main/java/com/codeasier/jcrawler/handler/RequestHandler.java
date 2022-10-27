package com.codeasier.jcrawler.handler;

import com.codeasier.jcrawler.response.holder.ResponseHolder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.List;

public interface RequestHandler {
    void readyToSent(HttpRequestBase request,int request_serial_number)throws Exception;
    void beforeRequest(HttpRequestBase request)throws Exception;
    void afterRequest(HttpRequestBase request, HttpResponse response)throws Exception;
    void exceptionHandle(HttpRequestBase request, HttpResponse response,Exception e);
    void finish();
    boolean isFinish();

    List<ResponseHolder> returnHoldersInRequestSerialNumber();
}
