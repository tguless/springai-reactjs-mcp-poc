package com.example.duffel.model.flights;

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
public class BatchOfferRequestResponse {
    
    @JsonProperty("data")
    private BatchOfferRequest data;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BatchOfferRequest {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("live_mode")
        private Boolean liveMode;
        
        @JsonProperty("created_at")
        private OffsetDateTime createdAt;
        
        @JsonProperty("client_key")
        private String clientKey;
        
        @JsonProperty("total_batches")
        private Integer totalBatches;
        
        @JsonProperty("remaining_batches")
        private Integer remainingBatches;
        
        // When retrieving a batch of offers
        @JsonProperty("offers")
        private List<Offer> offers;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Offer {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("live_mode")
        private Boolean liveMode;
        
        @JsonProperty("created_at")
        private OffsetDateTime createdAt;
        
        @JsonProperty("expires_at")
        private OffsetDateTime expiresAt;
        
        @JsonProperty("owner")
        private Airline owner;
        
        @JsonProperty("base_amount")
        private String baseAmount;
        
        @JsonProperty("base_currency")
        private String baseCurrency;
        
        @JsonProperty("passenger_identity_documents_required")
        private Boolean passengerIdentityDocumentsRequired;
        
        @JsonProperty("slices")
        private List<Slice> slices;
        
        @JsonProperty("passengers")
        private List<Passenger> passengers;
        
        @JsonProperty("payment_requirements")
        private PaymentRequirements paymentRequirements;
        
        @JsonProperty("conditions")
        private OfferConditions conditions;
        
        @JsonProperty("private_fares")
        private List<PrivateFare> privateFares;
        
        @JsonProperty("partial")
        private Boolean partial;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Airline {
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
        private Airline marketingCarrier;
        
        @JsonProperty("operating_carrier")
        private Airline operatingCarrier;
        
        @JsonProperty("marketing_carrier_flight_number")
        private String marketingCarrierFlightNumber;
        
        @JsonProperty("operating_carrier_flight_number")
        private String operatingCarrierFlightNumber;
        
        @JsonProperty("duration")
        private String duration;
        
        @JsonProperty("aircraft")
        private Aircraft aircraft;
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
        
        @JsonProperty("fare_type")
        private String fareType;
        
        @JsonProperty("loyalty_programme_accounts")
        private List<LoyaltyProgrammeAccount> loyaltyProgrammeAccounts;
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
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PaymentRequirements {
        @JsonProperty("requires_instant_payment")
        private Boolean requiresInstantPayment;
        
        @JsonProperty("price_guarantee_expires_at")
        private String priceGuaranteeExpiresAt;
        
        @JsonProperty("payment_required_by")
        private String paymentRequiredBy;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OfferConditions {
        @JsonProperty("refund_before_departure")
        private RefundCondition refundBeforeDeparture;
        
        @JsonProperty("change_before_departure")
        private ChangeCondition changeBeforeDeparture;
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
        
        @JsonProperty("advance_seat_selection")
        private String advanceSeatSelection;
        
        @JsonProperty("priority_boarding")
        private String priorityBoarding;
        
        @JsonProperty("priority_check_in")
        private String priorityCheckIn;
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
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PrivateFare {
        @JsonProperty("type")
        private String type;
        
        @JsonProperty("corporate_code")
        private String corporateCode;
        
        @JsonProperty("tracking_reference")
        private String trackingReference;
        
        @JsonProperty("tour_code")
        private String tourCode;
    }
} 