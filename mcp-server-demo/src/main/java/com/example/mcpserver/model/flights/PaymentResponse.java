package com.example.mcpserver.model.flights;

import lombok.Data;

@Data
public class PaymentResponse {
    private Payment data;
    
    @Data
    public static class Payment {
        private String id;
        private Boolean live_mode;
        private String created_at;
        private String type;
        private String amount;
        private String currency;
    }
} 