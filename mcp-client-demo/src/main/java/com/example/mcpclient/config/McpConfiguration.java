package com.example.mcpclient.config;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.customizer.McpSyncClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.URI;
import java.time.Duration;
import java.util.List;

/**
 * Configuration for the MCP client.
 * This sets up the customizer that will be applied to all MCP clients.
 */
@Configuration
public class McpConfiguration {
    
    private static final Logger log = LoggerFactory.getLogger(McpConfiguration.class);
    
    private final Environment environment;
    
    public McpConfiguration(Environment environment) {
        this.environment = environment;
        // Log the MCP configuration at startup
        String url = environment.getProperty("spring.ai.mcp.client.sse.connections.local-server.url");
        log.info("MCP Client will connect to: {}", url);
        
        String enabled = environment.getProperty("spring.ai.mcp.client.enabled");
        log.info("MCP Client enabled: {}", enabled);
    }
    
    @Bean
    public McpSyncClientCustomizer mcpSyncClientCustomizer() {
        return (String serverConfigName, McpClient.SyncSpec spec) -> {
            log.info("Customizing MCP client for server: {}", serverConfigName);
            
            // Set request timeout
            spec.requestTimeout(Duration.ofSeconds(30));
            
            // Add tool change notification handler
            spec.toolsChangeConsumer(tools -> {
                log.info("MCP tools changed, available tools: {}", tools.size());
                tools.forEach(tool -> log.debug("Tool: {}", tool.name()));
            });
            
            // Add resources change notification handler
            spec.resourcesChangeConsumer(resources -> {
                log.info("MCP resources changed, available resources: {}", resources.size());
                resources.forEach(resource -> log.debug("Resource: {}", resource.name()));
            });
            
            // Add prompts change notification handler
            spec.promptsChangeConsumer(prompts -> {
                log.info("MCP prompts changed, available prompts: {}", prompts.size());
                prompts.forEach(prompt -> log.debug("Prompt: {}", prompt.name()));
            });

            // Add logging handler to handle structured logs from the server
            spec.loggingConsumer(logMsg -> {
                switch (logMsg.level()) {
                    case ERROR:
                        log.error("[MCP] {}: {}", logMsg.logger(), logMsg.data());
                        break;
                    case WARNING:
                        log.warn("[MCP] {}: {}", logMsg.logger(), logMsg.data());
                        break;
                    case INFO:
                        log.info("[MCP] {}: {}", logMsg.logger(), logMsg.data());
                        break;
                    case DEBUG:
                    default:
                        log.debug("[MCP] {}: {}", logMsg.logger(), logMsg.data());
                        break;
                }
            });
        };
    }
} 