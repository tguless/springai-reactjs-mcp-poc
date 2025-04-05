package com.example.mcpserver.client;

import com.example.mcpserver.config.FeignClientConfig;
import com.example.mcpserver.model.links.SessionCreateRequest;
import com.example.mcpserver.model.links.SessionResponse;
import com.example.mcpserver.model.flights.DuffelApiWrapper;

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