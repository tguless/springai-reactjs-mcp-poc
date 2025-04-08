package com.example.duffel.config;

import com.example.duffel.model.payments.ThreeDSSessionCreateRequest;
import com.example.duffel.model.payments.ThreeDSSessionResponse;
import com.example.duffel.service.ThreeDSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class that registers 3DS operation tools with the MCP server.
 */
@Configuration
public class ThreeDSMcpConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ThreeDSMcpConfiguration.class);
    private final ThreeDSService threeDSService;

    public ThreeDSMcpConfiguration(ThreeDSService threeDSService) {
        this.threeDSService = threeDSService;
        logger.info("ThreeDSMcpConfiguration initialized");
    }

    /**
     * Registers the 3DS operation tools as a tool callback provider.
     * This provider is used by the MCP server to expose tools to clients.
     */
    @Bean
    public ToolCallbackProvider threeDSTools() {
        logger.info("Registering 3DS operation tools with MCP server");
        return MethodToolCallbackProvider.builder()
                .toolObjects(new ThreeDSTools(threeDSService))
                .build();
    }

    /**
     * Inner class that holds the tool methods exposed by the MCP server.
     */
    public static class ThreeDSTools {

        private final ThreeDSService service;

        public ThreeDSTools(ThreeDSService service) {
            this.service = service;
            logger.info("ThreeDSTools initialized");
        }

        @Tool(description = "Create a 3D Secure session for authenticating a card payment")
        public ThreeDSSessionResponse.ThreeDSSession createThreeDSSession(
                @ToolParam(description = "The ID of the card to authenticate") String cardId,
                @ToolParam(description = "The ID of the resource (offer/order) being paid for") String resourceId,
                @ToolParam(description = "Optional exception type (e.g. 'secure_corporate_payment')") String exception,
                @ToolParam(description = "Optional list of services being paid for") List<ThreeDSSessionCreateRequest.Service> services) {
            
            return service.createThreeDSSession(cardId, resourceId, exception, services);
        }

        @Tool(description = "Create a 3D Secure session for a secure corporate payment (bypasses 3DS challenge)")
        public ThreeDSSessionResponse.ThreeDSSession createSecureCorporatePaymentSession(
                @ToolParam(description = "The ID of the card to authenticate") String cardId,
                @ToolParam(description = "The ID of the resource (offer/order) being paid for") String resourceId,
                @ToolParam(description = "Optional list of services being paid for") List<ThreeDSSessionCreateRequest.Service> services) {
            
            return service.createSecureCorporatePaymentSession(cardId, resourceId, services);
        }
    }
} 