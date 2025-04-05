package com.example.mcpclient.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mcpclient.service.McpClientService;

/**
 * REST controller for exposing MCP tools to the frontend.
 * This controller is only available if the McpClientService is present.
 */
@RestController
@RequestMapping("/api/mcp")
@ConditionalOnBean(McpClientService.class)
@CrossOrigin(origins = "*")
public class McpClientController {
    
    private static final Logger logger = LoggerFactory.getLogger(McpClientController.class);
    
    @Autowired
    private McpClientService mcpClientService;
    
    /**
     * Get the status of the MCP client connection.
     * 
     * @return Response with connection status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> response = new HashMap<>();
        boolean connected = mcpClientService.isConnected();
        
        response.put("connected", connected);
        
        if (connected && mcpClientService.getAvailableTools() != null) {
            response.put("availableTools", mcpClientService.getAvailableTools());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Call a tool on the MCP server.
     * 
     * @param toolName Name of the tool to call
     * @param request Request body with parameters for the tool
     * @return Result of the tool invocation
     */
    @PostMapping("/tools/{toolName}")
    public ResponseEntity<Object> callTool(
            @PathVariable String toolName,
            @RequestBody Map<String, Object> request) {
        
        logger.info("Calling MCP tool: {} with input: {}", toolName, request);
        
        try {
            Object result = mcpClientService.callTool(toolName, request);
            return ResponseEntity.ok(result);
        } 
        catch (Exception e) {
            logger.error("Error calling MCP tool: {}", toolName, e);
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage(),
                "tool", toolName
            ));
        }
    }
} 