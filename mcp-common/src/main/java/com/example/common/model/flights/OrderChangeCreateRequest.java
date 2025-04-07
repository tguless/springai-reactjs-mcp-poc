package com.example.common.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for creating a pending order change
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderChangeCreateRequest {
    
    @JsonProperty("order_change_offer_id")
    private String orderChangeOfferId;
} 