package com.example.duffel.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for confirming an order change
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderChangeConfirmRequest {
    
    private Payment payment;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payment {
        private String type;
        
        private String amount;
        
        private String currency;
        
        @JsonProperty("three_d_secure_session_id")
        private String threeDSecureSessionId;
    }
} 