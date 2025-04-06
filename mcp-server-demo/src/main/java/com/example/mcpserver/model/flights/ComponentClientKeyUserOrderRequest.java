package com.example.mcpserver.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for creating a Component Client Key scoped to a user and order in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentClientKeyUserOrderRequest {
    @JsonProperty("user_id")
    private String userId;
    
    @JsonProperty("order_id")
    private String orderId;
} 