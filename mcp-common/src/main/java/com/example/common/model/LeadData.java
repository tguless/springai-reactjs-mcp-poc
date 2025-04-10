package com.example.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents sales lead data that can be shared between client and server applications.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadData {
    private String id;
    private String companyName;
    private String contactName;
    private String email;
    private String phone;
    private String industry;
    private String productInterest;
    private String sourceChannel;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 