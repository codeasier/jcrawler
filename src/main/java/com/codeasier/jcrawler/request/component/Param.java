package com.codeasier.jcrawler.request.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Param {
    private HashMap<String, String> url_params = new HashMap<>();
    private HashMap<String, Object> body_params = new HashMap<>();

    private int read_time_out = 10000;
    private int connect_time_out = 10000;

    public static Param build(String json) throws Exception {
        JSONObject jsonObject = JSON.parseObject(json);
        return build(jsonObject);
    }

    public static Param build(JSONObject json) throws Exception {
        Param param = new Param();

        param.body_params.putAll(json);

        return param;
    }

    public boolean addUrlParam(String key,String value) {
        return url_params.put(key,value)!=null;
    }
    public boolean addBodyParam(String key,Object value){
        return body_params.put(key,value)!=null;
    }
    public void setReadTimeOut(int read_time_out) {
        this.read_time_out = read_time_out;
    }

    public void setConnectTimeOut(int connect_time_out) {
        this.connect_time_out = connect_time_out;
    }

    public int getConnectTimeOut() {
        return connect_time_out;
    }

    public int getReadTimeOut() {
        return read_time_out;
    }

    public Map<String,String> getUrlParams(){
        return this.url_params;
    }
    public Map<String,Object> getBodyParams(){
        return this.body_params;
    }
    public String getBodyParamsJsonString(){
        return hasBodyParams()?JSON.toJSONString(this.body_params):null;
    }
    public boolean hasUrlParams(){
        return !this.url_params.isEmpty();
    }
    public boolean hasBodyParams(){
        return !this.body_params.isEmpty();
    }
    public String getUrlParamStr(){
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String,String>> iterator = url_params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry = iterator.next();
            sb.append(String.format("%s=%s",entry.getKey(),entry.getValue()));
            sb.append("&");
        }
        return sb.deleteCharAt(sb.length()-1).toString();
    }
}
