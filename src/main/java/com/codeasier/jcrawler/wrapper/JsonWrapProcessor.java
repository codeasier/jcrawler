package com.codeasier.jcrawler.wrapper;

import com.alibaba.fastjson.JSONObject;

public interface JsonWrapProcessor {
    void beforeWrap(JSONObject jsonObject);
    void afterWrap(JSONObject jsonObject,Object wrapObject);
}
