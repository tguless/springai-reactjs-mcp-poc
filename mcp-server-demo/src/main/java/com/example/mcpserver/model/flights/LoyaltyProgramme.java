package com.example.mcpserver.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing a Loyalty Programme in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyProgramme {
    private String id;
    private String name;
    private String alliance;
    
    @JsonProperty("logo_url")
    private String logoUrl;
    
    @JsonProperty("owner_airline_id")
    private String ownerAirlineId;
} 