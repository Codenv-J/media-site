package com.coden.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @ClassName ElasticSearchConfig
 * @Description ES的配置信息
 * @Version 1.0
 **/
@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${cloud.elasticsearch.host}")
    private String esHost;

    @Value("${cloud.elasticsearch.port}")
    private int esPort;

    @Value("${cloud.elasticsearch.username}")
    private String esUserName;

    @Value("${cloud.elasticsearch.password}")
    private String esPassword;


    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(esHost+":"+esPort).withBasicAuth(esUserName,esPassword)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }


    // 在业务启动的时候进行初始化
    // https://blog.csdn.net/wdz985721191/article/details/122866091



}
