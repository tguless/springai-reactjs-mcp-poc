package com.example.duffel.model.flights;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchOfferRequestCreate {

    @JsonProperty("cabin_class")
    private String cabinClass;

    @JsonProperty("max_connections")
    private Integer maxConnections;

    @JsonProperty("passengers")
    private List<Passenger> passengers;

    @JsonProperty("slices")
    private List<SliceInput> slices;

    @JsonProperty("private_fares")
    private Map<String, List<PrivateFare>> privateFares;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Passenger {
        @JsonProperty("family_name")
        private String familyName;

        @JsonProperty("given_name")
        private String givenName;

        @JsonProperty("type")
        private String type; // adult, child, infant_without_seat, infant_with_seat

        @JsonProperty("loyalty_programme_accounts")
        private List<LoyaltyProgrammeAccount> loyaltyProgrammeAccounts;

        @JsonProperty("age")
        private Integer age;

        @JsonProperty("fare_type")
        private String fareType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SliceInput {
        @JsonProperty("origin")
        private String origin;

        @JsonProperty("destination")
        private String destination;

        @JsonProperty("departure_date")
        private String departureDate;

        // References the top-level TimeRange class now
        @JsonProperty("departure_time")
        private TimeRange departureTime;

        @JsonProperty("arrival_time")
        private TimeRange arrivalTime;
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
    public static class PrivateFare {
        @JsonProperty("corporate_code")
        private String corporateCode;

        @JsonProperty("tracking_reference")
        private String trackingReference;

        @JsonProperty("tour_code")
        private String tourCode;
    }
}
