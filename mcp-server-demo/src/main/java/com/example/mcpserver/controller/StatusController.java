package com.example.mcpserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/status")
@CrossOrigin(origins = "*")
public class StatusController {

    private static final Logger logger = LoggerFactory.getLogger(StatusController.class);
    
    @Autowired
    @Qualifier("weatherTools")
    private ToolCallbackProvider toolCallbackProvider;
    
    @Value("${spring.ai.mcp.server.name:sse-mcp-server}")
    private String serverName;
    
    @Value("${spring.ai.mcp.server.type:SSE}")
    private String serverType;
    
    @GetMapping
    public Map<String, Object> getStatus() {
        logger.info("Status endpoint called");
        
        Map<String, Object> status = new HashMap<>();
        status.put("server", serverName);
        status.put("status", "running");
        status.put("mode", serverType);
        status.put("toolsAvailable", toolCallbackProvider.getToolCallbacks().length);
        
        // Add list of available tools
        status.put("tools", Arrays.stream(toolCallbackProvider.getToolCallbacks())
                .map(tool -> Map.of(
                    "name", tool.getName(),
                    "description", tool.getDescription()
                ))
                .toList());
        
        return status;
    }
} 