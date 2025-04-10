package com.example.mcpclient.tools;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Tool management implementation providing tools to discover and select other tools.
 */
@Configuration
public class ToolManagementTools {

    private static final Logger logger = LoggerFactory.getLogger(ToolManagementTools.class);
    private final List<ToolCallbackProvider> toolCallbackProviders;
    private final Map<String, ToolCallback> allToolsMap = new HashMap<>();
    private final Map<String, ToolCategory> categoriesMap = new HashMap<>();

    public ToolManagementTools(List<ToolCallbackProvider> toolCallbackProviders) {
        this.toolCallbackProviders = toolCallbackProviders;
        
        // Organize tools by category (using provider class name as default category)
        for (ToolCallbackProvider provider : toolCallbackProviders) {
            String providerName = getProviderName(provider);
            
            // Skip the tool management provider to avoid circular references
            if (providerName.contains("ToolManagement") || 
                providerName.contains("DateTime")) {
                continue;
            }
            
            try {
                for (Object callbackObj : provider.getToolCallbacks()) {
                    if (callbackObj instanceof ToolCallback) {
                        ToolCallback tool = (ToolCallback) callbackObj;
                        allToolsMap.put(tool.getName(), tool);
                        
                        // Add to category
                        String categoryName = getCategoryFromProvider(provider);
                        ToolCategory category = categoriesMap.computeIfAbsent(categoryName, 
                            k -> new ToolCategory(categoryName, new ArrayList<>()));
                        
                        // Create simplified tool info without parameter details
                        category.getTools().add(new ToolInfo(
                            tool.getName(), 
                            tool.getDescription()
                        ));
                    }
                }
            } catch (Exception e) {
                logger.warn("Error processing tools from provider {}: {}", 
                        provider.getClass().getName(), e.getMessage());
            }
        }
        
        logger.info("Tool management initialized with {} tools in {} categories", 
                allToolsMap.size(), categoriesMap.size());
    }
    
    private String getProviderName(ToolCallbackProvider provider) {
        return provider.getClass().getSimpleName();
    }
    
    private String getCategoryFromProvider(ToolCallbackProvider provider) {
        String providerName = getProviderName(provider);
        
        // Extract category from provider name (e.g., WeatherToolProvider -> Weather)
        if (providerName.endsWith("Provider")) {
            providerName = providerName.substring(0, providerName.length() - "Provider".length());
        }
        if (providerName.endsWith("Tools")) {
            providerName = providerName.substring(0, providerName.length() - "Tools".length());
        }
        if (providerName.endsWith("ToolCallback")) {
            providerName = providerName.substring(0, providerName.length() - "ToolCallback".length());
        }
        
        return providerName;
    }
    
    @Bean(name = "toolManagementProviderBean")
    @Qualifier("toolManagementProvider")
    public ToolCallbackProvider toolManagementTools() {
        logger.info("Registering tool management tools");
        return MethodToolCallbackProvider.builder()
                .toolObjects(new ToolManagement())
                .build();
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToolCategory {
        private String name;
        private List<ToolInfo> tools;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToolInfo {
        private String name;
        private String description;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToolSelectionResult {
        private List<String> selectedTools;
        private String message;
    }
    
    public class ToolManagement {
        
        @Tool(description = "List all available tool categories and their tools. Use this to discover what capabilities are available.")
        public List<ToolCategory> listAvailableTools() {
            logger.info("Tool resolver called, returning {} categories", categoriesMap.size());
            return new ArrayList<>(categoriesMap.values());
        }
        
        @Tool(description = "Select specific tools to be used in the current session. Choose tools based on the user's question or intent. For weather questions, select weather tools. For date/time questions, select datetime tools.")
        public ToolSelectionResult selectTools(
            @ToolParam(description = "List of tool names to select for the current session") List<String> toolNames,
            ToolContext context) {
            
            HttpServletRequest request = (HttpServletRequest) context.getContext().get("httpRequest");
            if (request == null) {
                // Fallback if httpRequest not available in the context
                ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attrs != null) {
                    request = attrs.getRequest();
                }
            }
            
            if (request == null) {
                return new ToolSelectionResult(
                    Collections.emptyList(),
                    "Error: Could not access request context to store selected tools"
                );
            }
            
            // Filter out invalid tool names
            List<String> validTools = toolNames.stream()
                .filter(allToolsMap::containsKey)
                .collect(Collectors.toList());
            
            // Store in session for retrieval later
            request.getSession().setAttribute("selectedTools", validTools);
            
            logger.info("Tool selector called, selected {} tools", validTools.size());
            return new ToolSelectionResult(
                validTools,
                String.format("Successfully selected %d tools for use", validTools.size())
            );
        }
        
        @Tool(description = "Clear all previously selected tools from the current session.")
        public ToolSelectionResult clearSelectedTools(ToolContext context) {
            HttpServletRequest request = (HttpServletRequest) context.getContext().get("httpRequest");
            if (request == null) {
                // Fallback if httpRequest not available in the context
                ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attrs != null) {
                    request = attrs.getRequest();
                }
            }
            
            if (request == null) {
                return new ToolSelectionResult(
                    Collections.emptyList(),
                    "Error: Could not access request context to clear selected tools"
                );
            }
            
            // Clear the selected tools
            request.getSession().removeAttribute("selectedTools");
            
            logger.info("Cleared all selected tools from session");
            return new ToolSelectionResult(
                Collections.emptyList(),
                "Successfully cleared all selected tools"
            );
        }
    }
} 