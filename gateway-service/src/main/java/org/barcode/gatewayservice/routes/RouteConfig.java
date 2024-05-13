package org.barcode.gatewayservice.routes;

import org.barcode.gatewayservice.security.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Autowired
    private AuthFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("song-delete", r->r.path("/*/delete/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://song-service/"))
                .route("lyrics-delete", r->r.path("/*/delete/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://lyrics-service/"))
                .route("song-update", r->r.path("/*/update/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://song-service/"))
                .route("song-new",r->r.path("/*/upload/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://song-service"))
                .build();
    }
}
