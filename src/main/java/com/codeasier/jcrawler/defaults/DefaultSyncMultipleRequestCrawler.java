package com.codeasier.jcrawler.defaults;

import com.codeasier.jcrawler.crawler.multiple.MultipleRequestCrawler;
import com.codeasier.jcrawler.client.ClientFactory;
import com.codeasier.jcrawler.request.component.Header;
import com.codeasier.jcrawler.request.component.Param;
import com.codeasier.jcrawler.request.component.ParamModifier;
import com.codeasier.jcrawler.request.multiple.UrlIterator;
import com.codeasier.jcrawler.handler.RequestHandler;
import com.codeasier.jcrawler.response.holder.ResponseHolder;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.List;

public class DefaultSyncMultipleRequestCrawler extends MultipleRequestCrawler {
    private int global_request_count = 0;

    public DefaultSyncMultipleRequestCrawler registerDefaultIterator(List<ParamModifier> modifiers) {
        registerUrlIterator(new DefaultUrlIterator(modifiers));
        return this;
    }

    @Override
    public DefaultSyncMultipleRequestCrawler build(String base_url, String method, Header header, Param param){
        super.build(base_url,method,header,param);
        return this;
    }

    @Override
    public RequestHandler request() throws Exception {
        prepare();
        UrlIterator urlIterator = this.getUrlIterator();
        while (urlIterator.hasNext()){
            String url = urlIterator.next();
            HttpClient client = clientRequired();
            doSingleRequest(url,this.getMethod(),this.getHeader(),this.getParam(),client,global_request_count++);
        }

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
    protected UrlIterator getDefaultUrlIterator() {
        return new DefaultUrlIterator(null);
    }

    @Override
    protected List<ResponseHolder> getResponse() {
        return this.getRequestHandler().returnHoldersInRequestSerialNumber();
    }
}
