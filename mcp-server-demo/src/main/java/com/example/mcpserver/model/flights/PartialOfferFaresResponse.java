package com.example.mcpserver.model.flights;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartialOfferFaresResponse {
    
    @JsonProperty("data")
    private FareData data;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FareData {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("live_mode")
        private Boolean liveMode;
        
        @JsonProperty("created_at")
        private OffsetDateTime createdAt;
        
        @JsonProperty("cabin_class")
        private String cabinClass;
        
        @JsonProperty("client_key")
        private String clientKey;
        
        @JsonProperty("slices")
        private List<SliceData> slices;
        
        @JsonProperty("passengers")
        private List<Passenger> passengers;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SliceData {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("origin_type")
        private String originType;
        
        @JsonProperty("origin")
        private Place origin;
        
        @JsonProperty("destination_type")
        private String destinationType;
        
        @JsonProperty("destination")
        private Place destination;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Passenger {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("type")
        private String type;
        
        @JsonProperty("given_name")
        private String givenName;
        
        @JsonProperty("family_name")
        private String familyName;
        
        @JsonProperty("age")
        private Integer age;
        
        @JsonProperty("loyalty_programme_accounts")
        private List<LoyaltyProgrammeAccount> loyaltyProgrammeAccounts;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Place {
        @JsonProperty("type")
        private String type;
        
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("iata_code")
        private String iataCode;
        
        @JsonProperty("iata_country_code")
        private String iataCountryCode;
        
        @JsonProperty("icao_code")
        private String icaoCode;
        
        @JsonProperty("city_name")
        private String cityName;
        
        @JsonProperty("city")
        private City city;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class City {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("iata_code")
        private String iataCode;
        
        @JsonProperty("iata_country_code")
        private String iataCountryCode;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LoyaltyProgrammeAccount {
        @JsonProperty("airline_iata_code")
        private String airlineIataCode;
        
        @JsonProperty("account_number")
        private String accountNumber;
    }
} 