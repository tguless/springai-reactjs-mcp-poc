package com.example.mcpserver.model.flights;

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
    private String id;
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
    
    private Double latitude;
    private Double longitude;
    
    private City city;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class City {
        private String id;
        private String name;
        
        @JsonProperty("iata_code")
        private String iataCode;
        
        @JsonProperty("iata_country_code")
        private String iataCountryCode;
        
        private List<AirportSummary> airports;
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class AirportSummary {
            private String id;
            private String name;
            
            @JsonProperty("iata_code")
            private String iataCode;
            
            @JsonProperty("icao_code")
            private String icaoCode;
            
            @JsonProperty("iata_country_code")
            private String iataCountryCode;
            
            private Double latitude;
            private Double longitude;
            
            @JsonProperty("time_zone")
            private String timeZone;
        }
    }
} 