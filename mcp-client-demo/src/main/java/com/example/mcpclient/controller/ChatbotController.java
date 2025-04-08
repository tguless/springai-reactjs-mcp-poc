package com.example.mcpclient.controller;

import com.example.common.config.WeatherMcpConfiguration;
import com.example.mcpclient.service.McpClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * REST controller for handling chatbot interactions with MCP tools.
 */
@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*")
public class ChatbotController {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatbotController.class);
    
    private final ChatClient chatClient;
    private final McpClientService mcpClientService;
    private final List<ToolCallbackProvider> toolCallbackProviders;

    @Value("${spring.ai.anthropic.model:claude-3-7-sonnet-20250219}")
    private String modelName;
    
    /**
     * Constructor that sets up the ChatClient with discovered tools.
     */
    public ChatbotController(ChatClient.Builder chatClientBuilder, 
                          @Autowired(required = false) @Qualifier("mcpToolCallbackProvider") ToolCallbackProvider toolCallbackProvider,
                          McpClientService mcpClientService,
                             List<ToolCallbackProvider> toolCallbackProviders
                             ) {
        
        // Build the ChatClient with tools if available
        ChatClient.Builder builder = chatClientBuilder;


        /*
        if (toolCallbackProvider != null) {
            logger.info("Adding MCP tool callback provider to ChatClient");
            builder = builder.defaultTools(toolCallbackProvider);
        } else {
            logger.warn("No MCP tool callback provider available, ChatClient will not have tool support");
        }*/

        if (toolCallbackProviders != null) {
            logger.info("Adding MCP tool callback provider to ChatClient");
            builder = builder.defaultTools(toolCallbackProviders.toArray(new ToolCallbackProvider[0]));
        } else {
            logger.warn("No MCP tool callback provider available, ChatClient will not have tool support");
        }

        this.toolCallbackProviders = toolCallbackProviders;

        this.chatClient = builder.build();
        this.mcpClientService = mcpClientService;
        
        logger.info("ChatbotController initialized with Spring AI ChatClient");
    }
    
    /**
     * Get the status of the MCP client connection.
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("connected", mcpClientService.isConnected());
        status.put("availableTools", mcpClientService.getAvailableTools());
        
        logger.debug("MCP client status: {}", status);
        
        return ResponseEntity.ok(status);
    }
    
    /**
     * Start a new conversation with the AI model.
     */
    @PostMapping("/startConversation")
    public ResponseEntity<Object> startConversation(@RequestBody Map<String, Object> request) {
        try {
            logger.info("Starting a new conversation with AI model");
            
            // Extract system message
            String systemPromptText = request.containsKey("system") 
                ? request.get("system").toString() 
                : "You are a helpful technical assistant with access to tools for data analysis and processing.";
            
            // Extract user messages
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> messages = (List<Map<String, Object>>) request.get("messages");
            
            // Convert messages to Spring AI format
            List<Message> aiMessages = new ArrayList<>();
            
            // Add system message first
            aiMessages.add(new SystemMessage(systemPromptText));
            
            // Add all user and assistant messages
            for (Map<String, Object> message : messages) {
                String role = (String) message.get("role");
                String content = extractContent(message);
                
                if ("user".equals(role)) {
                    aiMessages.add(new UserMessage(content));
                } else if ("assistant".equals(role)) {
                    aiMessages.add(new AssistantMessage(content));
                }
            }
            
            // Create prompt and get response
            Prompt prompt = new Prompt(aiMessages);
            // Use the same pattern as the original implementation
            ChatClient.CallResponseSpec responseSpec = chatClient.prompt(prompt).toolContext(Map.of("test", "token")).call();
            String responseText = responseSpec.content();

            // Format response for the frontend
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("id", "claude-response-" + System.currentTimeMillis());
            responseMap.put("model", modelName);
            responseMap.put("content", List.of(Map.of("type", "text", "text", responseText)));
            responseMap.put("type", "message");
            responseMap.put("role", "assistant");
            
            return ResponseEntity.ok(responseMap);
        } 
        catch (Exception e) {
            logger.error("Error in conversation flow: " + e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to process conversation: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /*
    public ArrayList<ToolCallback> providerToolCallbacks(List<ToolCallbackProvider> toolCallbackProviders) {
        ArrayList<ToolCallback> toolCallbacks = new ArrayList<>();

        for (ToolCallbackProvider toolCallbackProvider : toolCallbackProviders) {

            toolCallbacks.addAll((Collection<? extends ToolCallback>) Arrays.asList(toolCallbackProvider.getToolCallbacks()));
        }
        return toolCallbacks;

    }

     /*
     * Continue a conversation with the AI model.
     */
    @PostMapping("/continueConversation")
    public ResponseEntity<Object> continueConversation(@RequestBody Map<String, Object> request) {
        try {
            logger.info("Continuing conversation with AI model");
            
            // This is now the same as starting a conversation since Spring AI
            // handles the conversation state internally
            return startConversation(request);
        } 
        catch (Exception e) {
            logger.error("Error calling AI model: " + e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to communicate with AI model: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * Extract content from a message object which could be a string or a complex object.
     */
    private String extractContent(Map<String, Object> message) {
        Object content = message.get("content");
        
        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> contentList = (List<Object>) content;
            
            // Process the content objects to extract text
            StringBuilder sb = new StringBuilder();
            
            for (Object item : contentList) {
                if (item instanceof String) {
                    sb.append(item).append(" ");
                } else if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> contentItem = (Map<String, Object>) item;
                    
                    if (contentItem.containsKey("text")) {
                        sb.append(contentItem.get("text")).append(" ");
                    } else if (contentItem.containsKey("type") && "text".equals(contentItem.get("type"))) {
                        sb.append(contentItem.get("text")).append(" ");
                    }
                }
            }
            
            return sb.toString().trim();
        }
        
        return "";
    }
} 