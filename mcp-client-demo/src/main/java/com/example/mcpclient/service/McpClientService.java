package com.example.mcpclient.service;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for communicating with the MCP server through the MCP client.
 * This service is only loaded if the spring.ai.mcp.client.enabled property is true.
 * 
 * <p>To run this application properly:
 * <ol>
 *   <li>First start the standalone MCP server from blogCode/mcp-server-demo
 *       using 'mvn spring-boot:run'</li>
 *   <li>Then start this application, which will connect to that server</li>
 * </ol>
 * <p>Make sure both configurations are pointing to the same URL (http://localhost:8092/api by default).
 */
@Service
@ConditionalOnProperty(name = "spring.ai.mcp.client.enabled", havingValue = "true", matchIfMissing = false)
public class McpClientService {
    
    private static final Logger logger = LoggerFactory.getLogger(McpClientService.class);
    
    @Autowired(required = false)
    private List<McpSyncClient> mcpSyncClients;
    
    @Autowired
    private Environment environment;
    
    @Getter
    private List<McpSchema.Tool> availableTools = Collections.emptyList();
    
    /**
     * Initialize the MCP client and connect to the MCP server.
     */
    @PostConstruct
    public void init() {
        try {
            // Log the MCP configuration at startup
            String url = environment.getProperty("spring.ai.mcp.client.sse.connections.local-server.url");
            logger.info("MCP Client configuration - URL: {}", url);
            
            String enabled = environment.getProperty("spring.ai.mcp.client.enabled");
            logger.info("MCP Client configuration - Enabled: {}", enabled);
            
            if (mcpSyncClients == null) {
                logger.error("mcpSyncClients list is null - Spring bean injection failed");
                return;
            }
            
            if (mcpSyncClients.isEmpty()) {
                logger.warn("mcpSyncClients list is empty. Make sure the MCP server is configured properly.");
                logger.warn("Available Spring beans: {}", environment.getProperty("spring.beans.registered"));
                return;
            }
            
            logger.info("Found {} MCP sync clients", mcpSyncClients.size());
            logger.info("MCP client integration is enabled");
            
            // Collect tools from all clients
            List<McpSchema.Tool> allTools = new ArrayList<>();
            
            // Log each client and collect their tools
            for (McpSyncClient client : mcpSyncClients) {
                try {
                    logger.info("MCP client: {}", client);
                    
                    // Get tools from MCP server
                    McpSchema.ListToolsResult toolsResult = client.listTools();
                    if (toolsResult != null && toolsResult.tools() != null) {
                        allTools.addAll(toolsResult.tools());
                        logger.info("Found {} tools from MCP client", toolsResult.tools().size());
                        
                        // Log tool details
                        toolsResult.tools().forEach(tool -> 
                            logger.info("Tool: {} - {}", tool.name(), tool.description()));
                    }
                } catch (Exception e) {
                    logger.warn("Failed to get tools from MCP client: {}", e.getMessage(), e);
                }
            }
            
            this.availableTools = allTools;
            
            if (!allTools.isEmpty()) {
                logger.info("Total tools found: {}", allTools.size());
            } else {
                logger.warn("No tools found from any MCP client");
            }
        } catch (Exception e) {
            logger.error("Failed to initialize MCP clients: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Call a tool on the MCP server with the given input.
     *
     * @param toolName The name of the tool to call
     * @param input The input parameters for the tool
     * @return The result of the tool invocation
     */
    public Object callTool(String toolName, Map<String, Object> input) {
        logger.info("Calling MCP tool: {} with input: {}", toolName, input);
        
        if (mcpSyncClients == null || mcpSyncClients.isEmpty()) {
            logger.error("No MCP clients available to call tool: {}", toolName);
            return Map.of("error", "No MCP clients available");
        }
        
        // Preprocess input to handle numeric string values
        Map<String, Object> processedInput = preprocessInput(input);
        logger.debug("Processed input: {}", processedInput);
        
        // Try each client until we succeed
        for (McpSyncClient client : mcpSyncClients) {
            try {
                // Create call tool request
                McpSchema.CallToolRequest request = new McpSchema.CallToolRequest(toolName, processedInput);
                
                // Call the tool and log the response
                McpSchema.CallToolResult result = client.callTool(request);
                logger.debug("Raw tool result: {}", result);
                
                if (result == null) {
                    logger.error("Null result from calling tool: {}", toolName);
                    continue;
                }
                
                // Check if the result indicates an error
                if (Boolean.TRUE.equals(result.isError())) {
                    logger.error("Tool returned an error: {}", toolName);
                    
                    // Try to extract error details
                    if (result.content() != null && !result.content().isEmpty()) {
                        for (McpSchema.Content content : result.content()) {
                            if (content instanceof McpSchema.TextContent) {
                                String errorText = ((McpSchema.TextContent) content).text();
                                logger.error("Tool error details: {}", errorText);
                                return Map.of("error", errorText);
                            }
                        }
                    }
                    
                    return Map.of("error", "Tool execution returned an error");
                }
                
                // Try to extract content from the result
                if (result.content() != null && !result.content().isEmpty()) {
                    // Extract text content if available
                    for (McpSchema.Content content : result.content()) {
                        if (content instanceof McpSchema.TextContent) {
                            String textContent = ((McpSchema.TextContent) content).text();
                            // Check if the text contains error messages
                            if (textContent != null && textContent.contains("Exception")) {
                                logger.error("Tool returned text with exception: {}", textContent);
                                return Map.of("error", textContent);
                            }
                            return Map.of("result", textContent);
                        }
                    }
                }
                
                // Return a generic success message if no content was extracted
                return Map.of(
                    "success", true,
                    "message", "Tool executed successfully but no content was returned"
                );
            } catch (Exception e) {
                logger.error("Failed to call tool '{}' on client: {}", toolName, e.getMessage(), e);
            }
        }
        
        logger.error("Tool not found or failed to invoke: {}", toolName);
        return Map.of("error", "Tool not found or failed to invoke: " + toolName);
    }
    
    /**
     * Preprocess input parameters to handle string-to-number conversions.
     */
    private Map<String, Object> preprocessInput(Map<String, Object> input) {
        Map<String, Object> result = new HashMap<>();
        
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if (value instanceof String) {
                String strValue = (String) value;
                // Try to convert strings that look like numbers to actual number types
                try {
                    // Check if it's a decimal number
                    if (strValue.contains(".")) {
                        result.put(key, Double.parseDouble(strValue));
                        logger.debug("Converted string '{}' to Double for key '{}'", strValue, key);
                    } else {
                        // Try as integer
                        result.put(key, Integer.parseInt(strValue));
                        logger.debug("Converted string '{}' to Integer for key '{}'", strValue, key);
                    }
                } catch (NumberFormatException e) {
                    // If conversion fails, keep as string
                    result.put(key, strValue);
                    logger.debug("Kept value as string for key '{}': {}", key, strValue);
                }
            } else {
                // Keep other types as is
                result.put(key, value);
            }
        }
        
        return result;
    }
    
    /**
     * Check if the MCP client is connected and ready to use.
     *
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return mcpSyncClients != null && !mcpSyncClients.isEmpty() && !availableTools.isEmpty();
    }
    
    /**
     * Get a list of available tools from the MCP server.
     *
     * @return List of available tools
     */
    public List<Map<String, String>> getAvailableTools() {
        List<Map<String, String>> tools = new ArrayList<>();
        
        for (McpSchema.Tool tool : availableTools) {
            Map<String, String> toolInfo = new HashMap<>();
            toolInfo.put("name", tool.name());
            toolInfo.put("description", tool.description());
            tools.add(toolInfo);
        }
        
        return tools;
    }
} 