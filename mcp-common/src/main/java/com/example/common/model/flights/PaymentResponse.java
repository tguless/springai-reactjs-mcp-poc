package com.example.common.model.flights;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class PaymentResponse {
    @JsonProperty("data")
    private Payment data;
    
    @Data
    public static class Payment {
        @JsonProperty("id")
        private String id;
        @JsonProperty("live_mode")
        private Boolean liveMode;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("type")
        private String type;
        @JsonProperty("amount")
        private String amount;
        @JsonProperty("currency")
        private String currency;
    }
} 