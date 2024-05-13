package org.barcode.gatewayservice.security;

import org.barcode.gatewayservice.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtUtil jwtUtil(){return JwtUtil.getInstance();}

}
