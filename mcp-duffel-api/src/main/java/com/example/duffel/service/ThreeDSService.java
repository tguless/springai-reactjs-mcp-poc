package com.example.duffel.service;

import com.example.duffel.client.DuffelThreeDSClient;
import com.example.duffel.model.flights.DuffelApiWrapper;
import com.example.duffel.model.payments.ThreeDSSessionCreateRequest;
import com.example.duffel.model.payments.ThreeDSSessionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThreeDSService {
    
    private static final Logger logger = LoggerFactory.getLogger(ThreeDSService.class);
    
    private final DuffelThreeDSClient threeDSClient;
    private final String authToken;
    private final String duffelVersion;
    
    public ThreeDSService(DuffelThreeDSClient threeDSClient, 
                         @Value("${duffel.auth-token}") String authToken,
                         @Value("${duffel.version}") String duffelVersion) {
        this.threeDSClient = threeDSClient;
        this.authToken = "Bearer " + authToken;
        this.duffelVersion = duffelVersion;
        logger.info("ThreeDSService initialized with Duffel API version: {}", duffelVersion);
    }
    
    /**
     * Creates a 3DS session for payment authentication
     */
    public ThreeDSSessionResponse.ThreeDSSession createThreeDSSession(
            String cardId, 
            String resourceId, 
            String exception, 
            List<ThreeDSSessionCreateRequest.Service> services) {
        
        logger.info("Creating 3DS session for card ID: {} and resource ID: {}", cardId, resourceId);
        
        ThreeDSSessionCreateRequest request = ThreeDSSessionCreateRequest.builder()
                .cardId(cardId)
                .resourceId(resourceId)
                .exception(exception)
                .services(services)
                .build();
        
        DuffelApiWrapper<ThreeDSSessionCreateRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        ThreeDSSessionResponse response = threeDSClient.createThreeDSSession(
                authToken, 
                "application/json", 
                duffelVersion, 
                wrapper
        );
        
        logger.info("3DS session created successfully with ID: {} and status: {}", 
                response.getData().getId(), response.getData().getStatus());
        
        return response.getData();
    }
    
    /**
     * Creates a 3DS session with a secure corporate payment exception
     */
    public ThreeDSSessionResponse.ThreeDSSession createSecureCorporatePaymentSession(
            String cardId, 
            String resourceId, 
            List<ThreeDSSessionCreateRequest.Service> services) {
        
        logger.info("Creating secure corporate payment 3DS session for card ID: {} and resource ID: {}", 
                cardId, resourceId);
        
        return createThreeDSSession(cardId, resourceId, "secure_corporate_payment", services);
    }
} 