package com.example.mcpserver.model.flights;

import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class OfferResponse {
    @JsonProperty("data")
    private Offer data;
    @JsonProperty("offers")
    private List<Offer> offers;
    
    @Data
    public static class Offer {
        @JsonProperty("id")
        private String id;
        @JsonProperty("live_mode")
        private String liveMode;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("updated_at")
        private String updatedAt;
        @JsonProperty("expires_at")
        private String expiresAt;
        @JsonProperty("total_amount")
        private String totalAmount;
        @JsonProperty("total_currency")
        private String totalCurrency;
        @JsonProperty("tax_amount")
        private String taxAmount;
        @JsonProperty("tax_currency")
        private String taxCurrency;
        @JsonProperty("base_amount")
        private String baseAmount;
        @JsonProperty("base_currency")
        private String baseCurrency;
        @JsonProperty("slices")
        private List<OfferSlice> slices;
        @JsonProperty("passenger_pricings")
        private List<PassengerPricing> passengerPricings;
        @JsonProperty("available_services")
        private List<String> availableServices;
        @JsonProperty("owner")
        private Owner owner;
        @JsonProperty("private_fares")
        private List<PrivateFare> privateFares;
        @JsonProperty("payment_requirements")
        private PaymentRequirements paymentRequirements;
        @JsonProperty("passenger_identity_documents_required")
        private Boolean passengerIdentityDocumentsRequired;
        @JsonProperty("supported_passenger_identity_document_types")
        private List<String> supportedPassengerIdentityDocumentTypes;
        @JsonProperty("supported_loyalty_programmes")
        private List<String> supportedLoyaltyProgrammes;
        @JsonProperty("conditions")
        private Conditions conditions;
        @JsonProperty("partial")
        private Boolean partial;
        @JsonProperty("total_emissions_kg")
        private String totalEmissionsKg;
    }
    
    @Data
    public static class OfferSlice {
        @JsonProperty("id")
        private String id;
        @JsonProperty("origin_type")
        private String originType;
        @JsonProperty("origin")
        private Location origin;
        @JsonProperty("destination_type")
        private String destinationType;
        @JsonProperty("destination")
        private Location destination;
        @JsonProperty("duration")
        private String duration;
        @JsonProperty("segments")
        private List<Segment> segments;
        @JsonProperty("fare_brand_name")
        private String fareBrandName;
        @JsonProperty("conditions")
        private Conditions conditions;
    }
    
    @Data
    public static class Location {
        @JsonProperty("type")
        private String type;
        @JsonProperty("time_zone")
        private String timeZone;
        @JsonProperty("name")
        private String name;
        @JsonProperty("longitude")
        private Double longitude;
        @JsonProperty("latitude")
        private Double latitude;
        @JsonProperty("id")
        private String id;
        @JsonProperty("icao_code")
        private String icaoCode;
        @JsonProperty("iata_country_code")
        private String iataCountryCode;
        @JsonProperty("iata_code")
        private String iataCode;
        @JsonProperty("iata_city_code")
        private String iataCityCode;
        @JsonProperty("city_name")
        private String cityName;
        @JsonProperty("city")
        private City city;
    }
    
    @Data
    public static class City {
        @JsonProperty("name")
        private String name;
        @JsonProperty("id")
        private String id;
        @JsonProperty("iata_country_code")
        private String iataCountryCode;
        @JsonProperty("iata_code")
        private String iataCode;
        @JsonProperty("airports")
        private List<Airport> airports;
    }
    
    @Data
    public static class Airport {
        @JsonProperty("time_zone")
        private String timeZone;
        @JsonProperty("name")
        private String name;
        @JsonProperty("longitude")
        private Double longitude;
        @JsonProperty("latitude")
        private Double latitude;
        @JsonProperty("id")
        private String id;
        @JsonProperty("icao_code")
        private String icaoCode;
        @JsonProperty("iata_country_code")
        private String iataCountryCode;
        @JsonProperty("iata_code")
        private String iataCode;
    }
    
    @Data
    public static class Segment {
        @JsonProperty("id")
        private String id;
        @JsonProperty("departing_at")
        private String departingAt;
        @JsonProperty("arriving_at")
        private String arrivingAt;
        @JsonProperty("origin")
        private Location origin;
        @JsonProperty("destination")
        private Location destination;
        @JsonProperty("duration")
        private String duration;
        @JsonProperty("aircraft")
        private Aircraft aircraft;
        @JsonProperty("operating_carrier")
        private Carrier operatingCarrier;
        @JsonProperty("marketing_carrier")
        private Carrier marketingCarrier;
    }
    
    @Data
    public static class Aircraft {
        @JsonProperty("name")
        private String name;
        @JsonProperty("id")
        private String id;
        @JsonProperty("iata_code")
        private String iataCode;
    }
    
    @Data
    public static class Carrier {
        @JsonProperty("name")
        private String name;
        @JsonProperty("id")
        private String id;
        @JsonProperty("iata_code")
        private String iataCode;
    }
    
    @Data
    public static class PassengerPricing {
        @JsonProperty("passenger_id")
        private String passengerId;
        @JsonProperty("fare_type")
        private String fareType;
        @JsonProperty("passenger_type")
        private String passengerType;
        @JsonProperty("total_amount")
        private String totalAmount;
        @JsonProperty("total_currency")
        private String totalCurrency;
        @JsonProperty("tax_amount")
        private String taxAmount;
        @JsonProperty("tax_currency")
        private String taxCurrency;
        @JsonProperty("base_amount")
        private String baseAmount;
        @JsonProperty("base_currency")
        private String baseCurrency;
    }
    
    @Data
    public static class Owner {
        @JsonProperty("name")
        private String name;
        @JsonProperty("id")
        private String id;
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
    public static class PrivateFare {
        @JsonProperty("type")
        private String type;
        @JsonProperty("tracking_reference")
        private String trackingReference;
        @JsonProperty("tour_code")
        private String tourCode;
        @JsonProperty("corporate_code")
        private String corporateCode;
    }
    
    @Data
    public static class PaymentRequirements {
        @JsonProperty("requires_instant_payment")
        private Boolean requiresInstantPayment;
        @JsonProperty("price_guarantee_expires_at")
        private String priceGuaranteeExpiresAt;
        @JsonProperty("payment_required_by")
        private String paymentRequiredBy;
    }
    
    @Data
    public static class Conditions {
        @JsonProperty("refund_before_departure")
        private RefundOrChange refundBeforeDeparture;
        @JsonProperty("change_before_departure")
        private RefundOrChange changeBeforeDeparture;
        @JsonProperty("priority_boarding")
        private String priorityBoarding;
        @JsonProperty("priority_check_in")
        private String priorityCheckIn;
        @JsonProperty("advance_seat_selection")
        private String advanceSeatSelection;
    }
    
    @Data
    public static class RefundOrChange {
        @JsonProperty("allowed")
        private Boolean allowed;
        @JsonProperty("penalty_amount")
        private String penaltyAmount;
        @JsonProperty("penalty_currency")
        private String penaltyCurrency;
    }
} 