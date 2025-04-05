package com.example.mcpserver.model.flights;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class OrderResponse {
    private Order data;
    private List<Order> orders;
    
    @Data
    public static class Order {
        private String id;
        private Boolean live_mode;
        private String created_at;
        private String synced_at;
        private String type;
        private List<String> available_actions;
        private String booking_reference;
        private String cancelled_at;
        private String total_amount;
        private String total_currency;
        private String tax_amount;
        private String tax_currency;
        private String base_amount;
        private String base_currency;
        private List<Service> services;
        private List<OfferResponse.OfferSlice> slices;
        private List<Passenger> passengers;
        private Owner owner;
        private PaymentStatus payment_status;
        private List<Document> documents;
        private Conditions conditions;
        private Map<String, Object> metadata;
        private String offer_id;
        private String content;
        private String void_window_ends_at;
    }
    
    @Data
    public static class Service {
        private String id;
        private String service_type;
        private String status;
        private String total_amount;
        private String total_currency;
    }
    
    @Data
    public static class Passenger {
        private String id;
        private String given_name;
        private String family_name;
        private String gender;
        private String type;
        private String title;
        private String born_on;
        private String email;
        private String phone;
        private List<IdentityDocument> identity_documents;
        private List<LoyaltyProgrammeAccount> loyalty_programme_accounts;
    }
    
    @Data
    public static class IdentityDocument {
        private String unique_identifier;
        private String expires_on;
        private String issuing_country_code;
        private String type;
    }
    
    @Data
    public static class LoyaltyProgrammeAccount {
        private String airline_iata_code;
        private String account_number;
    }
    
    @Data
    public static class Owner {
        private String name;
        private String id;
        private String iata_code;
        private String logo_symbol_url;
        private String logo_lockup_url;
        private String conditions_of_carriage_url;
    }
    
    @Data
    public static class PaymentStatus {
        private String awaiting_payment;
        private String payment_required_by;
        private String price_guarantee_expires_at;
    }
    
    @Data
    public static class Document {
        private String id;
        private String type;
        private String url;
        private List<String> passenger_ids;
    }
    
    @Data
    public static class Conditions {
        private RefundOrChange refund_before_departure;
        private RefundOrChange change_before_departure;
    }
    
    @Data
    public static class RefundOrChange {
        private Boolean allowed;
        private String penalty_amount;
        private String penalty_currency;
    }
} 