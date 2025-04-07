package com.example.common.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request model for updating a Customer User Group in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUserGroupUpdateRequest {
    private String name;
    
    @JsonProperty("user_ids")
    private List<String> userIds;
} 