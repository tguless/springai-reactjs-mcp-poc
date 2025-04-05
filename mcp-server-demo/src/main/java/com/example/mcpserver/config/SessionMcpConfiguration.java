package com.example.mcpserver.config;

import com.example.mcpserver.model.links.SessionCreateRequest;
import com.example.mcpserver.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that registers session tools with the MCP server.
 */
@Configuration
public class SessionMcpConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SessionMcpConfiguration.class);
    private final SessionService sessionService;

    public SessionMcpConfiguration(SessionService sessionService) {
        this.sessionService = sessionService;
        logger.info("SessionMcpConfiguration initialized");
    }

    /**
     * Registers the session tools as a tool callback provider.
     * This provider is used by the MCP server to expose tools to clients.
     */
    @Bean
    public ToolCallbackProvider sessionTools() {
        logger.info("Registering session tools with MCP server");
        return MethodToolCallbackProvider.builder()
                .toolObjects(new SessionTools(sessionService))
                .build();
    }

    /**
     * Inner class that holds the tool methods exposed by the MCP server.
     */
    public static class SessionTools {

        private final SessionService service;

        public SessionTools(SessionService service) {
            this.service = service;
            logger.info("SessionTools initialized");
        }

        @Tool(description = "Create a fully customized session for the search and book flow")
        public String createCustomSession(
                @ToolParam(description = "A reference to identify the session (e.g. user ID)") String reference,
                @ToolParam(description = "The URL to redirect to on successful booking") String successUrl,
                @ToolParam(description = "The URL to redirect to on failure") String failureUrl,
                @ToolParam(description = "The URL to redirect to if traveler abandons the session") String abandonmentUrl,
                @ToolParam(description = "URL to your logo (optional)") String logoUrl,
                @ToolParam(description = "Primary color in hexadecimal (e.g. #FF0000) (optional)") String primaryColor,
                @ToolParam(description = "Secondary color in hexadecimal (e.g. #00FF00) (optional)") String secondaryColor,
                @ToolParam(description = "Text to display at checkout (optional)") String checkoutDisplayText,
                @ToolParam(description = "Currency for traveler prices (optional)") String travellerCurrency,
                @ToolParam(description = "Whether to hide currency selector (optional)") Boolean shouldHideTravellerCurrencySelector,
                @ToolParam(description = "Absolute markup amount (optional)") String markupAmount,
                @ToolParam(description = "Currency for markup (optional)") String markupCurrency,
                @ToolParam(description = "Markup rate (e.g. 0.01 for 1%) (optional)") String markupRate,
                @ToolParam(description = "Enable flights search (true/false)") String enableFlights,
                @ToolParam(description = "Enable stays search (true/false)") String enableStays) {
            
            SessionCreateRequest.FlightsConfig flightsConfig = null;
            if (enableFlights != null) {
                flightsConfig = SessionCreateRequest.FlightsConfig.builder()
                        .enabled(enableFlights)
                        .build();
            }
            
            SessionCreateRequest.StaysConfig staysConfig = null;
            if (enableStays != null) {
                staysConfig = SessionCreateRequest.StaysConfig.builder()
                        .enabled(enableStays)
                        .build();
            }
            
            SessionCreateRequest request = SessionCreateRequest.builder()
                    .reference(reference)
                    .successUrl(successUrl)
                    .failureUrl(failureUrl)
                    .abandonmentUrl(abandonmentUrl)
                    .logoUrl(logoUrl)
                    .primaryColor(primaryColor)
                    .secondaryColor(secondaryColor)
                    .checkoutDisplayText(checkoutDisplayText)
                    .travellerCurrency(travellerCurrency)
                    .shouldHideTravellerCurrencySelector(shouldHideTravellerCurrencySelector)
                    .markupAmount(markupAmount)
                    .markupCurrency(markupCurrency)
                    .markupRate(markupRate)
                    .flights(flightsConfig)
                    .stays(staysConfig)
                    .build();
            
            return service.createSession(request);
        }

        @Tool(description = "Create a flights-only session for the search and book flow")
        public String createFlightsSession(
                @ToolParam(description = "A reference to identify the session (e.g. user ID)") String reference,
                @ToolParam(description = "The URL to redirect to on successful booking") String successUrl,
                @ToolParam(description = "The URL to redirect to on failure") String failureUrl,
                @ToolParam(description = "The URL to redirect to if traveler abandons the session") String abandonmentUrl) {
            
            return service.createFlightsSession(reference, successUrl, failureUrl, abandonmentUrl);
        }

        @Tool(description = "Create a combined flights and stays session for the search and book flow")
        public String createFlightsAndStaysSession(
                @ToolParam(description = "A reference to identify the session (e.g. user ID)") String reference,
                @ToolParam(description = "The URL to redirect to on successful booking") String successUrl,
                @ToolParam(description = "The URL to redirect to on failure") String failureUrl,
                @ToolParam(description = "The URL to redirect to if traveler abandons the session") String abandonmentUrl) {
            
            return service.createFlightsAndStaysSession(reference, successUrl, failureUrl, abandonmentUrl);
        }

        @Tool(description = "Create a custom-branded session for the search and book flow")
        public String createBrandedSession(
                @ToolParam(description = "A reference to identify the session (e.g. user ID)") String reference,
                @ToolParam(description = "The URL to redirect to on successful booking") String successUrl,
                @ToolParam(description = "The URL to redirect to on failure") String failureUrl,
                @ToolParam(description = "The URL to redirect to if traveler abandons the session") String abandonmentUrl,
                @ToolParam(description = "URL to your logo") String logoUrl,
                @ToolParam(description = "Primary color in hexadecimal (e.g. #FF0000)") String primaryColor,
                @ToolParam(description = "Secondary color in hexadecimal (e.g. #00FF00)") String secondaryColor,
                @ToolParam(description = "Text to display at checkout") String checkoutDisplayText) {
            
            return service.createBrandedSession(
                    reference, successUrl, failureUrl, abandonmentUrl, 
                    logoUrl, primaryColor, secondaryColor, checkoutDisplayText);
        }
    }
} 