package com.codeasier.jcrawler.defaults;

import com.codeasier.jcrawler.crawler.single.SingleRequestCrawler;
import com.codeasier.jcrawler.client.ClientFactory;

import com.codeasier.jcrawler.request.single.UrlBuilder;
import com.codeasier.jcrawler.handler.RequestHandler;
import com.codeasier.jcrawler.response.holder.ResponseHolder;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.List;

public class DefaultSyncSingleRequestCrawler extends SingleRequestCrawler {
    @Override
    public RequestHandler request() throws Exception {
        prepare();
        // 同步请求
        String url = this.getUrlBuilder().build(this.getBaseUrl(), this.getHeader(), this.getParam());
        HttpClient client = clientRequired();
        doSingleRequest(url, getMethod(), getHeader(), getParam(), client, 0);

        // 完成
        this.getRequestHandler().finish();
        return this.getRequestHandler();
    }

    @Override
    protected ClientFactory getDefaultClientFactory() {
        return new DefaultHttpClientFactory(new PoolingHttpClientConnectionManager());
    }

    @Override
    protected RequestHandler getDefaultRequestHandler() {
        return new DefaultJsonRequestHandler();
    }

    @Override
    protected UrlBuilder getDefaultUrlBuilder() {
        return new DefaultUrlBuilder();
    }

    @Override
    protected ResponseHolder getResponse() {
        List<ResponseHolder> handlerResponse = this.getRequestHandler().returnHoldersInRequestSerialNumber();
        ResponseHolder holder = null;
        if (handlerResponse.size() > 0) holder = handlerResponse.get(0);
        return holder;
    }
}
