package com.example.common.config;

import com.example.common.model.flights.AirlineInitiatedChangeResponse;
import com.example.common.service.AirlineInitiatedChangeService;
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
 * Configuration class that registers airline-initiated change tools with the MCP server.
 */
@Configuration
public class AirlineInitiatedChangeMcpConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(AirlineInitiatedChangeMcpConfiguration.class);
    private final AirlineInitiatedChangeService airlineInitiatedChangeService;

    public AirlineInitiatedChangeMcpConfiguration(AirlineInitiatedChangeService airlineInitiatedChangeService) {
        this.airlineInitiatedChangeService = airlineInitiatedChangeService;
        logger.info("AirlineInitiatedChangeMcpConfiguration initialized");
    }

    /**
     * Registers the airline-initiated change tools as a tool callback provider.
     * This provider is used by the MCP server to expose tools to clients.
     */
    @Bean
    public ToolCallbackProvider airlineInitiatedChangeTools() {
        logger.info("Registering airline-initiated change tools with MCP server");
        return MethodToolCallbackProvider.builder()
                .toolObjects(new AirlineInitiatedChangeTools(airlineInitiatedChangeService))
                .build();
    }

    /**
     * Inner class that holds the tool methods exposed by the MCP server.
     */
    public static class AirlineInitiatedChangeTools {

        private final AirlineInitiatedChangeService service;

        public AirlineInitiatedChangeTools(AirlineInitiatedChangeService service) {
            this.service = service;
            logger.info("AirlineInitiatedChangeTools initialized");
        }

        @Tool(description = "List all airline-initiated changes, optionally filtered by order ID")
        public List<AirlineInitiatedChangeResponse.AirlineInitiatedChange> listAirlineInitiatedChanges(
                @ToolParam(description = "The order ID to filter changes by (optional)") String orderId,
                @ToolParam(description = "Maximum number of changes to return (optional)") Integer limit,
                @ToolParam(description = "Pagination token for previous page (optional)") String before,
                @ToolParam(description = "Pagination token for next page (optional)") String after) {
            
            return service.listAirlineInitiatedChanges(orderId, limit, before, after);
        }

        @Tool(description = "Get a single airline-initiated change by ID")
        public AirlineInitiatedChangeResponse.AirlineInitiatedChange getAirlineInitiatedChange(
                @ToolParam(description = "ID of the airline-initiated change to retrieve") String airlineInitiatedChangeId) {
            
            return service.getAirlineInitiatedChange(airlineInitiatedChangeId);
        }

        @Tool(description = "Update an airline-initiated change with the action taken")
        public AirlineInitiatedChangeResponse.AirlineInitiatedChange updateAirlineInitiatedChange(
                @ToolParam(description = "ID of the airline-initiated change to update") String airlineInitiatedChangeId,
                @ToolParam(description = "The action taken (accepted, cancelled, or changed)") String actionTaken) {
            
            return service.updateAirlineInitiatedChange(airlineInitiatedChangeId, actionTaken);
        }

        @Tool(description = "Accept an airline-initiated change")
        public AirlineInitiatedChangeResponse.AirlineInitiatedChange acceptAirlineInitiatedChange(
                @ToolParam(description = "ID of the airline-initiated change to accept") String airlineInitiatedChangeId) {
            
            return service.acceptAirlineInitiatedChange(airlineInitiatedChangeId);
        }
    }
} 