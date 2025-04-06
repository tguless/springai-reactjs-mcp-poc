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
public class AirlineInitiatedChangeResponse {
    
    @JsonProperty("data")
    private AirlineInitiatedChange data;
    
    @JsonProperty("meta")
    private MetaData meta;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AirlineInitiatedChange {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("order_id")
        private String orderId;
        
        @JsonProperty("created_at")
        private OffsetDateTime createdAt;
        
        @JsonProperty("updated_at")
        private OffsetDateTime updatedAt;
        
        @JsonProperty("action_taken")
        private String actionTaken; // accepted, cancelled, changed
        
        @JsonProperty("action_taken_at")
        private OffsetDateTime actionTakenAt;
        
        @JsonProperty("available_actions")
        private List<String> availableActions;
        
        @JsonProperty("added")
        private List<Slice> added;
        
        @JsonProperty("removed")
        private List<Slice> removed;
        
        @JsonProperty("travel_agent_ticket")
        private TravelAgentTicket travelAgentTicket;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Slice {
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
        
        @JsonProperty("duration")
        private String duration;
        
        @JsonProperty("fare_brand_name")
        private String fareBrandName;
        
        @JsonProperty("segments")
        private List<Segment> segments;
        
        @JsonProperty("conditions")
        private SliceConditions conditions;
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
        
        @JsonProperty("iata_city_code")
        private String iataCityCode;
        
        @JsonProperty("icao_code")
        private String icaoCode;
        
        @JsonProperty("city_name")
        private String cityName;
        
        @JsonProperty("city")
        private City city;
        
        @JsonProperty("time_zone")
        private String timeZone;
        
        @JsonProperty("latitude")
        private Double latitude;
        
        @JsonProperty("longitude")
        private Double longitude;
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
        
        @JsonProperty("marketing_carrier_flight_number")
        private String marketingCarrierFlightNumber;
        
        @JsonProperty("operating_carrier_flight_number")
        private String operatingCarrierFlightNumber;
        
        @JsonProperty("duration")
        private String duration;
        
        @JsonProperty("distance")
        private String distance;
        
        @JsonProperty("aircraft")
        private Aircraft aircraft;
        
        @JsonProperty("origin_terminal")
        private String originTerminal;
        
        @JsonProperty("destination_terminal")
        private String destinationTerminal;
        
        @JsonProperty("stops")
        private List<Stop> stops;
        
        @JsonProperty("passengers")
        private List<SegmentPassenger> passengers;
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
        
        @JsonProperty("logo_symbol_url")
        private String logoSymbolUrl;
        
        @JsonProperty("logo_lockup_url")
        private String logoLockupUrl;
        
        @JsonProperty("conditions_of_carriage_url")
        private String conditionsOfCarriageUrl;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Aircraft {
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
    public static class Stop {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("arriving_at")
        private String arrivingAt;
        
        @JsonProperty("departing_at")
        private String departingAt;
        
        @JsonProperty("duration")
        private String duration;
        
        @JsonProperty("airport")
        private Place airport;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SegmentPassenger {
        @JsonProperty("passenger_id")
        private String passengerId;
        
        @JsonProperty("cabin_class")
        private String cabinClass;
        
        @JsonProperty("cabin_class_marketing_name")
        private String cabinClassMarketingName;
        
        @JsonProperty("seat")
        private Seat seat;
        
        @JsonProperty("baggages")
        private List<Baggage> baggages;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Seat {
        @JsonProperty("designator")
        private String designator;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("disclosures")
        private List<String> disclosures;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Baggage {
        @JsonProperty("type")
        private String type;
        
        @JsonProperty("quantity")
        private Integer quantity;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SliceConditions {
        @JsonProperty("change_before_departure")
        private ChangeCondition changeBeforeDeparture;
        
        @JsonProperty("refund_before_departure")
        private RefundCondition refundBeforeDeparture;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChangeCondition {
        @JsonProperty("allowed")
        private Boolean allowed;
        
        @JsonProperty("penalty_amount")
        private String penaltyAmount;
        
        @JsonProperty("penalty_currency")
        private String penaltyCurrency;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RefundCondition {
        @JsonProperty("allowed")
        private Boolean allowed;
        
        @JsonProperty("penalty_amount")
        private String penaltyAmount;
        
        @JsonProperty("penalty_currency")
        private String penaltyCurrency;
    }
    
    @Data
    @Builder
    //@NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TravelAgentTicket {
        // This is a placeholder for the preview feature mentioned in the docs
        // Actual fields can be added when the feature is fully released
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MetaData {
        @JsonProperty("before")
        private String before;
        
        @JsonProperty("after")
        private String after;
    }
} 