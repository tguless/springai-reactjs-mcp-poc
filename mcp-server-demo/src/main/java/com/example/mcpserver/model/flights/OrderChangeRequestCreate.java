package com.example.mcpserver.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Request model for creating an order change request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderChangeRequestCreate {
    
    @JsonProperty("order_id")
    private String orderId;
    
    private Slices slices;
    
    @JsonProperty("private_fares")
    private Map<String, List<PrivateFare>> privateFares;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Slices {
        private List<RemoveSlice> remove;
        private List<AddSlice> add;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemoveSlice {
        @JsonProperty("slice_id")
        private String sliceId;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddSlice {
        private String origin;
        private String destination;
        
        @JsonProperty("departure_date")
        private LocalDate departureDate;
        
        @JsonProperty("cabin_class")
        private String cabinClass;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrivateFare {
        @JsonProperty("corporate_code")
        private String corporateCode;
        
        @JsonProperty("tour_code")
        private String tourCode;
        
        @JsonProperty("tracking_reference")
        private String trackingReference;
    }
} 