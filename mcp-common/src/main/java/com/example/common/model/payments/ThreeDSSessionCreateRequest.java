package com.example.common.model.payments;

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
public class ThreeDSSessionCreateRequest {
    
    @JsonProperty("card_id")
    private String cardId;
    
    @JsonProperty("resource_id")
    private String resourceId;
    
    @JsonProperty("exception")
    private String exception;
    
    @JsonProperty("services")
    private List<Service> services;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Service {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("quantity")
        private Integer quantity;
    }
} 