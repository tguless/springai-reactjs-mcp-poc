package com.example.common.service;

import com.example.common.model.DataAnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
public class DataAnalysisService {
    
    private static final Logger logger = LoggerFactory.getLogger(DataAnalysisService.class);
    private final Map<String, Boolean> analysisStatus = new ConcurrentHashMap<>();
    
    /**
     * Analyzes data synchronously for simple requests
     */
    public DataAnalysisResult analyzeData(String data, int complexity) {
        logger.info("Analyzing data synchronously: {}", data);
        
        // Create a simple result with a few data points
        List<DataAnalysisResult.DataPoint> dataPoints = new ArrayList<>();
        dataPoints.add(new DataAnalysisResult.DataPoint("value", Math.random() * 100, "units"));
        dataPoints.add(new DataAnalysisResult.DataPoint("confidence", Math.random() * 100, "%"));
        
        return DataAnalysisResult.builder()
                .dataId(UUID.randomUUID().toString())
                .analysisType("simple")
                .complexity(complexity)
                .analysisTime(LocalDateTime.now())
                .dataPoints(dataPoints)
                .conclusion("Analysis of '" + data + "' completed successfully")
                .build();
    }
    
    /**
     * Process data in stages and provides incremental updates through the callback
     */
    @Async
    public void analyzeDataWithUpdates(String data, int complexity, Consumer<String> updateCallback) {
        String analysisId = UUID.randomUUID().toString();
        analysisStatus.put(analysisId, true);
        
        logger.info("Starting asynchronous analysis: {} with complexity {}", data, complexity);
        
        try {
            // Simulate multiple stages of analysis
            for (int stage = 1; stage <= complexity && analysisStatus.getOrDefault(analysisId, false); stage++) {
                // Simulate processing time
                Thread.sleep(2000);
                
                // Generate stage result
                String stageResult = String.format(
                        "Stage %d analysis: Processed %s with confidence %.2f%%", 
                        stage, 
                        data, 
                        (Math.random() * 40) + 60); // 60-100% confidence
                
                logger.debug("Analysis update: {}", stageResult);
                updateCallback.accept(stageResult);
            }
            
            // Final conclusion if not canceled
            if (analysisStatus.getOrDefault(analysisId, false)) {
                String conclusion = "Analysis complete for data: " + data + 
                        ". Identified key patterns with " + ((Math.random() * 20) + 80) + "% confidence.";
                updateCallback.accept(conclusion);
            }
        } catch (InterruptedException e) {
            logger.warn("Analysis interrupted: {}", e.getMessage());
            Thread.currentThread().interrupt();
            updateCallback.accept("Analysis was interrupted: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error during analysis: {}", e.getMessage(), e);
            updateCallback.accept("Error during analysis: " + e.getMessage());
        } finally {
            analysisStatus.remove(analysisId);
        }
    }
    
    /**
     * Starts monitoring a data source and streams updates
     */
    @Async
    public void monitorDataSource(String source, int duration, Consumer<String> dataCallback) {
        analysisStatus.put(source, true);
        logger.info("Starting monitoring of data source: {} for {} seconds", source, duration);
        
        try {
            int iterations = duration;
            for (int i = 0; i < iterations && analysisStatus.getOrDefault(source, false); i++) {
                // Generate some simulated data
                double value = generateValue(i, source);
                String trend = value > 50 ? "increasing" : "decreasing";
                
                String update = String.format(
                        "Time: %d sec | Source: %s | Value: %.2f | Trend: %s", 
                        i, source, value, trend);
                
                logger.debug("Monitor update: {}", update);
                dataCallback.accept(update);
                
                // Wait for next monitoring interval
                Thread.sleep(1000);
            }
            
            // Send completion message
            if (analysisStatus.getOrDefault(source, false)) {
                dataCallback.accept("Monitoring complete for source: " + source);
            }
        } catch (InterruptedException e) {
            logger.warn("Monitoring interrupted: {}", e.getMessage());
            Thread.currentThread().interrupt();
            dataCallback.accept("Monitoring was interrupted: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error during monitoring: {}", e.getMessage(), e);
            dataCallback.accept("Error during monitoring: " + e.getMessage());
        } finally {
            analysisStatus.remove(source);
        }
    }
    
    /**
     * Stops monitoring or analysis
     */
    public void stopProcess(String id) {
        logger.info("Stopping process: {}", id);
        analysisStatus.put(id, false);
    }
    
    /**
     * Generates a synthetic data value
     */
    private double generateValue(int iteration, String source) {
        // Create different patterns based on the source name
        double offset = Math.abs(source.hashCode() % 100);
        double amplitude = 25.0;
        double frequency = 0.1;
        
        return offset + amplitude * Math.sin(frequency * iteration);
    }
} 