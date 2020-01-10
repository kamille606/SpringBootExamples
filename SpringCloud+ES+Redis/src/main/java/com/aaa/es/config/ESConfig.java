package com.aaa.es.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class ESConfig {

    @Autowired
    private ESProperties esProperties;

    @Bean("transportClient")
    public TransportClient getTransportClient() {

        TransportClient transportClient = null;

        try {
            Settings settings = Settings.builder()
                    .put("cluster.name",esProperties.getClusterName())
                    .put("node.name",esProperties.getNodeName())
                    .put("client.transport.sniff",true)
                    .put("thread_pool.search.size",esProperties.getPool()).build();

            transportClient = new PreBuiltTransportClient(settings);

            TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(esProperties.getIp()),Integer.parseInt(esProperties.getPort()));

            transportClient.addTransportAddress(transportAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return transportClient;
    }

}
