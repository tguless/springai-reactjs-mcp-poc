package com.example.duffel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class DuffelConfig {
    
    @Value("${duffel.api-key}")
    private String apiKey;
    
    @Value("${duffel.base-url}")
    private String baseUrl;
} 