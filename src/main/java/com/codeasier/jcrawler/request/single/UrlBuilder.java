package com.codeasier.jcrawler.request.single;

import com.codeasier.jcrawler.request.component.Header;
import com.codeasier.jcrawler.request.component.Param;

public interface UrlBuilder {
    String build(String base_url, Header header,Param param);
}
