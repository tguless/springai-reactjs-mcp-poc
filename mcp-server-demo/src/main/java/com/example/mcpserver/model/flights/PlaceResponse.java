package com.example.mcpserver.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response model for Place operations in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceResponse {
    private List<Place> data;
    
    /**
     * A list of warnings that may be returned by the API
     */
    private List<Warning> warnings;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Warning {
        private String type;
        private String title;
        private String message;
        private String code;
    }
} 