package com.codeasier.jcrawler.defaults;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codeasier.jcrawler.response.holder.BasicResponseHolder;
import com.codeasier.jcrawler.wrapper.JsonWrapProcessor;
import com.codeasier.jcrawler.wrapper.JsonWrapper;

import java.io.*;
import java.util.LinkedList;
import java.util.List;


/**
 *  ResponseHolder的json装饰器
 */
public class DefaultJsonResponseHolder extends BasicResponseHolder implements JsonWrapper {
    private final String default_encoding = "utf-8";

    // json
    private String cached_json_string;
    private JSONObject cached_json_object;

    private Object cached_target_entity;

    // wrapper
    private List<JsonWrapProcessor> processors = new LinkedList<>();

    public DefaultJsonResponseHolder(){

    }

    // 请求完成时,解析inputStream
    @Override
    protected void onComplete() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.getResponseStream()));

            StringBuilder temp = new StringBuilder();
            String line;

            while ((line= reader.readLine())!=null)temp.append(line);
            this.cached_json_string = temp.toString();
            this.cached_json_object = JSON.parseObject(temp.toString());
        }catch (Exception e){
            e.printStackTrace();
            setException(e);
        }
    }

    @Override
    public JsonWrapper registerProcessor(JsonWrapProcessor processor) {
        this.processors.add(processor);
        return this;
    }

    @Override
    public <T> T wrapJsonToEntity(Class<T> targetClass) {
        if(cached_target_entity==null) {
            doProcessWrapBefore(processors,cached_json_object);
            doWrap(targetClass);
            doProcessWrapAfter(processors,cached_json_object,this.cached_target_entity);
        }
        return (T) cached_target_entity;
    }

    public void doWrap(Class targetClass){
        this.cached_target_entity = cached_json_object.toJavaObject(targetClass);
    }
}
