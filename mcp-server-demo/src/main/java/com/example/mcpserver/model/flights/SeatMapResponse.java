package com.example.mcpserver.model.flights;

import java.util.List;
import lombok.Data;

@Data
public class SeatMapResponse {
    private List<SeatMap> data;
    
    @Data
    public static class SeatMap {
        private String id;
        private String segment_id;
        private String slice_id;
        private List<Cabin> cabins;
    }
    
    @Data
    public static class Cabin {
        private String cabin_class;
        private Integer deck;
        private Integer aisles;
        private List<Row> rows;
        private Wings wings;
    }
    
    @Data
    public static class Wings {
        private Integer first_row_index;
        private Integer last_row_index;
    }
    
    @Data
    public static class Row {
        private List<Section> sections;
    }
    
    @Data
    public static class Section {
        private List<Element> elements;
    }
    
    @Data
    public static class Element {
        private String type;
        private String designator;
        private String name;
        private List<String> disclosures;
        private List<AvailableService> available_services;
    }
    
    @Data
    public static class AvailableService {
        private String id;
        private String passenger_id;
        private String total_amount;
        private String total_currency;
    }
} 