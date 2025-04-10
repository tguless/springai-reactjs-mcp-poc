package com.example.duffel.client;

import com.example.common.config.FeignClientConfig;
import com.example.duffel.model.links.SessionCreateRequest;
import com.example.duffel.model.links.SessionResponse;
import com.example.duffel.model.flights.DuffelApiWrapper;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Client for Duffel Links Sessions API
 */
@FeignClient(name = "duffelSession", url = "${duffel.links-base-url}", configuration = FeignClientConfig.class)
public interface DuffelSessionClient {
    
    @PostMapping("/sessions")
    SessionResponse createSession(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<SessionCreateRequest> request
    );
} 