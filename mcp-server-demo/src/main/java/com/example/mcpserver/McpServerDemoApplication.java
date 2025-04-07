package com.example.mcpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@EnableFeignClients(basePackages = {"com.example.common", "com.example.mcpserver"})
@SpringBootApplication(scanBasePackages = {"com.example.common", "com.example.mcpserver"})
public class McpServerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerDemoApplication.class, args);
    }
} 