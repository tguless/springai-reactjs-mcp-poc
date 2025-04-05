package com.example.mcpserver.model.geocoding;

import lombok.Data;

@Data
public class GeocodingLocation {
    private String name;
    private Double lat;
    private Double lon;
    private String country;
    private String state;
    private LocalNames local_names;
}

@Data
class LocalNames {
    private String en;
    private String ascii;
    private String feature_name;
    // Other local names could be added as needed
} 