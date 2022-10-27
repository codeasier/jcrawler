package com.codeasier.jcrawler.defaults;

import com.codeasier.jcrawler.client.ClientConsumer;
import com.codeasier.jcrawler.client.ClientFactory;
import com.codeasier.jcrawler.request.component.Param;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultHttpClientFactory implements ClientFactory {
    private PoolingHttpClientConnectionManager connectionManager;
    private ConcurrentHashMap<ClientConsumer, HashSet<CloseableHttpClient>> consumerUsingClients = new ConcurrentHashMap<>();
    private ConcurrentHashMap<ClientConsumer, Queue<CloseableHttpClient>> consumerUsableClients = new ConcurrentHashMap<>();
    private ConcurrentHashMap<ClientConsumer, Integer> consumerSizes = new ConcurrentHashMap<>();
    private volatile int client_remain;

    public DefaultHttpClientFactory(PoolingHttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.client_remain = connectionManager.getMaxTotal();
    }

    public void setConnectionManager(PoolingHttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.client_remain = connectionManager.getMaxTotal();
    }

    @Override
    public synchronized void registerCrawler(ClientConsumer clientConsumer, int max_client_size) throws RuntimeException {
        if (consumerSizes.containsKey(clientConsumer)) throw new RuntimeException("已注册过的Consumer");

        consumerSizes.put(clientConsumer, max_client_size);
        consumerUsableClients.put(clientConsumer, new LinkedList<>());
        consumerUsingClients.put(clientConsumer, new HashSet<>());
    }

    @Override
    public synchronized void releaseCrawler(ClientConsumer clientConsumer) throws RuntimeException {
        HashSet<CloseableHttpClient> usingClients = consumerUsingClients.get(clientConsumer);
        if(usingClients.size()>0)throw new RuntimeException("Crawler存在尚未归还的Client,无法释放");

        Queue<CloseableHttpClient> usableClients = consumerUsableClients.get(clientConsumer);

        while (!usableClients.isEmpty()){
            try {
                usableClients.poll().close();
            }catch (IOException e){
                throw new RuntimeException("释放Crawler的client失败");
            }
            client_remain++;
        }

        consumerSizes.remove(clientConsumer);
        consumerUsingClients.remove(clientConsumer);
        consumerUsableClients.remove(clientConsumer);
    }


    @Override
    public synchronized CloseableHttpClient deliverClient(ClientConsumer clientConsumer, Param param) throws RuntimeException {
        if (!consumerSizes.containsKey(clientConsumer)) throw new RuntimeException("未注册的Consumer");
        if (isPoolExhausted()||isConsumerClientExhausted(clientConsumer)) return null;

        consumerSizes.put(clientConsumer, consumerSizes.get(clientConsumer) - 1);

        Queue<CloseableHttpClient> usableClients = this.consumerUsableClients.get(clientConsumer);

        if (usableClients.isEmpty()) {
            usableClients.offer(generateClient(connectionManager, param));
        }

        CloseableHttpClient usableClient = usableClients.poll();

        consumerUsingClients.get(clientConsumer).add(usableClient);
        client_remain--;

        return usableClient;
    }

    @Override
    public synchronized int recoveryClient(ClientConsumer clientConsumer, HttpClient client) throws RuntimeException {
        if (!consumerSizes.containsKey(clientConsumer)) throw new RuntimeException("未注册的Consumer");

        int remain = consumerSizes.get(clientConsumer) + 1;
        consumerSizes.put(clientConsumer, remain);

        consumerUsingClients.get(clientConsumer).remove(client);
        consumerUsableClients.get(clientConsumer).offer((CloseableHttpClient) client);

        client_remain++;
        return remain;
    }

    @Override
    public synchronized boolean isPoolExhausted() {
        return this.client_remain <= 0;
    }
    @Override
    public synchronized boolean isConsumerClientExhausted(ClientConsumer clientConsumer) {
        return this.consumerSizes.get(clientConsumer) <= 0;
    }

    @Override
    public CloseableHttpClient generateClient(PoolingHttpClientConnectionManager connectionManager, Param param) {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(param.getConnectTimeOut())
                .setConnectionRequestTimeout(param.getReadTimeOut())
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(config)
                .setConnectionManager(connectionManager)
                .disableAutomaticRetries()
                .build();
    }
}
