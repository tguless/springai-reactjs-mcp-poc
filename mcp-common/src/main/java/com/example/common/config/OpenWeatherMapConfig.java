package com.example.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class OpenWeatherMapConfig {
    
    @Value("${openweathermap.api-key}")
    private String apiKey;
} 