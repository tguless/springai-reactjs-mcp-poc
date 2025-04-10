package com.example.duffel.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing an Airline in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Airline {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("iata_code")
    private String iataCode;
    
    @JsonProperty("logo_symbol_url")
    private String logoSymbolUrl;
    
    @JsonProperty("logo_lockup_url")
    private String logoLockupUrl;
    
    @JsonProperty("conditions_of_carriage_url")
    private String conditionsOfCarriageUrl;
} 