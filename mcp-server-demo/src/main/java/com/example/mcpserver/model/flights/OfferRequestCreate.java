package com.example.mcpserver.model.flights;

import java.util.List;
import lombok.Data;
import lombok.Builder;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
public class OfferRequestCreate {
    @JsonProperty("slices")
    private List<Slice> slices;
    @JsonProperty("passengers")
    private List<Passenger> passengers;
    @JsonProperty("cabin_class")
    private CabinClass cabinClass;
    
    // Additional parameters that may be needed
    @JsonProperty("return_offers")
    private String returnOffers;
    @JsonProperty("max_connections")
    private String maxConnections;
    
    @Data
    @Builder
    public static class Slice {
        @JsonProperty("origin")
        private String origin;
        @JsonProperty("destination")
        private String destination;
        @JsonProperty("departure_date")
        private LocalDate departureDate;
        
        // Optional parameters
        @JsonProperty("departure_time")
        private String departureTime;
        @JsonProperty("arrival_time")
        private String arrivalTime;
    }
    
    @Data
    @Builder
    public static class Passenger {
        @JsonProperty("type")
        private PassengerType type;
        
        // Optional parameters
        @JsonProperty("given_name")
        private String givenName;
        @JsonProperty("family_name")
        private String familyName;
        @JsonProperty("loyalty_programme_accounts")
        private String loyaltyProgrammeAccounts;
    }
    
    public enum PassengerType {
        adult,
        child,
        infant_without_seat
    }
    
    public enum CabinClass {
        economy,
        premium_economy,
        business,
        first
    }
} 