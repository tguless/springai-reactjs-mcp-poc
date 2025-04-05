package com.example.mcpserver.model.flights;

import java.util.List;
import lombok.Data;

@Data
public class OfferRequestResponse {
    private OfferRequestData data;
    
    @Data
    public static class OfferRequestData {
        private String id;
        private String liveMode;
        private String createdAt;
        private List<Offer> offers;
        private List<Slice> slices;
        private List<Passenger> passengers;
        private String cabinClass;
    }
    
    @Data
    public static class Offer {
        private String id;
        private String createdAt;
        private String updatedAt;
        private String liveModeKey;
        private String totalAmount;
        private String totalCurrency;
        private List<OfferSlice> slices;
        private List<PassengerPricing> passengerPricings;
        private List<String> availableServices;
        private String tax;
    }
    
    @Data
    public static class OfferSlice {
        private String id;
        private String origin;
        private String destination;
        private String departureDate;
        private String arrivalDate;
        private Integer duration;
        private List<Segment> segments;
    }
    
    @Data
    public static class Segment {
        private String id;
        private String departureAirport;
        private String arrivalAirport;
        private String departureTime;
        private String arrivalTime;
        private Integer duration;
        private String aircraftModel;
        private String operatingCarrier;
        private String marketingCarrier;
    }
    
    @Data
    public static class PassengerPricing {
        private String passengerType;
        private String totalAmount;
        private String totalCurrency;
        private String baseAmount;
        private String taxAmount;
    }
    
    @Data
    public static class Slice {
        private String origin;
        private String destination;
        private String departureDate;
    }
    
    @Data
    public static class Passenger {
        private String id;
        private String type;
    }
} 