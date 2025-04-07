package com.example.common.model.flights;

import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class SeatMapResponse {
    @JsonProperty("data")
    private List<SeatMap> data;
    
    @Data
    public static class SeatMap {
        @JsonProperty("id")
        private String id;
        @JsonProperty("segment_id")
        private String segment_id;
        @JsonProperty("slice_id")
        private String slice_id;
        @JsonProperty("cabins")
        private List<Cabin> cabins;
    }
    
    @Data
    public static class Cabin {
        @JsonProperty("cabin_class")
        private String cabin_class;
        @JsonProperty("deck")
        private Integer deck;
        @JsonProperty("aisles")
        private Integer aisles;
        @JsonProperty("rows")
        private List<Row> rows;
        @JsonProperty("wings")
        private Wings wings;
    }
    
    @Data
    public static class Wings {
        @JsonProperty("first_row_index")
        private Integer first_row_index;
        @JsonProperty("last_row_index")
        private Integer last_row_index;
    }
    
    @Data
    public static class Row {
        @JsonProperty("sections")
        private List<Section> sections;
    }
    
    @Data
    public static class Section {
        @JsonProperty("elements")
        private List<Element> elements;
    }
    
    @Data
    public static class Element {
        @JsonProperty("type")
        private String type;
        @JsonProperty("designator")
        private String designator;
        @JsonProperty("name")
        private String name;
        @JsonProperty("disclosures")
        private List<String> disclosures;
        @JsonProperty("available_services")
        private List<AvailableService> available_services;
    }
    
    @Data
    public static class AvailableService {
        @JsonProperty("id")
        private String id;
        @JsonProperty("passenger_id")
        private String passenger_id;
        @JsonProperty("total_amount")
        private String total_amount;
        @JsonProperty("total_currency")
        private String total_currency;
    }
} 