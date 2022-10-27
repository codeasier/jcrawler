package com.codeasier.jcrawler.request.component;

@FunctionalInterface
public interface ParamModifier {
    void modify(Header header,Param param);
}
