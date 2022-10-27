package com.codeasier.jcrawler.crawler.multiple;

import com.codeasier.jcrawler.crawler.BasicAbstractCrawler;
import com.codeasier.jcrawler.request.multiple.UrlIterator;
import com.codeasier.jcrawler.response.holder.ResponseHolder;

import java.util.List;

public abstract class MultipleRequestCrawler extends BasicAbstractCrawler {
    private UrlIterator urlIterator;

    @Override
    protected void prepare() {
        super.prepare();
        prepareUrlIterator();
    }

    protected void prepareUrlIterator() {
        if (!this.isUrlIteratorPrepared()) {
            registerUrlIterator(this.getDefaultUrlIterator());
            if(!isRequestHandlerPrepared())throw new RuntimeException("未注册url迭代器,且获取缺省url迭代器失败");
            this.urlIterator.register(this.getBaseUrl(),this.getHeader(),this.getParam());
        }
    }

    protected abstract UrlIterator getDefaultUrlIterator();

    protected UrlIterator getUrlIterator(){
        return this.urlIterator;
    }

    public MultipleRequestCrawler registerUrlIterator(UrlIterator urlIterator) {
        this.urlIterator = urlIterator;
        if(urlIterator!=null)urlIterator.register(getBaseUrl(),getHeader(),getParam());
        return this;
    }

    public boolean isUrlIteratorPrepared(){
        return this.urlIterator!=null;
    }

    protected abstract List<ResponseHolder> getResponse();
}
