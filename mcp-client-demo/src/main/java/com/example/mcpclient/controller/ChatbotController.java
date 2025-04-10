package com.example.mcpclient.controller;

import com.example.mcpclient.service.McpClientService;
import com.example.mcpclient.tools.ToolResolverService;
import com.example.mcpclient.util.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final Map<String, ToolCallback> allTools = new HashMap<>();
    private final Map<String, ToolCallbackProvider> toolProviderMap = new HashMap<>();
    private final JwtTokenUtils jwtTokenUtils;
    private final ToolResolverService toolResolverService;

    @Value("${spring.ai.anthropic.model:claude-3-7-sonnet-20250219}")
    private String modelName;

    @Value("${app.tools.autoResolve:true}")
    private boolean autoResolveTools;

    /**
     * Constructor that sets up the ChatClient with discovered tools.
     */
    @SuppressWarnings("unchecked")
    public ChatbotController(ChatClient.Builder chatClientBuilder,
                             @Autowired(required = false) @Qualifier("mcpToolCallbackProvider") ToolCallbackProvider toolCallbackProvider,
                             McpClientService mcpClientService,
                             List<ToolCallbackProvider> toolCallbackProviders,
                             @Autowired(required = false) @Qualifier("toolManagementProvider") ToolCallbackProvider toolManagementProvider,
                             @Autowired(required = false) @Qualifier("dateTimeToolProvider") ToolCallbackProvider dateTimeToolProvider,
                             JwtTokenUtils jwtTokenUtils,
                             ToolResolverService toolResolverService) {

        this.toolCallbackProviders = toolCallbackProviders;
        this.mcpClientService = mcpClientService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.toolResolverService = toolResolverService;

        // Index all tools by name for easier selection
        if (toolCallbackProviders != null) {
            for (ToolCallbackProvider provider : toolCallbackProviders) {
                try {
                    for (Object callbackObj : provider.getToolCallbacks()) {
                        if (callbackObj instanceof ToolCallback) {
                            ToolCallback callback = (ToolCallback) callbackObj;
                            allTools.put(callback.getName(), callback);
                            toolProviderMap.put(callback.getName(), provider);
                        }
                    }
                } catch (Exception e) {
                    logger.warn("Error processing tools from provider {}: {}",
                            provider.getClass().getName(), e.getMessage());
                }
            }
            logger.info("Indexed {} tools from {} providers", allTools.size(), toolCallbackProviders.size());
        }

        // Build the ChatClient with always-available tools
        ChatClient.Builder builder = chatClientBuilder;

        // Add tool management tools (always in scope)
        if (toolManagementProvider != null) {
            logger.info("Adding tool management provider to ChatClient");
            builder = builder.defaultTools(toolManagementProvider);
        }

        // Add datetime tools (always in scope)
        if (dateTimeToolProvider != null) {
            logger.info("Adding datetime tool provider to ChatClient");
            builder = builder.defaultTools(dateTimeToolProvider);
        }

        this.chatClient = builder.build();

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
        status.put("totalTools", allTools.size());
        status.put("autoResolveTools", autoResolveTools);

        logger.debug("MCP client status: {}", status);

        return ResponseEntity.ok(status);
    }

    /**
     * Start a new conversation with the AI model.
     */
    @PostMapping("/startConversation")
    public ResponseEntity<Object> startConversation(@RequestBody Map<String, Object> request, HttpServletRequest httpRequest) {
        try {
            logger.info("Starting a new conversation with AI model");

            // Extract system message
            String systemPromptText = request.containsKey("system")
                    ? request.get("system").toString()
                    : "You are a helpful technical assistant with access to tools for data analysis and processing.";

            // Extract user messages
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> messages = (List<Map<String, Object>>) request.get("messages");

            // Get the latest user message for tool resolution
            String latestUserMessage = "";
            if (messages != null && !messages.isEmpty()) {
                Map<String, Object> lastMessage = messages.get(messages.size() - 1);
                if ("user".equals(lastMessage.get("role"))) {
                    latestUserMessage = extractContent(lastMessage);
                }
            }

            // Automatically resolve tools if needed
            if (autoResolveTools && !latestUserMessage.isEmpty()) {
                // Create a temporary session with just tool management tools
                logger.info("Auto-resolving tools for query: {}", latestUserMessage);

                // Clear previously selected tools to avoid accumulation when topic changes
                if (httpRequest.getSession() != null) {
                    httpRequest.getSession().removeAttribute("selectedTools");
                    logger.debug("Cleared previously selected tools from session");
                }

                // Use the tool resolver service to select appropriate tools
                // This will internally call the listAvailableTools and selectTools methods
                // Pass the httpRequest to allow the tools to access it
                toolResolverService.resolveToolsForQuery(latestUserMessage, httpRequest);

                // The tools should now be selected in the session
                logger.info("Tool resolution completed");
            }

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

            // Get selected tools from both session and JWT token
            Set<String> selectedToolNames = getSelectedToolsFromRequest(httpRequest);

            // Create chat client response spec with selected tools and context
            ChatClient.CallResponseSpec responseSpec;
            if (!selectedToolNames.isEmpty()) {
                // Add selected tools to the chat client
                List<ToolCallback> selectedTools = selectedToolNames.stream()
                        .filter(allTools::containsKey)
                        .map(allTools::get)
                        .collect(Collectors.toList());

                logger.info("Adding {} selected tools to chat response", selectedTools.size());
                responseSpec = chatClient.prompt(prompt)
                        .toolContext(Map.of("httpRequest", httpRequest))
                        .tools(selectedTools.toArray(new ToolCallback[0]))
                        .call();
            } else {
                // No specific tools selected, just use the default ones
                responseSpec = chatClient.prompt(prompt)
                        .toolContext(Map.of("httpRequest", httpRequest))
                        .call();
            }

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
     * Continue a conversation with the AI model.
     */
    @PostMapping("/continueConversation")
    public ResponseEntity<Object> continueConversation(@RequestBody Map<String, Object> request, HttpServletRequest httpRequest) {
        try {
            logger.info("Continuing conversation with AI model");

            // This is now the same as starting a conversation since Spring AI
            // handles the conversation state internally
            return startConversation(request, httpRequest);
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

    /**
     * Get selected tools from JWT token or session.
     */
    @SuppressWarnings("unchecked")
    private Set<String> getSelectedToolsFromRequest(HttpServletRequest request) {
        Set<String> selectedTools = new HashSet<>();

        if (request != null) {
            // First try to get from session
            if (request.getSession() != null) {
                Object toolsAttribute = request.getSession().getAttribute("selectedTools");
                if (toolsAttribute instanceof List) {
                    List<String> toolsList = (List<String>) toolsAttribute;
                    selectedTools.addAll(toolsList);
                    logger.debug("Retrieved {} tools from session", selectedTools.size());
                }
            }

            // Then try to get from JWT token if available
            String token = jwtTokenUtils.extractToken(request);
            if (token != null) {
                Set<String> tokenTools = jwtTokenUtils.getSelectedToolsFromToken(token);
                selectedTools.addAll(tokenTools);
                logger.debug("Retrieved {} tools from JWT token", tokenTools.size());
            }
        }

        return selectedTools;
    }
} 