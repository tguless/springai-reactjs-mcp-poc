package com.example.duffel.model.flights;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Response model for order cancellation operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancellationResponse {
    
    private Meta meta;
    private List<OrderCancellation> data;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private Integer limit;
        private String before;
        private String after;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderCancellation {
        
        private String id;
        
        @JsonProperty("live_mode")
        private Boolean liveMode;
        
        @JsonProperty("order_id")
        private String orderId;
        
        @JsonProperty("refund_amount")
        private String refundAmount;
        
        @JsonProperty("refund_currency")
        private String refundCurrency;
        
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
        
        @JsonProperty("airline_credits")
        private List<AirlineCredit> airlineCredits;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AirlineCredit {
        
        private String id;
        
        @JsonProperty("passenger_id")
        private String passengerId;
        
        @JsonProperty("credit_amount")
        private String creditAmount;
        
        @JsonProperty("credit_currency")
        private String creditCurrency;
        
        @JsonProperty("credit_code")
        private String creditCode;
        
        @JsonProperty("credit_name")
        private String creditName;
        
        @JsonProperty("issued_on")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private String issuedOn;
    }
} 