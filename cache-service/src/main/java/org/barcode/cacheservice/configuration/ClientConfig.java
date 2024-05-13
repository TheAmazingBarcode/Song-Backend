package org.barcode.cacheservice.configuration;

import org.barcode.cacheservice.client.ClientWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public ClientWrapper clientWrapper(){
        return ClientWrapper.getInstance();
    }
}
