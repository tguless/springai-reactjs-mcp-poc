package com.example.mcpclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.example.common", "com.example.mcpclient"})
@EnableFeignClients(basePackages = {"com.example.common", "com.example.mcpclient"})
public class McpClientDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpClientDemoApplication.class, args);
    }
} 