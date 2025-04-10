package com.example.duffel.model.flights;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response model for Aircraft operations in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AircraftResponse {
    private Aircraft data;
    private List<Aircraft> data_list;
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