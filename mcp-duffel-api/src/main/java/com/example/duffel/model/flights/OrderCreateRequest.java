package com.example.duffel.model.flights;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
public class OrderCreateRequest {
    @JsonProperty("type")
    private String type;
    @JsonProperty("offer_id")
    private String offerId;
    @JsonProperty("passengers")
    private List<Passenger> passengers;
    @JsonProperty("services")
    private Services services;
    @JsonProperty("payments")
    private PaymentDetails payments;
    @JsonProperty("selected_offers")
    private SelectedOffers selectedOffers;
    @JsonProperty("metadata")
    private Metadata metadata;
    
    @Data
    @Builder
    public static class Passenger {
        @JsonProperty("id")
        private String id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("given_name")
        private String givenName;
        @JsonProperty("family_name")
        private String familyName;
        @JsonProperty("gender")
        private String gender;
        @JsonProperty("born_on")
        private LocalDate bornOn;
        @JsonProperty("email")
        private String email;
        @JsonProperty("phone")
        private String phone;
        @JsonProperty("identity_documents")
        private List<IdentityDocument> identityDocuments;
        @JsonProperty("loyalty_programme_accounts")
        private List<LoyaltyProgrammeAccount> loyaltyProgrammeAccounts;
    }
    
    @Data
    @Builder
    public static class IdentityDocument {
        @JsonProperty("type")
        private String type;
        @JsonProperty("unique_identifier")
        private String uniqueIdentifier;
        @JsonProperty("issuing_country_code")
        private String issuingCountryCode;
        @JsonProperty("expires_on")
        private LocalDate expiresOn;
    }
    
    @Data
    @Builder
    public static class LoyaltyProgrammeAccount {
        @JsonProperty("airline_iata_code")
        private String airlineIataCode;
        @JsonProperty("account_number")
        private String accountNumber;
    }
    
    @Data
    @Builder
    public static class Services {
        @JsonProperty("ids")
        private List<String> ids;
    }
    
    @Data
    @Builder
    public static class PaymentDetails {
        @JsonProperty("type")
        private String type;
        @JsonProperty("amount")
        private String amount;
        @JsonProperty("currency")
        private String currency;
    }
    
    @Data
    @Builder
    public static class SelectedOffers {
        @JsonProperty("offer_ids")
        private List<String> offerIds;
    }
    
    @Data
    @Builder
    public static class Metadata {
        @JsonProperty("customer_reference")
        private String customerReference;
    }
} 