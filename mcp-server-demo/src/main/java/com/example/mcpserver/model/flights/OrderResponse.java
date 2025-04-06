package com.example.mcpserver.model.flights;

import java.util.List;
import java.util.Map;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class OrderResponse {
    @JsonProperty("data")
    private Order data;
    @JsonProperty("orders")
    private List<Order> orders;
    
    @Data
    public static class Order {
        @JsonProperty("id")
        private String id;
        @JsonProperty("live_mode")
        private Boolean liveMode;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("synced_at")
        private String syncedAt;
        @JsonProperty("type")
        private String type;
        @JsonProperty("available_actions")
        private List<String> availableActions;
        @JsonProperty("booking_reference")
        private String bookingReference;
        @JsonProperty("cancelled_at")
        private String cancelledAt;
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
        @JsonProperty("services")
        private List<Service> services;
        @JsonProperty("slices")
        private List<OfferResponse.OfferSlice> slices;
        @JsonProperty("passengers")
        private List<Passenger> passengers;
        @JsonProperty("owner")
        private Owner owner;
        @JsonProperty("payment_status")
        private PaymentStatus paymentStatus;
        @JsonProperty("documents")
        private List<Document> documents;
        @JsonProperty("conditions")
        private Conditions conditions;
        @JsonProperty("metadata")
        private Map<String, Object> metadata;
        @JsonProperty("offer_id")
        private String offerId;
        @JsonProperty("content")
        private String content;
        @JsonProperty("void_window_ends_at")
        private String voidWindowEndsAt;
    }
    
    @Data
    public static class Service {
        @JsonProperty("id")
        private String id;
        @JsonProperty("service_type")
        private String serviceType;
        @JsonProperty("status")
        private String status;
        @JsonProperty("total_amount")
        private String totalAmount;
        @JsonProperty("total_currency")
        private String totalCurrency;
    }
    
    @Data
    public static class Passenger {
        @JsonProperty("id")
        private String id;
        @JsonProperty("given_name")
        private String givenName;
        @JsonProperty("family_name")
        private String familyName;
        @JsonProperty("gender")
        private String gender;
        @JsonProperty("type")
        private String type;
        @JsonProperty("title")
        private String title;
        @JsonProperty("born_on")
        private String bornOn;
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
    public static class IdentityDocument {
        @JsonProperty("unique_identifier")
        private String uniqueIdentifier;
        @JsonProperty("expires_on")
        private String expiresOn;
        @JsonProperty("issuing_country_code")
        private String issuingCountryCode;
        @JsonProperty("type")
        private String type;
    }
    
    @Data
    public static class LoyaltyProgrammeAccount {
        @JsonProperty("airline_iata_code")
        private String airlineIataCode;
        @JsonProperty("account_number")
        private String accountNumber;
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
    public static class PaymentStatus {
        @JsonProperty("awaiting_payment")
        private String awaitingPayment;
        @JsonProperty("payment_required_by")
        private String paymentRequiredBy;
        @JsonProperty("price_guarantee_expires_at")
        private String priceGuaranteeExpiresAt;
    }
    
    @Data
    public static class Document {
        @JsonProperty("id")
        private String id;
        @JsonProperty("type")
        private String type;
        @JsonProperty("url")
        private String url;
        @JsonProperty("passenger_ids")
        private List<String> passengerIds;
    }
    
    @Data
    public static class Conditions {
        @JsonProperty("refund_before_departure")
        private RefundOrChange refundBeforeDeparture;
        @JsonProperty("change_before_departure")
        private RefundOrChange changeBeforeDeparture;
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