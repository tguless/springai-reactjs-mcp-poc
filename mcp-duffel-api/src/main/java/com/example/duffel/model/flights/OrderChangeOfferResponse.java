package com.example.duffel.model.flights;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Response model for retrieving order change offers
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderChangeOfferResponse {
    
    private Meta meta;
    private List<OrderChangeOffer> data;
    
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
    public static class OrderChangeOffer {
        
        private String id;
        
        @JsonProperty("live_mode")
        private Boolean liveMode;
        
        @JsonProperty("created_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private ZonedDateTime createdAt;
        
        @JsonProperty("updated_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private ZonedDateTime updatedAt;
        
        @JsonProperty("expires_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private ZonedDateTime expiresAt;
        
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
        
        private OfferSlices slices;
        
        private Conditions conditions;
        
        @JsonProperty("order_change_id")
        private String orderChangeId;
        
        @JsonProperty("private_fares")
        private List<PrivateFare> privateFares;
    }
    
    // Reusing the same nested classes from OrderChangeRequestResponse
    // For simplicity, most of these nested classes would be duplicated
    // In a real application, you might want to extract common models
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfferSlices {
        private List<Object> remove;
        private List<Object> add;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Conditions {
        @JsonProperty("refund_before_departure")
        private RefundBeforeDeparture refundBeforeDeparture;
        
        @JsonProperty("change_before_departure")
        private ChangeBeforeDeparture changeBeforeDeparture;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefundBeforeDeparture {
        private Boolean allowed;
        
        @JsonProperty("penalty_amount")
        private String penaltyAmount;
        
        @JsonProperty("penalty_currency")
        private String penaltyCurrency;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeBeforeDeparture {
        private Boolean allowed;
        
        @JsonProperty("penalty_amount")
        private String penaltyAmount;
        
        @JsonProperty("penalty_currency")
        private String penaltyCurrency;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrivateFare {
        private String type;
        
        @JsonProperty("corporate_code")
        private String corporateCode;
        
        @JsonProperty("tour_code")
        private String tourCode;
        
        @JsonProperty("tracking_reference")
        private String trackingReference;
    }
} 