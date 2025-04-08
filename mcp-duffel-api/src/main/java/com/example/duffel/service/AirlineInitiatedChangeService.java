package com.example.duffel.service;

import com.example.duffel.client.DuffelClient;
import com.example.duffel.model.flights.AirlineInitiatedChangeResponse;
import com.example.duffel.model.flights.AirlineInitiatedChangeUpdateRequest;
import com.example.duffel.model.flights.DuffelApiWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirlineInitiatedChangeService {
    
    private static final Logger logger = LoggerFactory.getLogger(AirlineInitiatedChangeService.class);
    
    private final DuffelClient duffelClient;
    private final String authToken;
    private final String duffelVersion;
    
    public AirlineInitiatedChangeService(DuffelClient duffelClient, 
                                        @Value("${duffel.auth-token}") String authToken,
                                        @Value("${duffel.version}") String duffelVersion) {
        this.duffelClient = duffelClient;
        this.authToken = "Bearer " + authToken;
        this.duffelVersion = duffelVersion;
        logger.info("AirlineInitiatedChangeService initialized with Duffel API version: {}", duffelVersion);
    }
    
    /**
     * Lists airline-initiated changes, optionally filtered by order ID
     */
    public List<AirlineInitiatedChangeResponse.AirlineInitiatedChange> listAirlineInitiatedChanges(String orderId, Integer limit, String before, String after) {
        if (orderId != null) {
            logger.info("Listing airline-initiated changes for order ID: {}", orderId);
        } else {
            logger.info("Listing all airline-initiated changes");
        }
        
        AirlineInitiatedChangeResponse response = duffelClient.listAirlineInitiatedChanges(
                authToken, 
                "application/json", 
                duffelVersion, 
                orderId, 
                limit, 
                before, 
                after
        );
        
        if (response.getData() instanceof List) {
            logger.info("Successfully retrieved {} airline-initiated changes", ((List<?>) response.getData()).size());
            return (List<AirlineInitiatedChangeResponse.AirlineInitiatedChange>) response.getData();
        } else {
            logger.info("Successfully retrieved single airline-initiated change");
            return List.of((AirlineInitiatedChangeResponse.AirlineInitiatedChange) response.getData());
        }
    }
    
    /**
     * Retrieves a single airline-initiated change by ID
     */
    public AirlineInitiatedChangeResponse.AirlineInitiatedChange getAirlineInitiatedChange(String airlineInitiatedChangeId) {
        logger.info("Retrieving airline-initiated change with ID: {}", airlineInitiatedChangeId);
        
        AirlineInitiatedChangeResponse response = duffelClient.getAirlineInitiatedChange(
                authToken, 
                "application/json", 
                duffelVersion, 
                airlineInitiatedChangeId
        );
        
        logger.info("Successfully retrieved airline-initiated change with ID: {}", airlineInitiatedChangeId);
        return response.getData();
    }
    
    /**
     * Updates an airline-initiated change to reflect the action taken
     */
    public AirlineInitiatedChangeResponse.AirlineInitiatedChange updateAirlineInitiatedChange(String airlineInitiatedChangeId, String actionTaken) {
        logger.info("Updating airline-initiated change with ID: {} and action: {}", airlineInitiatedChangeId, actionTaken);
        
        AirlineInitiatedChangeUpdateRequest request = AirlineInitiatedChangeUpdateRequest.builder()
                .actionTaken(actionTaken)
                .build();
        
        DuffelApiWrapper<AirlineInitiatedChangeUpdateRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        AirlineInitiatedChangeResponse response = duffelClient.updateAirlineInitiatedChange(
                authToken, 
                "application/json", 
                duffelVersion, 
                airlineInitiatedChangeId, 
                wrapper
        );
        
        logger.info("Successfully updated airline-initiated change with ID: {}", airlineInitiatedChangeId);
        return response.getData();
    }
    
    /**
     * Accepts an airline-initiated change
     */
    public AirlineInitiatedChangeResponse.AirlineInitiatedChange acceptAirlineInitiatedChange(String airlineInitiatedChangeId) {
        logger.info("Accepting airline-initiated change with ID: {}", airlineInitiatedChangeId);
        
        AirlineInitiatedChangeResponse response = duffelClient.acceptAirlineInitiatedChange(
                authToken, 
                "application/json", 
                duffelVersion, 
                airlineInitiatedChangeId
        );
        
        logger.info("Successfully accepted airline-initiated change with ID: {}", airlineInitiatedChangeId);
        return response.getData();
    }
} 