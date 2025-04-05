package com.example.mcpserver.model.flights;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartialOfferRequestCreate {
    
    @JsonProperty("cabin_class")
    private String cabinClass;
    
    @JsonProperty("passengers")
    private List<Passenger> passengers;
    
    @JsonProperty("slices")
    private List<SliceInput> slices;
    
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
        
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("age")
        private Integer age;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SliceInput {
        @JsonProperty("departure_date")
        private String departureDate;
        
        @JsonProperty("destination")
        private String destination;
        
        @JsonProperty("destination_type")
        private String destinationType; // airport or city
        
        @JsonProperty("origin")
        private String origin;
        
        @JsonProperty("origin_type")
        private String originType; // airport or city
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