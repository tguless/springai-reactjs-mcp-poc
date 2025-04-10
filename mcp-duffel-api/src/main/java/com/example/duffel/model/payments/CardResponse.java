package com.example.duffel.model.payments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardResponse {
    
    @JsonProperty("data")
    private Card data;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Card {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("live_mode")
        private Boolean liveMode;
        
        @JsonProperty("brand")
        private String brand;
        
        @JsonProperty("last_4_digits")
        private String last4Digits;
        
        @JsonProperty("multi_use")
        private Boolean multiUse;
        
        @JsonProperty("unavailable_at")
        private OffsetDateTime unavailableAt;
    }
} 