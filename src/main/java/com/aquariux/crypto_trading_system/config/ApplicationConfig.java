package com.aquariux.crypto_trading_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.IdGenerator;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Configuration
public class ApplicationConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UUID uuid() {
        return UUID.randomUUID();
    }
}
