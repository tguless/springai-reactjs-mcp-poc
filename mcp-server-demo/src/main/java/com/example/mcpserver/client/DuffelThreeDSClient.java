package com.example.mcpserver.client;

import com.example.mcpserver.config.FeignClientConfig;
import com.example.mcpserver.model.payments.ThreeDSSessionCreateRequest;
import com.example.mcpserver.model.payments.ThreeDSSessionResponse;
import com.example.mcpserver.model.flights.DuffelApiWrapper;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Client for Duffel 3D Secure API
 */
@FeignClient(name = "duffelThreeDS", url = "${duffel.base-url}", configuration = FeignClientConfig.class)
public interface DuffelThreeDSClient {
    
    @PostMapping("/payments/three_d_secure_sessions")
    ThreeDSSessionResponse createThreeDSSession(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<ThreeDSSessionCreateRequest> request
    );
} 