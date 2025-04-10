package com.example.duffel.model.flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model representing a Customer User Group in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUserGroup {
    private String id;
    private String name;
    
    @JsonProperty("user_ids")
    private List<String> userIds;
} 