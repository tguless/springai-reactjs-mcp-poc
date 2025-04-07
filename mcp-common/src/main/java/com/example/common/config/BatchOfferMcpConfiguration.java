package com.example.common.config;

import com.example.common.model.flights.BatchOfferRequestCreate;
import com.example.common.model.flights.BatchOfferRequestResponse;
import com.example.common.service.BatchOfferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Configuration class that registers batch offer request tools with the MCP server.
 */
@Configuration
public class BatchOfferMcpConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BatchOfferMcpConfiguration.class);
    private final BatchOfferService batchOfferService;

    public BatchOfferMcpConfiguration(BatchOfferService batchOfferService) {
        this.batchOfferService = batchOfferService;
        logger.info("BatchOfferMcpConfiguration initialized");
    }

    /**
     * Registers the batch offer request tools as a tool callback provider.
     * This provider is used by the MCP server to expose tools to clients.
     */
    @Bean
    public ToolCallbackProvider batchOfferTools() {
        logger.info("Registering batch offer request tools with MCP server");
        return MethodToolCallbackProvider.builder()
                .toolObjects(new BatchOfferTools(batchOfferService))
                .build();
    }

    /**
     * Inner class that holds the tool methods exposed by the MCP server.
     */
    public static class BatchOfferTools {

        private final BatchOfferService service;

        public BatchOfferTools(BatchOfferService service) {
            this.service = service;
            logger.info("BatchOfferTools initialized");
        }

        //@Tool(description = "Create a batch offer request to search for flights with results returned incrementally")
        public BatchOfferRequestResponse.BatchOfferRequest createBatchOfferRequest(
                @ToolParam(description = "The cabin class for the flights (economy, premium_economy, business, first)") String cabinClass,
                @ToolParam(description = "Maximum number of connections (0 for direct flights only, default is 1)") Integer maxConnections,
                @ToolParam(description = "List of passenger information") List<BatchOfferRequestCreate.Passenger> passengers,
                @ToolParam(description = "List of flight slices (origin, destination, departure date/time)") List<BatchOfferRequestCreate.SliceInput> slices,
                @ToolParam(description = "Private fare codes (optional)") Map<String, List<BatchOfferRequestCreate.PrivateFare>> privateFares,
                @ToolParam(description = "Supplier timeout in milliseconds (2000-60000, default is 20000)") Integer supplierTimeout) {
            
            BatchOfferRequestCreate request = BatchOfferRequestCreate.builder()
                    .cabinClass(cabinClass)
                    .maxConnections(maxConnections)
                    .passengers(passengers)
                    .slices(slices)
                    .privateFares(privateFares)
                    .build();
            
            return service.createBatchOfferRequest(request, supplierTimeout);
        }

        @Tool(description = "Retrieve a batch of offers from a batch offer request (long-polling style API)")
        public BatchOfferRequestResponse.BatchOfferRequest getBatchOfferRequest(
                @ToolParam(description = "ID of the batch offer request to retrieve") String batchOfferRequestId) {
            
            return service.getBatchOfferRequest(batchOfferRequestId);
        }
    }
} 