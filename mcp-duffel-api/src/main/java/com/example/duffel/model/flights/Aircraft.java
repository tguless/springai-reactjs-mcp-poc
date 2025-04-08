package com.example.duffel.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing an Aircraft in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aircraft {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("iata_code")
    private String iataCode;
} 