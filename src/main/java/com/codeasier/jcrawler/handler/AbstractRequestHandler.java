package com.codeasier.jcrawler.handler;

import com.codeasier.jcrawler.response.holder.BasicResponseHolder;
import com.codeasier.jcrawler.response.holder.ResponseHolder;
import com.codeasier.jcrawler.response.component.ResponseStatus;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractRequestHandler implements RequestHandler {
    private boolean is_finish = false;
    private HashMap<HttpRequestBase, BasicResponseHolder> responseHolders = new HashMap<>();
    private ArrayList<HttpRequestBase> requests = new ArrayList<>();

    @Override
    public void readyToSent(HttpRequestBase request, int request_serial_number) throws Exception {
        requests.add(request_serial_number, request);
        BasicResponseHolder responseHolder = generateNewResponseHolder();
        responseHolders.put(request, responseHolder);
        responseHolder.setStatus(ResponseStatus.PREPARED);
        System.out.println(request.getMethod() + ":" + request.getURI().toString() + "请求就绪...");
    }

    protected abstract BasicResponseHolder generateNewResponseHolder()throws Exception;

    @Override
    public void beforeRequest(HttpRequestBase request) throws Exception {
        BasicResponseHolder responseHolder = responseHolders.get(request);
        responseHolder.setStatus(ResponseStatus.RUNNING);
        System.out.println(request.getMethod() + ":" + request.getURI().toString() + "请求发起...");
    }

    @Override
    public void afterRequest(HttpRequestBase request, HttpResponse response) throws Exception {
        try{
            int resp_code = response.getStatusLine().getStatusCode();
            ResponseStatus status = resp_code == 200 ? ResponseStatus.EXIT_SUCCESS : ResponseStatus.EXIT_FAILED;

            this.getResponseHolder(request)
                    .setStatus(status)
                    .setResponseCode(resp_code)
                    .setResponseStream(response.getEntity().getContent())
                    .finish();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(request.getMethod() + ":" + request.getURI().toString() + "请求执行完毕");
    }

    @Override
    public void exceptionHandle(HttpRequestBase request, HttpResponse response, Exception e) {
        BasicResponseHolder responseHolder = responseHolders.get(request);
        responseHolder.setException(e);
        responseHolder.setStatus(ResponseStatus.Exception);
        System.out.println(request.getMethod() + ":" + request.getURI().toString() + "请求发生异常:" + e.getMessage());
    }

    @Override
    public void finish() {
        this.is_finish = true;
    }

    @Override
    public boolean isFinish() {
        return this.is_finish;
    }

    @Override
    public List<ResponseHolder> returnHoldersInRequestSerialNumber() {
        return getResponseHolders();
    }

    protected List<ResponseHolder> getResponseHolders() {
        List<ResponseHolder> res = new LinkedList<>();
        for (int i = 0; i < requests.size(); i++) {
            HttpRequestBase request = requests.get(i);
            if (request == null) continue;
            ResponseHolder holder = responseHolders.get(request);
            res.add(holder);
        }
        return res;
    }

    protected BasicResponseHolder getResponseHolder(HttpRequestBase request) {
        return responseHolders.get(request);
    }
}
