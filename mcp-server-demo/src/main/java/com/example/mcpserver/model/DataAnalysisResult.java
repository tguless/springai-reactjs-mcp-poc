package com.example.mcpserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataAnalysisResult {
    private String dataId;
    private String analysisType;
    private int complexity;
    private LocalDateTime analysisTime;
    private List<DataPoint> dataPoints;
    private String conclusion;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataPoint {
        private String name;
        private double value;
        private String unit;
    }
} 