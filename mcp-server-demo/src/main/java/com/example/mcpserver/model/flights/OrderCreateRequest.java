package com.example.mcpserver.model.flights;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCreateRequest {
    private String type;
    private String offerId;
    private List<Passenger> passengers;
    private Services services;
    private PaymentDetails payments;
    private SelectedOffers selectedOffers;
    private Metadata metadata;
    
    @Data
    @Builder
    public static class Passenger {
        private String id;
        private String title;
        private String givenName;
        private String familyName;
        private String gender;
        private LocalDate bornOn;
        private String email;
        private String phone;
        private List<IdentityDocument> identityDocuments;
        private List<LoyaltyProgrammeAccount> loyaltyProgrammeAccounts;
    }
    
    @Data
    @Builder
    public static class IdentityDocument {
        private String type;
        private String uniqueIdentifier;
        private String issuingCountryCode;
        private LocalDate expiresOn;
    }
    
    @Data
    @Builder
    public static class LoyaltyProgrammeAccount {
        private String airlineIataCode;
        private String accountNumber;
    }
    
    @Data
    @Builder
    public static class Services {
        private List<String> ids;
    }
    
    @Data
    @Builder
    public static class PaymentDetails {
        private String type;
        private String amount;
        private String currency;
    }
    
    @Data
    @Builder
    public static class SelectedOffers {
        private List<String> offerIds;
    }
    
    @Data
    @Builder
    public static class Metadata {
        private String customerReference;
    }
} 