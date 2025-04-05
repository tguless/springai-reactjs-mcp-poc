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
public class PartialOfferRequestResponse {
    
    @JsonProperty("data")
    private PartialOfferRequest data;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PartialOfferRequest {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("live_mode")
        private Boolean liveMode;
        
        @JsonProperty("created_at")
        private OffsetDateTime createdAt;
        
        @JsonProperty("cabin_class")
        private String cabinClass;
        
        @JsonProperty("passengers")
        private List<Passenger> passengers;
        
        @JsonProperty("slices")
        private List<SliceInfo> slices;
        
        @JsonProperty("offers")
        private List<PartialOffer> offers;
        
        @JsonProperty("client_key")
        private String clientKey;
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
        
        @JsonProperty("loyalty_programme_accounts")
        private List<LoyaltyProgrammeAccount> loyaltyProgrammeAccounts;
        
        @JsonProperty("age")
        private Integer age;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SliceInfo {
        @JsonProperty("origin_type")
        private String originType;
        
        @JsonProperty("origin")
        private Place origin;
        
        @JsonProperty("destination_type")
        private String destinationType;
        
        @JsonProperty("destination")
        private Place destination;
        
        @JsonProperty("departure_date")
        private String departureDate;
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
    public static class PartialOffer {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("slice_id")
        private String sliceId;
        
        @JsonProperty("origin")
        private Place origin;
        
        @JsonProperty("destination")
        private Place destination;
        
        @JsonProperty("departure_date")
        private String departureDate;
        
        @JsonProperty("departure_time")
        private String departureTime;
        
        @JsonProperty("arrival_time")
        private String arrivalTime;
        
        @JsonProperty("total_duration")
        private String totalDuration;
        
        @JsonProperty("segments")
        private List<Segment> segments;
        
        @JsonProperty("base_amount")
        private String baseAmount;
        
        @JsonProperty("base_currency")
        private String baseCurrency;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Segment {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("departing_at")
        private String departingAt;
        
        @JsonProperty("arriving_at")
        private String arrivingAt;
        
        @JsonProperty("origin")
        private Place origin;
        
        @JsonProperty("destination")
        private Place destination;
        
        @JsonProperty("marketing_carrier")
        private Carrier marketingCarrier;
        
        @JsonProperty("operating_carrier")
        private Carrier operatingCarrier;
        
        @JsonProperty("duration")
        private String duration;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Carrier {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("iata_code")
        private String iataCode;
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