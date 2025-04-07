package com.example.common.model.flights;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Response model for order change request operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderChangeRequestResponse {
    
    private OrderChangeRequest data;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderChangeRequest {
        
        private String id;
        
        @JsonProperty("live_mode")
        private Boolean liveMode;
        
        @JsonProperty("order_id")
        private String orderId;
        
        private Slices slices;
        
        @JsonProperty("created_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private ZonedDateTime createdAt;
        
        @JsonProperty("updated_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private ZonedDateTime updatedAt;
        
        @JsonProperty("order_change_offers")
        private List<OrderChangeOffer> orderChangeOffers;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Slices {
        private List<RemoveSlice> remove;
        private List<AddSlice> add;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemoveSlice {
        @JsonProperty("slice_id")
        private String sliceId;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddSlice {
        private String origin;
        private String destination;
        
        @JsonProperty("departure_date")
        private LocalDate departureDate;
        
        @JsonProperty("cabin_class")
        private String cabinClass;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderChangeOffer {
        
        private String id;
        
        @JsonProperty("live_mode")
        private Boolean liveMode;
        
        @JsonProperty("created_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private ZonedDateTime createdAt;
        
        @JsonProperty("updated_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private ZonedDateTime updatedAt;
        
        @JsonProperty("expires_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private ZonedDateTime expiresAt;
        
        @JsonProperty("change_total_amount")
        private String changeTotalAmount;
        
        @JsonProperty("change_total_currency")
        private String changeTotalCurrency;
        
        @JsonProperty("new_total_amount")
        private String newTotalAmount;
        
        @JsonProperty("new_total_currency")
        private String newTotalCurrency;
        
        @JsonProperty("penalty_total_amount")
        private String penaltyTotalAmount;
        
        @JsonProperty("penalty_total_currency")
        private String penaltyTotalCurrency;
        
        private OfferSlices slices;
        
        private Conditions conditions;
        
        @JsonProperty("order_change_id")
        private String orderChangeId;
        
        @JsonProperty("private_fares")
        private List<PrivateFare> privateFares;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfferSlices {
        private List<DetailedSlice> remove;
        // The add field would contain complex slice structures with segments
        // For simplicity, we're using Object type here
        private List<Object> add;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailedSlice {
        private String id;
        
        @JsonProperty("origin_type")
        private String originType;
        
        @JsonProperty("destination_type")
        private String destinationType;
        
        private Object origin;
        
        private Object destination;
        
        private String duration;
        
        @JsonProperty("fare_brand_name")
        private String fareBrandName;
        
        private List<Segment> segments;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Segment {
        private String id;
        
        @JsonProperty("departing_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private ZonedDateTime departingAt;
        
        @JsonProperty("arriving_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private ZonedDateTime arrivingAt;
        
        private String duration;
        
        private String distance;
        
        private Object origin;
        
        private Object destination;
        
        @JsonProperty("origin_terminal")
        private String originTerminal;
        
        @JsonProperty("destination_terminal")
        private String destinationTerminal;
        
        private Aircraft aircraft;
        
        @JsonProperty("marketing_carrier")
        private Carrier marketingCarrier;
        
        @JsonProperty("marketing_carrier_flight_number")
        private String marketingCarrierFlightNumber;
        
        @JsonProperty("operating_carrier")
        private Carrier operatingCarrier;
        
        @JsonProperty("operating_carrier_flight_number")
        private String operatingCarrierFlightNumber;
        
        private List<Passenger> passengers;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Aircraft {
        private String id;
        
        @JsonProperty("iata_code")
        private String iataCode;
        
        private String name;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Carrier {
        private String id;
        
        @JsonProperty("iata_code")
        private String iataCode;
        
        private String name;
        
        @JsonProperty("logo_symbol_url")
        private String logoSymbolUrl;
        
        @JsonProperty("logo_lockup_url")
        private String logoLockupUrl;
        
        @JsonProperty("conditions_of_carriage_url")
        private String conditionsOfCarriageUrl;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Passenger {
        @JsonProperty("passenger_id")
        private String passengerId;
        
        @JsonProperty("cabin_class")
        private String cabinClass;
        
        @JsonProperty("cabin_class_marketing_name")
        private String cabinClassMarketingName;
        
        private Seat seat;
        
        private List<Baggage> baggages;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Seat {
        private String designator;
        
        private String name;
        
        private List<String> disclosures;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Baggage {
        private String type;
        
        private Integer quantity;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Conditions {
        @JsonProperty("refund_before_departure")
        private RefundBeforeDeparture refundBeforeDeparture;
        
        @JsonProperty("change_before_departure")
        private ChangeBeforeDeparture changeBeforeDeparture;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefundBeforeDeparture {
        private Boolean allowed;
        
        @JsonProperty("penalty_amount")
        private String penaltyAmount;
        
        @JsonProperty("penalty_currency")
        private String penaltyCurrency;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeBeforeDeparture {
        private Boolean allowed;
        
        @JsonProperty("penalty_amount")
        private String penaltyAmount;
        
        @JsonProperty("penalty_currency")
        private String penaltyCurrency;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrivateFare {
        private String type;
        
        @JsonProperty("corporate_code")
        private String corporateCode;
        
        @JsonProperty("tour_code")
        private String tourCode;
        
        @JsonProperty("tracking_reference")
        private String trackingReference;
    }
} 