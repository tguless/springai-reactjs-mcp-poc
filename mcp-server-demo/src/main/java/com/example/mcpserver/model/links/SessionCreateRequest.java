package com.example.mcpserver.model.links;

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
public class SessionCreateRequest {
    
    @JsonProperty("reference")
    private String reference;
    
    @JsonProperty("success_url")
    private String successUrl;
    
    @JsonProperty("failure_url")
    private String failureUrl;
    
    @JsonProperty("abandonment_url")
    private String abandonmentUrl;
    
    @JsonProperty("logo_url")
    private String logoUrl;
    
    @JsonProperty("primary_color")
    private String primaryColor;
    
    @JsonProperty("secondary_color")
    private String secondaryColor;
    
    @JsonProperty("checkout_display_text")
    private String checkoutDisplayText;
    
    @JsonProperty("traveller_currency")
    private String travellerCurrency;
    
    @JsonProperty("should_hide_traveller_currency_selector")
    private Boolean shouldHideTravellerCurrencySelector;
    
    @JsonProperty("markup_amount")
    private String markupAmount;
    
    @JsonProperty("markup_currency")
    private String markupCurrency;
    
    @JsonProperty("markup_rate")
    private String markupRate;
    
    @JsonProperty("flights")
    private FlightsConfig flights;
    
    @JsonProperty("stays")
    private StaysConfig stays;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FlightsConfig {
        @JsonProperty("enabled")
        private String enabled;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StaysConfig {
        @JsonProperty("enabled")
        private String enabled;
    }
} 