package com.example.duffel.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model representing an Airport in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Airport {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    
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
    
    @JsonProperty("time_zone")
    private String timeZone;
    
    @JsonProperty("latitude")
    private Double latitude;
    @JsonProperty("longitude")
    private Double longitude;
    
    @JsonProperty("city")
    private City city;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class City {
        @JsonProperty("id")
        private String id;
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("iata_code")
        private String iataCode;
        
        @JsonProperty("iata_country_code")
        private String iataCountryCode;
        
        @JsonProperty("airports")
        private List<AirportSummary> airports;
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class AirportSummary {
            @JsonProperty("id")
            private String id;
            @JsonProperty("name")
            private String name;
            
            @JsonProperty("iata_code")
            private String iataCode;
            
            @JsonProperty("icao_code")
            private String icaoCode;
            
            @JsonProperty("iata_country_code")
            private String iataCountryCode;
            
            @JsonProperty("latitude")
            private Double latitude;
            @JsonProperty("longitude")
            private Double longitude;
            
            @JsonProperty("time_zone")
            private String timeZone;
        }
    }
} 