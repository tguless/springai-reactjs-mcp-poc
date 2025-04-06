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
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
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
    
    @JsonProperty("city")
    private City city;
    
    @JsonProperty("latitude")
    private Double latitude;
    @JsonProperty("longitude")
    private Double longitude;
    
    @JsonProperty("time_zone")
    private String timeZone;
    
    @JsonProperty("airports")
    private List<Place> airports;
} 