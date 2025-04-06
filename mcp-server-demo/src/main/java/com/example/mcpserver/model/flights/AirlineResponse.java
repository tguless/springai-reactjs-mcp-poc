package com.example.mcpserver.model.flights;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response model for Airline operations in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirlineResponse {
    private Airline data;
    private List<Airline> data_list;
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