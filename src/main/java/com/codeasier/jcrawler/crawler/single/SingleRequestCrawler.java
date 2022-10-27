package com.codeasier.jcrawler.crawler.single;

import com.codeasier.jcrawler.crawler.BasicAbstractCrawler;
import com.codeasier.jcrawler.request.single.UrlBuilder;
import com.codeasier.jcrawler.response.holder.ResponseHolder;


public abstract class SingleRequestCrawler extends BasicAbstractCrawler {
     private UrlBuilder urlBuilder;

     public SingleRequestCrawler registerUrlBuilder(UrlBuilder urlBuilder){
          this.urlBuilder = urlBuilder;
          return this;
     }

     @Override
     protected void prepare(){
          super.prepare();
          prepareUrlBuilder();
     }

     protected void prepareUrlBuilder(){
          if(!isUrlBuilderPrepared()){
               this.urlBuilder = getDefaultUrlBuilder();
               if(!isUrlBuilderPrepared())throw new RuntimeException("未注册UrlBuilder,且获取缺省UrlBuilder失败!");
          }
     }

     public boolean isUrlBuilderPrepared(){
          return this.urlBuilder!=null;
     }

     protected abstract UrlBuilder getDefaultUrlBuilder();

     public UrlBuilder getUrlBuilder() {
          return this.urlBuilder;
     }

     protected abstract ResponseHolder getResponse();
}
