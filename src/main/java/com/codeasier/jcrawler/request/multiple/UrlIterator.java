package com.codeasier.jcrawler.request.multiple;

import com.codeasier.jcrawler.request.component.Header;
import com.codeasier.jcrawler.request.component.Param;

public interface UrlIterator {
    void register(String base_url, Header header, Param param);
    boolean hasNext();
    String next();
}
