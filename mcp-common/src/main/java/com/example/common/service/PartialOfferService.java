package com.example.common.service;

import com.example.common.client.DuffelClient;
import com.example.common.model.flights.DuffelApiWrapper;
import com.example.common.model.flights.PartialOfferRequestCreate;
import com.example.common.model.flights.PartialOfferRequestResponse;
import com.example.common.model.flights.PartialOfferFaresResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartialOfferService {
    
    private static final Logger logger = LoggerFactory.getLogger(PartialOfferService.class);
    
    private final DuffelClient duffelClient;
    private final String authToken;
    private final String duffelVersion;
    
    public PartialOfferService(DuffelClient duffelClient, 
                               @Value("${duffel.auth-token}") String authToken,
                               @Value("${duffel.version}") String duffelVersion) {
        this.duffelClient = duffelClient;
        this.authToken = "Bearer " + authToken;
        this.duffelVersion = duffelVersion;
        logger.info("PartialOfferService initialized with Duffel API version: {}", duffelVersion);
    }
    
    /**
     * Creates a partial offer request to search for flights for each slice separately
     */
    public PartialOfferRequestResponse.PartialOfferRequest createPartialOfferRequest(PartialOfferRequestCreate request) {
        logger.info("Creating partial offer request with {} passengers and {} slices", 
                request.getPassengers().size(), request.getSlices().size());
        
        DuffelApiWrapper<PartialOfferRequestCreate> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        PartialOfferRequestResponse response = duffelClient.createPartialOfferRequest(
                authToken, 
                "application/json", 
                duffelVersion, 
                wrapper
        );
        
        logger.info("Partial offer request created successfully with ID: {}", response.getData().getId());
        return response.getData();
    }
    
    /**
     * Retrieves a single partial offer request by ID
     */
    public PartialOfferRequestResponse.PartialOfferRequest getPartialOfferRequest(String partialOfferRequestId) {
        logger.info("Retrieving partial offer request with ID: {}", partialOfferRequestId);
        
        PartialOfferRequestResponse response = duffelClient.getPartialOfferRequest(
                authToken, 
                "application/json", 
                duffelVersion, 
                partialOfferRequestId
        );
        
        logger.info("Successfully retrieved partial offer request with ID: {}", partialOfferRequestId);
        return response.getData();
    }
    
    /**
     * Retrieves full offer fares for the given partial offers
     */
    public PartialOfferFaresResponse.FareData getPartialOfferFares(String partialOfferRequestId, List<String> selectedPartialOffers) {
        logger.info("Retrieving fares for partial offer request: {} with {} selected offers", 
                partialOfferRequestId, selectedPartialOffers.size());
        
        PartialOfferFaresResponse response = duffelClient.getPartialOfferFares(
                authToken, 
                "application/json", 
                duffelVersion, 
                partialOfferRequestId, 
                selectedPartialOffers
        );
        
        logger.info("Successfully retrieved fares for partial offer request: {}", partialOfferRequestId);
        return response.getData();
    }
} 