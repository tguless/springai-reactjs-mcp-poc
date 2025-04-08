package com.example.duffel.service;

import com.example.duffel.client.DuffelSessionClient;
import com.example.duffel.model.flights.DuffelApiWrapper;
import com.example.duffel.model.links.SessionCreateRequest;
import com.example.duffel.model.links.SessionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    
    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);
    
    private final DuffelSessionClient sessionClient;
    private final String authToken;
    private final String duffelVersion;
    
    public SessionService(DuffelSessionClient sessionClient, 
                          @Value("${duffel.auth-token}") String authToken,
                          @Value("${duffel.version}") String duffelVersion) {
        this.sessionClient = sessionClient;
        this.authToken = "Bearer " + authToken;
        this.duffelVersion = duffelVersion;
        logger.info("SessionService initialized with Duffel API version: {}", duffelVersion);
    }
    
    /**
     * Creates a new session for traveler to go through search and book flow
     */
    public String createSession(SessionCreateRequest request) {
        logger.info("Creating session with reference: {}", request.getReference());
        
        DuffelApiWrapper<SessionCreateRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        SessionResponse response = sessionClient.createSession(
                authToken, 
                "application/json", 
                duffelVersion, 
                wrapper
        );
        
        logger.info("Session created successfully with URL: {}", response.getData().getUrl());
        return response.getData().getUrl();
    }
    
    /**
     * Creates a flights-only session
     */
    public String createFlightsSession(
            String reference, 
            String successUrl, 
            String failureUrl, 
            String abandonmentUrl) {
        
        logger.info("Creating flights-only session with reference: {}", reference);
        
        SessionCreateRequest request = SessionCreateRequest.builder()
                .reference(reference)
                .successUrl(successUrl)
                .failureUrl(failureUrl)
                .abandonmentUrl(abandonmentUrl)
                .flights(SessionCreateRequest.FlightsConfig.builder().enabled("true").build())
                .build();
        
        return createSession(request);
    }
    
    /**
     * Creates a combined flights and stays session
     */
    public String createFlightsAndStaysSession(
            String reference, 
            String successUrl, 
            String failureUrl, 
            String abandonmentUrl) {
        
        logger.info("Creating flights and stays session with reference: {}", reference);
        
        SessionCreateRequest request = SessionCreateRequest.builder()
                .reference(reference)
                .successUrl(successUrl)
                .failureUrl(failureUrl)
                .abandonmentUrl(abandonmentUrl)
                .flights(SessionCreateRequest.FlightsConfig.builder().enabled("true").build())
                .stays(SessionCreateRequest.StaysConfig.builder().enabled("true").build())
                .build();
        
        return createSession(request);
    }
    
    /**
     * Creates a custom-branded session
     */
    public String createBrandedSession(
            String reference, 
            String successUrl, 
            String failureUrl, 
            String abandonmentUrl,
            String logoUrl,
            String primaryColor,
            String secondaryColor,
            String checkoutDisplayText) {
        
        logger.info("Creating branded session with reference: {}", reference);
        
        SessionCreateRequest request = SessionCreateRequest.builder()
                .reference(reference)
                .successUrl(successUrl)
                .failureUrl(failureUrl)
                .abandonmentUrl(abandonmentUrl)
                .logoUrl(logoUrl)
                .primaryColor(primaryColor)
                .secondaryColor(secondaryColor)
                .checkoutDisplayText(checkoutDisplayText)
                .flights(SessionCreateRequest.FlightsConfig.builder().enabled("true").build())
                .build();
        
        return createSession(request);
    }
} 