package com.example.mcpclient.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.modelcontextprotocol.client.McpSyncClient;

/**
 * Configuration for Chat Client with MCP tools.
 */
@Configuration
public class ChatClientConfiguration {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatClientConfiguration.class);
    
    /**
     * Creates a ToolCallbackProvider that collects tools from MCP clients.
     * This will automatically discover and register tools from the MCP server.
     * 
     * @param mcpSyncClients List of MCP clients
     * @return The tool callback provider with all MCP tools
     */
    @Bean("mcpToolCallbackProvider")
    @Primary
    public ToolCallbackProvider mcpToolCallbackProvider(List<McpSyncClient> mcpSyncClients) {
        if (mcpSyncClients == null || mcpSyncClients.isEmpty()) {
            logger.warn("No MCP clients found for tool discovery");
            return null;
        }
        
        logger.info("Creating tool callback provider with {} MCP clients", mcpSyncClients.size());
        
        // Create a tool callback provider with all MCP tools
        SyncMcpToolCallbackProvider provider = new SyncMcpToolCallbackProvider(mcpSyncClients);
        
        // Log basic information about the provider
        logger.info("Successfully created MCP tool callback provider");
        
        return provider;
    }
} 