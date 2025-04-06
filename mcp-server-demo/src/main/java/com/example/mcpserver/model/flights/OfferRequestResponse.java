package com.example.mcpserver.model.flights;

import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferRequestResponse {
    @JsonProperty("data")
    private OfferRequestData data;
    
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OfferRequestData {
        @JsonProperty("id")
        private String id;
        @JsonProperty("live_mode")
        private String liveMode;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("offers")
        private List<Offer> offers;
        @JsonProperty("slices")
        private List<Slice> slices;
        @JsonProperty("passengers")
        private List<Passenger> passengers;
        @JsonProperty("cabin_class")
        private String cabinClass;
    }
    
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Offer {
        @JsonProperty("id")
        private String id;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("updated_at")
        private String updatedAt;
        @JsonProperty("live_mode_key")
        private String liveModeKey;
        @JsonProperty("total_amount")
        private String totalAmount;
        @JsonProperty("total_currency")
        private String totalCurrency;
        @JsonProperty("slices")
        private List<OfferSlice> slices;
        @JsonProperty("passenger_pricings")
        private List<PassengerPricing> passengerPricings;
        @JsonProperty("available_services")
        private List<String> availableServices;
        @JsonProperty("tax")
        private String tax;
    }
    
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OfferSlice {
        @JsonProperty("id")
        private String id;
        @JsonProperty("origin")
        private Airport origin;
        @JsonProperty("destination")
        private Airport destination;
        @JsonProperty("departure_date")
        private String departureDate;
        @JsonProperty("arrival_date")
        private String arrivalDate;
        @JsonProperty("duration")
        private String duration;
        @JsonProperty("segments")
        private List<Segment> segments;
    }
    
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Segment {
        @JsonProperty("id")
        private String id;
        @JsonProperty("departure_airport")
        private Airport departureAirport;
        @JsonProperty("arrival_airport")
        private Airport arrivalAirport;
        @JsonProperty("departure_time")
        private String departureTime;
        @JsonProperty("arrival_time")
        private String arrivalTime;
        @JsonProperty("duration")
        private String duration;
        @JsonProperty("aircraft_model")
        private String aircraftModel;
        @JsonProperty("operating_carrier")
        private Airline operatingCarrier;
        @JsonProperty("marketing_carrier")
        private Airline marketingCarrier;
    }
    
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PassengerPricing {
        @JsonProperty("passenger_type")
        private String passengerType;
        @JsonProperty("total_amount")
        private String totalAmount;
        @JsonProperty("total_currency")
        private String totalCurrency;
        @JsonProperty("base_amount")
        private String baseAmount;
        @JsonProperty("tax_amount")
        private String taxAmount;
    }
    
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Slice {
        @JsonProperty("origin")
        private Place origin;
        @JsonProperty("destination")
        private Place destination;
        @JsonProperty("departure_date")
        private String departureDate;
    }
    
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Passenger {
        @JsonProperty("id")
        private String id;
        @JsonProperty("type")
        private String type;
    }
} 