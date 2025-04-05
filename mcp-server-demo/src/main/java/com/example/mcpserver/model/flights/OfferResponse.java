package com.example.mcpserver.model.flights;

import java.util.List;
import lombok.Data;

@Data
public class OfferResponse {
    private Offer data;
    private List<Offer> offers;
    
    @Data
    public static class Offer {
        private String id;
        private String live_mode;
        private String created_at;
        private String updated_at;
        private String expires_at;
        private String total_amount;
        private String total_currency;
        private String tax_amount;
        private String tax_currency;
        private String base_amount;
        private String base_currency;
        private List<OfferSlice> slices;
        private List<PassengerPricing> passenger_pricings;
        private List<String> available_services;
        private Owner owner;
        private List<PrivateFare> private_fares;
        private PaymentRequirements payment_requirements;
        private Boolean passenger_identity_documents_required;
        private List<String> supported_passenger_identity_document_types;
        private List<String> supported_loyalty_programmes;
        private Conditions conditions;
        private Boolean partial;
        private String total_emissions_kg;
    }
    
    @Data
    public static class OfferSlice {
        private String id;
        private String origin_type;
        private Location origin;
        private String destination_type;
        private Location destination;
        private String duration;
        private List<Segment> segments;
        private String fare_brand_name;
        private Conditions conditions;
    }
    
    @Data
    public static class Location {
        private String type;
        private String time_zone;
        private String name;
        private Double longitude;
        private Double latitude;
        private String id;
        private String icao_code;
        private String iata_country_code;
        private String iata_code;
        private String iata_city_code;
        private String city_name;
        private City city;
    }
    
    @Data
    public static class City {
        private String name;
        private String id;
        private String iata_country_code;
        private String iata_code;
        private List<Airport> airports;
    }
    
    @Data
    public static class Airport {
        private String time_zone;
        private String name;
        private Double longitude;
        private Double latitude;
        private String id;
        private String icao_code;
        private String iata_country_code;
        private String iata_code;
    }
    
    @Data
    public static class Segment {
        private String id;
        private String departing_at;
        private String arriving_at;
        private Location origin;
        private Location destination;
        private String duration;
        private Aircraft aircraft;
        private Carrier operating_carrier;
        private Carrier marketing_carrier;
    }
    
    @Data
    public static class Aircraft {
        private String name;
        private String id;
        private String iata_code;
    }
    
    @Data
    public static class Carrier {
        private String name;
        private String id;
        private String iata_code;
    }
    
    @Data
    public static class PassengerPricing {
        private String passenger_id;
        private String fare_type;
        private String passenger_type;
        private String total_amount;
        private String total_currency;
        private String tax_amount;
        private String tax_currency;
        private String base_amount;
        private String base_currency;
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
    public static class PrivateFare {
        private String type;
        private String tracking_reference;
        private String tour_code;
        private String corporate_code;
    }
    
    @Data
    public static class PaymentRequirements {
        private Boolean requires_instant_payment;
        private String price_guarantee_expires_at;
        private String payment_required_by;
    }
    
    @Data
    public static class Conditions {
        private RefundOrChange refund_before_departure;
        private RefundOrChange change_before_departure;
        private String priority_boarding;
        private String priority_check_in;
        private String advance_seat_selection;
    }
    
    @Data
    public static class RefundOrChange {
        private Boolean allowed;
        private String penalty_amount;
        private String penalty_currency;
    }
} 