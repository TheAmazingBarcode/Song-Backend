package org.barcode.lyricsservice.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.Getter;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;


public class ElasticClient {

    private static ElasticClient instance;

    @Getter
    private ElasticsearchClient client;

    private final String url;
    private final String key;

    private ElasticClient(String url, String key) {
        this.url = url;
        this.key = key;
    }

    public static ElasticClient getInstance(String url, String key) {
        if (instance == null) {
            instance = new ElasticClient(url, key);
            instance.client = initRestClient();
        }
        return instance;
    }

    public static ElasticClient getInstance() {
        if (instance == null) {
            throw new IllegalArgumentException("Cannot initialize the Elastic Search Rest Client. No credentials provided.");
        }
        return getInstance(null, null);
    }

    private static ElasticsearchClient initRestClient() {
        RestClient restClient = RestClient.builder(HttpHost.create(instance.url))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + instance.key)
                })
                .build();

        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }



}
