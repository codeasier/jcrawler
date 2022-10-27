package com.codeasier.jcrawler.crawler;

import com.codeasier.jcrawler.client.ClientConsumer;
import com.codeasier.jcrawler.client.ClientFactory;
import com.codeasier.jcrawler.request.component.Header;
import com.codeasier.jcrawler.request.component.Param;
import com.codeasier.jcrawler.handler.RequestHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

public interface BaseCrawler extends ClientConsumer {
    // 链式
    BaseCrawler registerHandler(RequestHandler handler);
    BaseCrawler registerClientFactory(ClientFactory clientFactory)throws Exception;

    HttpRequestBase buildRequest(String url, String method, Header header, Param param)throws Exception;
    RequestHandler request()throws Exception;
    void requestPreparedNotify(HttpRequestBase requestBase,int request_serial_number)throws Exception;
    void requestBeforeNotify(HttpRequestBase request)throws Exception;
    void requestAfterNotify(HttpRequestBase request, HttpResponse response)throws Exception;
    void exceptionNotify(HttpRequestBase request, HttpResponse response,Exception e);
    void close()throws Exception;

    boolean isClientFactoryPrepared();
    boolean isRequestHandlerPrepared();

    String getBaseUrl();
    Header getHeader();
    String getMethod();
    Param getParam();
}
