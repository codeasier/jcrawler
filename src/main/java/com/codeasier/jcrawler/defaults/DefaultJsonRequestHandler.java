package com.codeasier.jcrawler.defaults;

import com.codeasier.jcrawler.handler.AbstractRequestHandler;
import com.codeasier.jcrawler.response.holder.BasicResponseHolder;

public class DefaultJsonRequestHandler extends AbstractRequestHandler {
    @Override
    protected BasicResponseHolder generateNewResponseHolder(){
        return new DefaultJsonResponseHolder();
    }
}
