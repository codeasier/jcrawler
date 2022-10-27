package com.codeasier.jcrawler.client;

import org.apache.http.client.HttpClient;

public interface ClientConsumer {
    ClientConsumer registerClientFactory(ClientFactory clientFactory)throws Exception;
    HttpClient applyClientFromClientFactory()throws Exception;
    int returnClientToClientFactory(HttpClient client)throws Exception;


    HttpClient clientRequired()throws Exception;
    void clientUsingFinish(HttpClient client)throws Exception;
}
