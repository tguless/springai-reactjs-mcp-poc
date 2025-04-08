package com.example.duffel.service;

import com.example.duffel.client.DuffelClient;
import com.example.duffel.model.flights.BatchOfferRequestCreate;
import com.example.duffel.model.flights.BatchOfferRequestResponse;
import com.example.duffel.model.flights.DuffelApiWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BatchOfferService {
    
    private static final Logger logger = LoggerFactory.getLogger(BatchOfferService.class);
    
    private final DuffelClient duffelClient;
    private final String authToken;
    private final String duffelVersion;
    
    public BatchOfferService(DuffelClient duffelClient, 
                             @Value("${duffel.auth-token}") String authToken,
                             @Value("${duffel.version}") String duffelVersion) {
        this.duffelClient = duffelClient;
        this.authToken = "Bearer " + authToken;
        this.duffelVersion = duffelVersion;
        logger.info("BatchOfferService initialized with Duffel API version: {}", duffelVersion);
    }
    
    /**
     * Creates a batch offer request to search for flights
     */
    public BatchOfferRequestResponse.BatchOfferRequest createBatchOfferRequest(BatchOfferRequestCreate request, Integer supplierTimeout) {
        logger.info("Creating batch offer request with {} passengers and {} slices", 
                request.getPassengers().size(), request.getSlices().size());
        
        if (supplierTimeout != null) {
            logger.info("Using custom supplier timeout: {} ms", supplierTimeout);
            if (supplierTimeout < 2000 || supplierTimeout > 60000) {
                logger.warn("Supplier timeout {} is outside the valid range (2000-60000), using default", supplierTimeout);
            }
        }
        
        DuffelApiWrapper<BatchOfferRequestCreate> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        BatchOfferRequestResponse response = duffelClient.createBatchOfferRequest(
                authToken, 
                "application/json", 
                duffelVersion,
                supplierTimeout,
                wrapper
        );
        
        logger.info("Batch offer request created successfully with ID: {}, total batches: {}", 
                response.getData().getId(), response.getData().getTotalBatches());
        return response.getData();
    }
    
    /**
     * Retrieves a batch of offers from a batch offer request
     * Can be called repeatedly to get all batches (long-polling style)
     */
    public BatchOfferRequestResponse.BatchOfferRequest getBatchOfferRequest(String batchOfferRequestId) {
        logger.info("Retrieving batch offer request with ID: {}", batchOfferRequestId);
        
        BatchOfferRequestResponse response = duffelClient.getBatchOfferRequest(
                authToken, 
                "application/json", 
                duffelVersion, 
                batchOfferRequestId
        );
        
        int remainingBatches = response.getData().getRemainingBatches() != null ? 
                response.getData().getRemainingBatches() : 0;
        
        int offerCount = response.getData().getOffers() != null ? 
                response.getData().getOffers().size() : 0;
        
        logger.info("Successfully retrieved batch offer request with ID: {}, offers: {}, remaining batches: {}", 
                batchOfferRequestId, offerCount, remainingBatches);
        
        return response.getData();
    }
} 