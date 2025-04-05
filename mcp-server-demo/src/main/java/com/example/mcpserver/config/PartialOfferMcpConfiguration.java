package com.example.mcpserver.config;

import com.example.mcpserver.model.flights.PartialOfferRequestCreate;
import com.example.mcpserver.model.flights.PartialOfferRequestResponse;
import com.example.mcpserver.model.flights.PartialOfferFaresResponse;
import com.example.mcpserver.service.PartialOfferService;
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
 * Configuration class that registers partial offer tools with the MCP server.
 */
@Configuration
public class PartialOfferMcpConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(PartialOfferMcpConfiguration.class);
    private final PartialOfferService partialOfferService;

    public PartialOfferMcpConfiguration(PartialOfferService partialOfferService) {
        this.partialOfferService = partialOfferService;
        logger.info("PartialOfferMcpConfiguration initialized");
    }

    /**
     * Registers the partial offer tools as a tool callback provider.
     * This provider is used by the MCP server to expose tools to clients.
     */
    @Bean
    public ToolCallbackProvider partialOfferTools() {
        logger.info("Registering partial offer tools with MCP server");
        return MethodToolCallbackProvider.builder()
                .toolObjects(new PartialOfferTools(partialOfferService))
                .build();
    }

    /**
     * Inner class that holds the tool methods exposed by the MCP server.
     */
    public static class PartialOfferTools {

        private final PartialOfferService service;

        public PartialOfferTools(PartialOfferService service) {
            this.service = service;
            logger.info("PartialOfferTools initialized");
        }

        @Tool(description = "Create a partial offer request to search for flights for each slice separately")
        public PartialOfferRequestResponse.PartialOfferRequest createPartialOfferRequest(
                @ToolParam(description = "The cabin class for the flights (economy, premium_economy, business, first)") String cabinClass,
                @ToolParam(description = "List of passenger information") List<PartialOfferRequestCreate.Passenger> passengers,
                @ToolParam(description = "List of flight slices (origin, destination, departure date)") List<PartialOfferRequestCreate.SliceInput> slices) {
            
            PartialOfferRequestCreate request = PartialOfferRequestCreate.builder()
                    .cabinClass(cabinClass)
                    .passengers(passengers)
                    .slices(slices)
                    .build();
            
            return service.createPartialOfferRequest(request);
        }

        @Tool(description = "Retrieve a partial offer request by ID")
        public PartialOfferRequestResponse.PartialOfferRequest getPartialOfferRequest(
                @ToolParam(description = "The ID of the partial offer request to retrieve") String partialOfferRequestId) {
            
            return service.getPartialOfferRequest(partialOfferRequestId);
        }

        @Tool(description = "Get full offer fares for selected partial offers")
        public PartialOfferFaresResponse.FareData getPartialOfferFares(
                @ToolParam(description = "The ID of the partial offer request") String partialOfferRequestId,
                @ToolParam(description = "List of selected partial offer IDs") List<String> selectedPartialOffers) {
            
            return service.getPartialOfferFares(partialOfferRequestId, selectedPartialOffers);
        }
    }
} 