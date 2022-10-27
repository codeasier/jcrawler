package com.codeasier.jcrawler.request.component;

import org.apache.http.message.BasicHeader;

import java.util.Collection;
import java.util.HashMap;

public class Header {
    private HashMap<String, BasicHeader> headers = new HashMap<>();
    private String DEFAULT_HEADER_CONNECTION = "keep-alive";
    private String DEFAULT_HEADER_ACCEPT = "application / json, text / plain, * / *";
    private String DEFAULT_HEADER_AUTHORIZATION = "";
    private String DEFAULT_HEADER_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36 Edg/98.0.1108.56";
    private String DEFAULT_HEADER_ACCEPT_ENCODING = "gzip,deflate";
    private String DEFAULT_HEADER_ACCEPT_LANGUAGE = "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6";
    public Header(){
        headers.put("Connection",new BasicHeader("Connection",DEFAULT_HEADER_CONNECTION));
        headers.put("Accept",new BasicHeader("Accept",DEFAULT_HEADER_ACCEPT));
//        headers.put("Authorization",new BasicHeader("Authorization",DEFAULT_HEADER_AUTHORIZATION));
        headers.put("User-Agent",new BasicHeader("User-Agent",DEFAULT_HEADER_USER_AGENT));
        headers.put("Accept-Language",new BasicHeader("Accept-Language",DEFAULT_HEADER_ACCEPT_LANGUAGE));
        headers.put("Accept-Encoding",new BasicHeader("Accept-Encoding",DEFAULT_HEADER_ACCEPT_ENCODING));

    }
    public Header putHeader(String key,String value){
        BasicHeader basicHeader = new BasicHeader(key,value);
        headers.put(key,basicHeader);
        return this;
    }
    public Collection<BasicHeader> getHeadersCollection() {
        return headers.values();
    }
    public BasicHeader[] getHeaders(){
        Collection<BasicHeader> headers = this.getHeadersCollection();
        return headers.toArray(new BasicHeader[0]);
    }
}
