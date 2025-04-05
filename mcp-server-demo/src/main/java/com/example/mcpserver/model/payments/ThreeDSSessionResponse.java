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
public class ThreeDSSessionResponse {
    
    @JsonProperty("data")
    private ThreeDSSession data;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ThreeDSSession {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("live_mode")
        private Boolean liveMode;
        
        @JsonProperty("card_id")
        private String cardId;
        
        @JsonProperty("resource_id")
        private String resourceId;
        
        @JsonProperty("status")
        private String status;
        
        @JsonProperty("client_id")
        private String clientId;
    }
} 