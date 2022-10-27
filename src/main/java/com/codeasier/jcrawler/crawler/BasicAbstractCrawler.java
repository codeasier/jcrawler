package com.codeasier.jcrawler.crawler;

import com.codeasier.jcrawler.client.ClientFactory;
import com.codeasier.jcrawler.request.component.Header;
import com.codeasier.jcrawler.request.component.Method;
import com.codeasier.jcrawler.request.component.Param;
import com.codeasier.jcrawler.handler.RequestHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

public abstract class BasicAbstractCrawler implements BaseCrawler {
    private RequestHandler requestHandler;

    private String base_url;

    private String method;

    private Header header;

    private Param param;

    private ClientFactory clientFactory;

    private int max_client_size = 1;

    private boolean is_init = false;

    @Override
    public HttpClient applyClientFromClientFactory() throws Exception {
        return clientFactory.deliverClient(this, this.param);
    }

    @Override
    public int returnClientToClientFactory(HttpClient client) throws Exception {
        return this.clientFactory.recoveryClient(this, client);
    }

    @Override
    public HttpClient clientRequired() throws Exception {
        return this.applyClientFromClientFactory();
    }

    @Override
    public void clientUsingFinish(HttpClient client) throws Exception {
        this.returnClientToClientFactory(client);
    }

    @Override
    public BaseCrawler registerClientFactory(ClientFactory clientFactory){
        this.clientFactory = clientFactory;
        clientFactory.registerCrawler(this, max_client_size);
        return this;
    }

    public BasicAbstractCrawler build(String base_url, String method, Header header, Param param) {
        this.base_url = base_url;
        this.method = method;
        this.header = header;
        this.param = param;
        this.is_init = true;
        return this;
    }

    public void setMaxClientSize(int max_client_size) {
        this.max_client_size = max_client_size;
    }

    /**
     * 构建一个httpRequest的模板方案
     * @param url
     * @param method
     * @param header
     * @param param
     * @return
     */
    @Override
    public HttpRequestBase buildRequest(String url, String method, Header header, Param param) {
        HttpRequestBase request = null;

        // 依据method获取新的request,若为post请求则需放入body参数
        switch (method) {
            case Method.GET:
                request = new HttpGet(url);
                break;
            case Method.POST:
                HttpPost postRequest = new HttpPost(url);
                if (param != null && param.hasBodyParams())
                    postRequest.setEntity(new StringEntity(param.getBodyParamsJsonString(), "utf-8"));
                request = postRequest;
                break;
        }

        request.setHeaders(header.getHeaders());
        return request;
    }

    @Override
    public Header getHeader() {
        return this.header;
    }

    @Override
    public String getBaseUrl() {
        return this.base_url;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public Param getParam() {
        return this.param;
    }

    @Override
    public BaseCrawler registerHandler(RequestHandler handler) {
        requestHandler = handler;
        return this;
    }

    @Override
    public abstract RequestHandler request() throws Exception;


    /**
     * 发送一个单http请求的模板方案
     * @param url
     * @param method
     * @param header
     * @param param
     * @param client
     * @param request_serial_number
     * @throws Exception
     */
    protected void doSingleRequest(String url, String method, Header header, Param param, HttpClient client, int request_serial_number) throws Exception {
        if (requestHandler == null) throw new RuntimeException("未注册请求处理器！");
        HttpRequestBase request = null;
        HttpResponse response = null;
        try {
            // 构建请求
            request = this.buildRequest(url, method, header, param);

            // prepared
            requestPreparedNotify(request, request_serial_number);


            // handler.requestBefore
            requestBeforeNotify(request);

            // doSent
            response = client.execute(request);

            // handler.requestAfter
            requestAfterNotify(request, response);
        } catch (Exception e) {
            exceptionNotify(request, response, e);
        } finally {
            this.clientUsingFinish(client);
        }
    }

    /**
     * 在发起请求前需要做的前期准备,至少包括:
     * checkInit
     * prepare HttpClientFactory
     * prepare RequestHandler
     */
    //
    protected void prepare()throws RuntimeException{
        checkInit();
        prepareClientFactory();
        prepareRequestHandler();
    }

    protected void checkInit(){
        if(!is_init)throw new RuntimeException("Crawler未进行初始化!");
    }

    protected void prepareClientFactory()throws RuntimeException{
        if(!this.isClientFactoryPrepared()){
            registerClientFactory(getDefaultClientFactory());
            if(!this.isClientFactoryPrepared())throw new RuntimeException("未注册HttpClient工厂,且获取缺省工厂失败!");
        }
    }
    protected void prepareRequestHandler()throws RuntimeException{
        if(!this.isRequestHandlerPrepared()){
            registerHandler(getDefaultRequestHandler());
            if(!this.isRequestHandlerPrepared())throw new RuntimeException("未注册RequestHandler,且注册缺省处理器失败!");
        }
    }
    protected abstract ClientFactory getDefaultClientFactory();
    protected abstract RequestHandler getDefaultRequestHandler();

    @Override
    public void requestPreparedNotify(HttpRequestBase request, int request_serial_number) throws Exception {
        requestHandler.readyToSent(request, request_serial_number);
    }

    @Override
    public void requestBeforeNotify(HttpRequestBase request) throws Exception {
        requestHandler.beforeRequest(request);
    }

    @Override
    public boolean isRequestHandlerPrepared() {
        return this.requestHandler!=null;
    }

    @Override
    public boolean isClientFactoryPrepared() {
        return this.clientFactory!=null;
    }

    @Override
    public void requestAfterNotify(HttpRequestBase request, HttpResponse response) throws Exception {
        requestHandler.afterRequest(request, response);
    }

    @Override
    public void exceptionNotify(HttpRequestBase request, HttpResponse response, Exception e) {
        requestHandler.exceptionHandle(request, response, e);
    }

    @Override
    public void close() throws Exception {
        if(clientFactory!=null)clientFactory.releaseCrawler(this);
    }

    protected RequestHandler getRequestHandler() {
        return this.requestHandler;
    }
}
