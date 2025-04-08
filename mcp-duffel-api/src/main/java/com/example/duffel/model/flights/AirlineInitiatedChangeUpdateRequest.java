package com.example.duffel.model.flights;

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
public class AirlineInitiatedChangeUpdateRequest {
    
    @JsonProperty("action_taken")
    private String actionTaken; // Values: "accepted", "cancelled", or "changed"
} 