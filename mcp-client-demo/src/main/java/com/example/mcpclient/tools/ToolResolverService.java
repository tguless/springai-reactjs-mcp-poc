package com.example.mcpclient.tools;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * A service that uses the LLM to intelligently determine which tools should be 
 * selected based on the user's query.
 */
@Service
public class ToolResolverService {
    
    private static final Logger logger = LoggerFactory.getLogger(ToolResolverService.class);
    
    private final ChatClient chatClient;
    private final Map<String, List<String>> toolCategoryMap = new HashMap<>();
    
    public ToolResolverService(ChatClient.Builder chatClientBuilder,
                             @Autowired(required = false) @Qualifier("toolManagementProvider") ToolCallbackProvider toolManagementProvider) {
        
        // Create a dedicated ChatClient for tool selection that only has access to the tool management tools
        ChatClient.Builder builder = chatClientBuilder;
        
        if (toolManagementProvider != null) {
            builder = builder.defaultTools(toolManagementProvider);
        }
        
        this.chatClient = builder.build();
        
        // Initialize our tool category map with common categories and queries
        // This helps seed the LLM with examples of which tools to select for different types of queries
        toolCategoryMap.put("weather", Arrays.asList(
            "What's the weather like?", 
            "Is it raining in New York?",
            "What's the temperature in Miami?",
            "Will it snow tomorrow?"
        ));
        
        toolCategoryMap.put("datetime", Arrays.asList(
            "What time is it?",
            "How many days until Christmas?",
            "What's the date next Tuesday?",
            "How long between these two dates?"
        ));
    }
    
    /**
     * Analyze a user query and determine which tools should be selected.
     * 
     * @param userQuery The user's question or request
     * @param httpRequest The HTTP request to pass to tool calls
     * @return A list of tool names that should be selected
     */
    public List<String> resolveToolsForQuery(String userQuery, HttpServletRequest httpRequest) {
        try {
            logger.debug("Resolving tools for query: {}", userQuery);
            
            // Simple keyword check for fallback selection
            Set<String> fallbackTools = detectKeywordTools(userQuery);
            
            // Create a system prompt that instructs the model to select appropriate tools
            String systemPrompt = 
                "You are a query analyzer that determines which tools a user needs to answer their question. " +
                "Your job is to analyze the user's query and select appropriate tools from the available tools. " +
                "IMPORTANT: Do not try to answer the user's question or engage in conversation - your only job is to " +
                "select the appropriate tools that would be needed to answer it.\n\n" +
                
                "You will see a simplified list of tools organized by category. Each tool has only a name and description. " +
                "You do not need parameter details for tool selection - those will be provided later when the tool is used.\n\n" +
                
                "Follow these steps exactly:\n" +
                "1. First, use the listAvailableTools tool to see what tools are available\n" +
                "2. Analyze the user's query carefully:\n" +
                "   - If the query is about weather, look for tools like 'getWeatherData', 'getGeocodingByCityName', 'getWeatherForCity'\n" +
                "   - If the query is about time or dates, look for tools like 'getCurrentDateTime', 'calculateTimeBetween'\n" +
                "3. Use the selectTools tool ONCE to select ONLY the tools necessary to answer the query\n" +
                "4. Select as few tools as possible to answer the query effectively\n" +
                "5. DO NOT call any other tools besides listAvailableTools and selectTools\n" +
                "6. After calling selectTools, STOP. Do not try to perform the final analysis.\n\n" +
                
                "Examples:\n" +
                "- For 'What's the weather in New York?', select 'getWeatherForCity' or both 'getGeocodingByCityName' and 'getWeatherData'\n" +
                "- For 'What time is it?', select 'getCurrentDateTime'\n" +
                "- For 'How many days until Christmas?', select 'getCurrentDateTime' and 'calculateTimeBetween'\n\n" +
                
                "WEATHER QUERIES: For any query containing words like 'weather', 'temperature', 'forecast', you MUST select weather tools!\n\n" +
                
                "VERY IMPORTANT: After you call selectTools, your job is DONE. DO NOT make any additional tool calls!\n\n" +
                
                "DO NOT try to answer the question, just select the appropriate tools.";
            
            List<Message> messages = new ArrayList<>();
            messages.add(new SystemMessage(systemPrompt));
            messages.add(new UserMessage(userQuery));
            
            Prompt prompt = new Prompt(messages);
            
            // Execute the query with the LLM, passing the httpRequest in the tool context
            String responseText = chatClient.prompt(prompt)
                .toolContext(Map.of("httpRequest", httpRequest))
                .call()
                .content();
            
            // Check if any tools were selected in the session
            @SuppressWarnings("unchecked")
            List<String> selectedTools = (List<String>) httpRequest.getSession().getAttribute("selectedTools");
            
            // Apply fallback if needed - store fallback tools directly in the session
            if (selectedTools == null || selectedTools.isEmpty()) {
                if (!fallbackTools.isEmpty()) {
                    logger.info("No tools selected by LLM, applying fallback selection: {}", fallbackTools);
                    
                    // Store the fallback tools directly in the session
                    List<String> fallbackToolsList = new ArrayList<>(fallbackTools);
                    httpRequest.getSession().setAttribute("selectedTools", fallbackToolsList);
                    
                    logger.info("Applied fallback tool selection: {}", fallbackToolsList);
                }
            }
            
            return Collections.emptyList(); // The actual tools are selected during the execution
        } catch (Exception e) {
            logger.error("Error resolving tools for query: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Detect tools to select based on keywords in the user query.
     * This is a backup method in case the LLM doesn't correctly identify tools.
     */
    private Set<String> detectKeywordTools(String userQuery) {
        Set<String> toolsToSelect = new HashSet<>();
        String query = userQuery.toLowerCase();
        
        // Weather-related keywords
        if (query.contains("weather") || query.contains("temperature") || 
            query.contains("forecast") || query.contains("rain") || 
            query.contains("snow") || query.contains("humid") || 
            query.contains("degrees") || query.contains("hot") || 
            query.contains("cold") || query.contains("climate")) {
            
            toolsToSelect.add("getWeatherForCity");
            // If we have geo-specific tools
            toolsToSelect.add("getGeocodingByCityName");
            toolsToSelect.add("getWeatherData");
        }
        
        // Date/time related keywords
        if (query.contains("time") || query.contains("date") || 
            query.contains("when") || query.contains("day") || 
            query.contains("month") || query.contains("year") || 
            query.contains("hour") || query.contains("minute") || 
            query.contains("calendar")) {
            
            toolsToSelect.add("getCurrentDateTime");
            
            if (query.contains("between") || query.contains("difference") || 
                query.contains("until") || query.contains("since")) {
                toolsToSelect.add("calculateTimeBetween");
            }
        }
        
        return toolsToSelect;
    }
    
    /**
     * Generate examples of queries that would use a specific set of tools.
     * This can be used to help the LLM understand when to use certain tools.
     * 
     * @param toolNames The names of tools to generate examples for
     * @return A list of example queries that would use these tools
     */
    public List<String> generateQueryExamplesForTools(List<String> toolNames) {
        List<String> examples = new ArrayList<>();
        
        for (Map.Entry<String, List<String>> entry : toolCategoryMap.entrySet()) {
            String category = entry.getKey();
            if (toolNames.stream().anyMatch(tool -> tool.toLowerCase().contains(category.toLowerCase()))) {
                examples.addAll(entry.getValue());
            }
        }
        
        return examples;
    }
} 