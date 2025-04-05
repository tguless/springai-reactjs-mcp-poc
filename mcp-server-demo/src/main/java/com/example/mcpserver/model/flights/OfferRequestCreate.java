package com.example.mcpserver.model.flights;

import java.util.List;
import lombok.Data;
import lombok.Builder;
import java.time.LocalDate;

@Data
@Builder
public class OfferRequestCreate {
    private List<Slice> slices;
    private List<Passenger> passengers;
    private CabinClass cabinClass;
    
    // Additional parameters that may be needed
    private String returnOffers;
    private String maxConnections;
    
    @Data
    @Builder
    public static class Slice {
        private String origin;
        private String destination;
        private LocalDate departureDate;
        
        // Optional parameters
        private String departureTime;
        private String arrivalTime;
    }
    
    @Data
    @Builder
    public static class Passenger {
        private PassengerType type;
        
        // Optional parameters
        private String givenName;
        private String familyName;
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