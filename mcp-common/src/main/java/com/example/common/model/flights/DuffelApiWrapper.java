package com.example.common.model.flights;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper class for Duffel API requests and responses.
 * Duffel API expects requests to be wrapped in a "data" object.
 * @param <T> Type of the data being wrapped
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DuffelApiWrapper<T> {
    
    @JsonProperty("data")
    private T data;
} 