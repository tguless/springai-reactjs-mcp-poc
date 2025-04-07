package com.example.common.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model for Component Client Key operations in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentClientKeyResponse {
    private ComponentClientKey data;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComponentClientKey {
        @JsonProperty("component_client_key")
        private String componentClientKey;
    }
} 