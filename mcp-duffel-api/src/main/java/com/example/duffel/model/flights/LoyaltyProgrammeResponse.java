package com.example.duffel.model.flights;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response model for Loyalty Programme operations in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyProgrammeResponse {
    private LoyaltyProgramme data;
    private List<LoyaltyProgramme> data_list;
    private Meta meta;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private Integer limit;
        private String before;
        private String after;
    }
} 