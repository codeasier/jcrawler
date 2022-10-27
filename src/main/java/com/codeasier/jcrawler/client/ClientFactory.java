package com.codeasier.jcrawler.client;

import com.codeasier.jcrawler.request.component.Param;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public interface ClientFactory {
    void registerCrawler(ClientConsumer clientConsumer,int max_client_size);
    void releaseCrawler(ClientConsumer clientConsumer);
    CloseableHttpClient deliverClient(ClientConsumer clientConsumer, Param param);
    int recoveryClient(ClientConsumer clientConsumer, HttpClient client);
    boolean isPoolExhausted();
    boolean isConsumerClientExhausted(ClientConsumer clientConsumer);

    CloseableHttpClient generateClient(PoolingHttpClientConnectionManager connectionManager,Param param);
}
