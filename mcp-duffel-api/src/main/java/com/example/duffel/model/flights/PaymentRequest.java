package com.example.duffel.model.flights;

import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
public class PaymentRequest {
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("payment")
    private Payment payment;
    
    @Data
    @Builder
    public static class Payment {
        @JsonProperty("type")
        private String type;
        @JsonProperty("amount")
        private String amount;
        @JsonProperty("currency")
        private String currency;
        @JsonProperty("three_d_secure_session_id")
        private String threeDSecureSessionId;
    }
} 