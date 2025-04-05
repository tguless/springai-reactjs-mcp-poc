package com.example.mcpserver.config;

import com.example.mcpserver.model.payments.CardCreateRequest;
import com.example.mcpserver.model.payments.CardResponse;
import com.example.mcpserver.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that registers card operation tools with the MCP server.
 */
@Configuration
public class CardMcpConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CardMcpConfiguration.class);
    private final CardService cardService;

    public CardMcpConfiguration(CardService cardService) {
        this.cardService = cardService;
        logger.info("CardMcpConfiguration initialized");
    }

    /**
     * Registers the card operation tools as a tool callback provider.
     * This provider is used by the MCP server to expose tools to clients.
     */
    @Bean
    public ToolCallbackProvider cardTools() {
        logger.info("Registering card operation tools with MCP server");
        return MethodToolCallbackProvider.builder()
                .toolObjects(new CardTools(cardService))
                .build();
    }

    /**
     * Inner class that holds the tool methods exposed by the MCP server.
     */
    public static class CardTools {

        private final CardService service;

        public CardTools(CardService service) {
            this.service = service;
            logger.info("CardTools initialized");
        }

        @Tool(description = "Create a new payment card that can be used to pay for bookings")
        public CardResponse.Card createCard(
                @ToolParam(description = "The card number") String number,
                @ToolParam(description = "The cardholder's name as it appears on the card") String name,
                @ToolParam(description = "The card expiry month as 2 digits (e.g. 02 for February)") String expiryMonth,
                @ToolParam(description = "The card expiry year as 2 digits (e.g. 25 for 2025)") String expiryYear,
                @ToolParam(description = "The card verification code (CVC)") String cvc,
                @ToolParam(description = "First line of the billing address") String addressLine1,
                @ToolParam(description = "Second line of the billing address (optional)") String addressLine2,
                @ToolParam(description = "City of the billing address") String addressCity,
                @ToolParam(description = "Region/state of the billing address") String addressRegion,
                @ToolParam(description = "Postal code of the billing address") String addressPostalCode,
                @ToolParam(description = "Country code of the billing address (ISO 3166-1 alpha-2)") String addressCountryCode,
                @ToolParam(description = "Whether this card can be used multiple times (true) or for a single booking (false)") Boolean multiUse) {
            
            CardCreateRequest request = CardCreateRequest.builder()
                    .number(number)
                    .name(name)
                    .expiryMonth(expiryMonth)
                    .expiryYear(expiryYear)
                    .cvc(cvc)
                    .addressLine1(addressLine1)
                    .addressLine2(addressLine2)
                    .addressCity(addressCity)
                    .addressRegion(addressRegion)
                    .addressPostalCode(addressPostalCode)
                    .addressCountryCode(addressCountryCode)
                    .multiUse(multiUse)
                    .build();
            
            return service.createCard(request);
        }

        @Tool(description = "Create a new single-use card from an existing multi-use card")
        public CardResponse.Card createCardFromExistingCard(
                @ToolParam(description = "The ID of the multi-use card") String cardId,
                @ToolParam(description = "The card verification code (CVC)") String cvc) {
            
            return service.createCardFromExistingCard(cardId, cvc);
        }

        @Tool(description = "Delete a card by ID")
        public void deleteCard(
                @ToolParam(description = "The ID of the card to delete") String cardId) {
            
            service.deleteCard(cardId);
        }
    }
} 