package com.example.mcpserver.model.flights;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {
    private String orderId;
    private Payment payment;
    
    @Data
    @Builder
    public static class Payment {
        private String type;
        private String amount;
        private String currency;
        private String threeDSecureSessionId;
    }
} 