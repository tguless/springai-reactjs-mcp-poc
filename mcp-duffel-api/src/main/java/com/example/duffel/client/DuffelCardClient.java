package com.example.duffel.client;

import com.example.common.config.FeignClientConfig;
import com.example.duffel.model.payments.CardCreateRequest;
import com.example.duffel.model.payments.CardResponse;
import com.example.duffel.model.flights.DuffelApiWrapper;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Client for Duffel Card API
 * Note: This API uses a different hostname (api.duffel.cards) for PCI compliance
 */
@FeignClient(name = "duffelCard", url = "${duffel.card-base-url}", configuration = FeignClientConfig.class)
public interface DuffelCardClient {
    
    @PostMapping("/payments/cards")
    CardResponse createCard(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<CardCreateRequest> request
    );
    
    @DeleteMapping("/payments/cards/{id}")
    void deleteCard(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String cardId
    );
} 