package org.barcode.lyricsservice.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticClientConfig {
    private final ClientProperties properties;

    @Autowired
    public ElasticClientConfig(ClientProperties properties) {
        this.properties = properties;
    }

    @Bean
    public ElasticClient client(){
        return ElasticClient.getInstance(properties.getUrl(), properties.getKey());
    }
}
