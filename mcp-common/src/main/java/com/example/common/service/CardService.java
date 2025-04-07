package com.example.common.service;

import com.example.common.client.DuffelCardClient;
import com.example.common.model.flights.DuffelApiWrapper;
import com.example.common.model.payments.CardCreateRequest;
import com.example.common.model.payments.CardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CardService {
    
    private static final Logger logger = LoggerFactory.getLogger(CardService.class);
    
    private final DuffelCardClient cardClient;
    private final String authToken;
    private final String duffelVersion;
    
    public CardService(DuffelCardClient cardClient, 
                       @Value("${duffel.auth-token}") String authToken,
                       @Value("${duffel.version}") String duffelVersion) {
        this.cardClient = cardClient;
        this.authToken = "Bearer " + authToken;
        this.duffelVersion = duffelVersion;
        logger.info("CardService initialized with Duffel API version: {}", duffelVersion);
    }
    
    /**
     * Creates a card that can be used for payment
     */
    public CardResponse.Card createCard(CardCreateRequest request) {
        logger.info("Creating payment card for {}", request.getName());
        
        // Log minimal card info for debugging (never log full card details)
        if (request.getNumber() != null) {
            String last4 = request.getNumber().substring(Math.max(0, request.getNumber().length() - 4));
            logger.info("Card ends with: {}", last4);
        }
        
        DuffelApiWrapper<CardCreateRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        CardResponse response = cardClient.createCard(
                authToken, 
                "application/json", 
                duffelVersion, 
                wrapper
        );
        
        logger.info("Card created successfully with ID: {}", response.getData().getId());
        return response.getData();
    }
    
    /**
     * Creates a single-use card from a multi-use card
     */
    public CardResponse.Card createCardFromExistingCard(String cardId, String cvc) {
        logger.info("Creating payment card from existing card ID: {}", cardId);
        
        CardCreateRequest request = CardCreateRequest.builder()
                .cardId(cardId)
                .cvc(cvc)
                .build();
        
        DuffelApiWrapper<CardCreateRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        CardResponse response = cardClient.createCard(
                authToken, 
                "application/json", 
                duffelVersion, 
                wrapper
        );
        
        logger.info("Card created successfully with ID: {}", response.getData().getId());
        return response.getData();
    }
    
    /**
     * Deletes a card by ID
     */
    public void deleteCard(String cardId) {
        logger.info("Deleting card with ID: {}", cardId);
        
        cardClient.deleteCard(
                authToken, 
                "application/json", 
                duffelVersion, 
                cardId
        );
        
        logger.info("Card deleted successfully with ID: {}", cardId);
    }
} 