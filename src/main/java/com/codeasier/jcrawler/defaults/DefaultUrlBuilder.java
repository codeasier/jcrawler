package com.codeasier.jcrawler.defaults;

import com.codeasier.jcrawler.request.component.Header;
import com.codeasier.jcrawler.request.component.Param;
import com.codeasier.jcrawler.request.single.UrlBuilder;

public class DefaultUrlBuilder implements UrlBuilder {
    @Override
    public String build(String base_url, Header header, Param param) {
        if(!param.hasUrlParams())return base_url;
        StringBuilder res = new StringBuilder(base_url);
        res.append("?");
        res.append(param.getUrlParamStr());
        return res.toString();
    }
}
