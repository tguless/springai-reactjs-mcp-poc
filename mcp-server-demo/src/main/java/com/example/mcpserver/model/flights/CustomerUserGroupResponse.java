package com.example.mcpserver.model.flights;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response model for Customer User Group operations in the Duffel API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUserGroupResponse {
    private CustomerUserGroup data;
    private List<CustomerUserGroup> data_list;
} 