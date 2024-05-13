package org.barcode.lyricsservice.configuration;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ClientProperties {

    @Value("${elastic.client.url}")
    private String url;

    @Value("${elastic.client.username}")
    private String username;

    @Value("${elastic.client.password}")
    private String password;

    @Value("${elastic.client.key}")
    private String key;
}
