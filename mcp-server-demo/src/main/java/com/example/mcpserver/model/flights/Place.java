package com.example.mcpserver.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model representing a Place in the Duffel API (a city or airport that can serve as an origin or destination)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    private String id;
    private String name;
    private String type; // "airport" or "city"
    
    @JsonProperty("iata_code")
    private String iataCode;
    
    @JsonProperty("icao_code")
    private String icaoCode;
    
    @JsonProperty("iata_country_code")
    private String iataCountryCode;
    
    @JsonProperty("iata_city_code")
    private String iataCityCode;
    
    @JsonProperty("city_name")
    private String cityName;
    
    private City city;
    
    private Double latitude;
    private Double longitude;
    
    @JsonProperty("time_zone")
    private String timeZone;
    
    private List<Place> airports;
} 