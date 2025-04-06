package com.example.mcpserver.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for updating a Customer User in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUserUpdateRequest {
    private String email;
    
    @JsonProperty("given_name")
    private String givenName;
    
    @JsonProperty("family_name")
    private String familyName;
    
    @JsonProperty("phone_number")
    private String phoneNumber;
    
    @JsonProperty("group_id")
    private String groupId;
} 