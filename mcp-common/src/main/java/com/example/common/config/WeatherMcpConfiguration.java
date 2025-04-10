package com.example.common.config;

import com.example.common.model.geocoding.GeocodingLocation;
import com.example.common.model.weather.WeatherResponse;
import com.example.common.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class that registers weather tools with the MCP server.
 */
@Configuration
public class WeatherMcpConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(WeatherMcpConfiguration.class);
    private final WeatherService weatherService;

    public WeatherMcpConfiguration(WeatherService weatherService) {
        this.weatherService = weatherService;
        logger.info("WeatherMcpConfiguration initialized");
    }

    /**
     * Registers the weather tools as a tool callback provider.
     * This provider is used by the MCP server to expose tools to clients.
     */
    @Bean
    @Qualifier("weatherTools")
    public ToolCallbackProvider weatherTools() {
        logger.info("Registering weather tools with MCP server");
        return MethodToolCallbackProvider.builder()
                .toolObjects(new WeatherTools(weatherService))
                .build();
    }

    /**
     * Inner class that holds the weather tool methods exposed by the MCP server.
     */
    public static class WeatherTools {

        private final WeatherService service;

        public WeatherTools(WeatherService service) {
            this.service = service;
            logger.info("WeatherTools initialized");
        }

        @Tool(description = "Get geocoding information for a city by its name")
        public List<GeocodingLocation> getGeocodingByCityName(
                @ToolParam(description = "The city name to search for") String city,
                @ToolParam(description = "Maximum number of results to return (default 5)") Integer limit) {
            return service.getGeocodingByCityName(city, limit);
        }

        @Tool(description = "Get geocoding information for a location by its zip code")
        public GeocodingLocation getGeocodingByZip(
                @ToolParam(description = "The zip code to search for (format: zip,countryCode e.g. 10001,us)") String zipCode) {
            return service.getGeocodingByZip(zipCode);
        }

        @Tool(description = "Get geocoding information for a location by its coordinates")
        public List<GeocodingLocation> getGeocodingByCoordinates(
                @ToolParam(description = "The latitude coordinate") Double latitude,
                @ToolParam(description = "The longitude coordinate") Double longitude,
                @ToolParam(description = "Maximum number of results to return (default 5)") Integer limit) {
            return service.getGeocodingByCoordinates(latitude, longitude, limit);
        }

        @Tool(description = "Get current weather and forecast data for a location using its coordinates")
        public WeatherResponse getWeatherData(
                @ToolParam(description = "The latitude coordinate") Double latitude,
                @ToolParam(description = "The longitude coordinate") Double longitude,
                @ToolParam(description = "Parts to exclude: current,minutely,hourly,daily,alerts (comma-separated, optional)") String exclude,
                @ToolParam(description = "Units of measurement: standard, metric, imperial (optional, default: standard)") String units,
                @ToolParam(description = "Response language (optional, default: en)") String language,
                ToolContext context
        ) throws InterruptedException {
            HttpServletRequest request = (HttpServletRequest) context.getContext().get("httpRequest");
            return service.getWeatherData(latitude, longitude, exclude, units, language);
        }

        @Tool(description = "Get weather data for a city by name (combines geocoding and weather lookup)")
        public WeatherResponse getWeatherForCity(
                @ToolParam(description = "The city name to search for") String city,
                @ToolParam(description = "Units of measurement: standard, metric, imperial (optional, default: standard)") String units,
                @ToolParam(description = "Response language (optional, default: en)") String language,
                ToolContext context
        ) {
            HttpServletRequest request = (HttpServletRequest) context.getContext().get("httpRequest");
            return service.getWeatherForCity(city, units, language);
        }



    }
} 