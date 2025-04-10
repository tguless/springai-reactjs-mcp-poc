package com.example.duffel.model.flights;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * Response model for order change operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderChangeResponse {
    
    private OrderChange data;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderChange {
        
        private String id;
        
        @JsonProperty("live_mode")
        private Boolean liveMode;
        
        @JsonProperty("order_id")
        private String orderId;
        
        @JsonProperty("change_total_amount")
        private String changeTotalAmount;
        
        @JsonProperty("change_total_currency")
        private String changeTotalCurrency;
        
        @JsonProperty("new_total_amount")
        private String newTotalAmount;
        
        @JsonProperty("new_total_currency")
        private String newTotalCurrency;
        
        @JsonProperty("penalty_total_amount")
        private String penaltyTotalAmount;
        
        @JsonProperty("penalty_total_currency")
        private String penaltyTotalCurrency;
        
        @JsonProperty("refund_to")
        private String refundTo;
        
        @JsonProperty("created_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private ZonedDateTime createdAt;
        
        @JsonProperty("expires_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private ZonedDateTime expiresAt;
        
        @JsonProperty("confirmed_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private ZonedDateTime confirmedAt;
        
        // Reusing the slices structure from OrderChangeOfferResponse
        // For simplicity we're using Object here, but in a real implementation
        // we would create detailed models for all the nested data
        private Object slices;
    }
} 