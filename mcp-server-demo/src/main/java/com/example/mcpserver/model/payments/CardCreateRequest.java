package com.example.mcpserver.model.payments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardCreateRequest {
    
    @JsonProperty("number")
    private String number;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("expiry_month")
    private String expiryMonth;
    
    @JsonProperty("expiry_year")
    private String expiryYear;
    
    @JsonProperty("cvc")
    private String cvc;
    
    @JsonProperty("address_line_1")
    private String addressLine1;
    
    @JsonProperty("address_line_2")
    private String addressLine2;
    
    @JsonProperty("address_city")
    private String addressCity;
    
    @JsonProperty("address_region")
    private String addressRegion;
    
    @JsonProperty("address_postal_code")
    private String addressPostalCode;
    
    @JsonProperty("address_country_code")
    private String addressCountryCode;
    
    @JsonProperty("multi_use")
    private Boolean multiUse;
    
    @JsonProperty("card_id")
    private String cardId;
} 