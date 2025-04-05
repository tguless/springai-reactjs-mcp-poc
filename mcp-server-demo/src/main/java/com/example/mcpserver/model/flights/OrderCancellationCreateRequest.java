package com.example.mcpserver.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for creating a pending order cancellation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancellationCreateRequest {
    
    @JsonProperty("order_id")
    private String orderId;
} 