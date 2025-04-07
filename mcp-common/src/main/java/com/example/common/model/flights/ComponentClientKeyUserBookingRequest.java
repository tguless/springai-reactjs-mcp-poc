package com.example.common.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for creating a Component Client Key scoped to a user and booking in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentClientKeyUserBookingRequest {
    @JsonProperty("user_id")
    private String userId;
    
    @JsonProperty("booking_id")
    private String bookingId;
} 