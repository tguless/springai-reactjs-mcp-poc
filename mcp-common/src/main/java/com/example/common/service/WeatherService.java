package com.example.common.service;

import com.example.common.client.GeocodingClient;
import com.example.common.client.WeatherClient;
import com.example.common.config.OpenWeatherMapConfig;
import com.example.common.model.geocoding.GeocodingLocation;
import com.example.common.model.weather.WeatherResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WeatherService {

    private final GeocodingClient geocodingClient;
    private final WeatherClient weatherClient;
    private final OpenWeatherMapConfig openWeatherMapConfig;

    public WeatherService(GeocodingClient geocodingClient, 
                         WeatherClient weatherClient, 
                         OpenWeatherMapConfig openWeatherMapConfig) {
        this.geocodingClient = geocodingClient;
        this.weatherClient = weatherClient;
        this.openWeatherMapConfig = openWeatherMapConfig;
    }

    public List<GeocodingLocation> getGeocodingByCityName(String city, Integer limit) {
        if (limit == null) {
            limit = 5;
        }
        
        log.debug("Getting geocoding information for city: {}", city);
        return geocodingClient.getLocationsByName(city, limit, openWeatherMapConfig.getApiKey());
    }

    public GeocodingLocation getGeocodingByZip(String zipCode) {
        log.debug("Getting geocoding information for zip code: {}", zipCode);
        return geocodingClient.getLocationByZipCode(zipCode, openWeatherMapConfig.getApiKey());
    }

    public List<GeocodingLocation> getGeocodingByCoordinates(Double latitude, Double longitude, Integer limit) {
        if (limit == null) {
            limit = 5;
        }
        
        log.debug("Getting geocoding information for coordinates: {}, {}", latitude, longitude);
        return geocodingClient.getLocationsByCoordinates(latitude, longitude, limit, openWeatherMapConfig.getApiKey());
    }

    public WeatherResponse getWeatherData(Double latitude, Double longitude, String exclude, String units, String language) {
        log.debug("Getting weather data for coordinates: {}, {}", latitude, longitude);
        return weatherClient.getWeatherData(
                latitude, 
                longitude, 
                openWeatherMapConfig.getApiKey(),
                exclude,
                units,
                language
        );
    }

    public WeatherResponse getWeatherForCity(String city, String units, String language) {
        log.debug("Getting weather data for city: {}", city);
        List<GeocodingLocation> locations = getGeocodingByCityName(city, 1);
        
        if (locations.isEmpty()) {
            throw new RuntimeException("City not found: " + city);
        }
        
        GeocodingLocation location = locations.get(0);
        return getWeatherData(location.getLat(), location.getLon(), null, units, language);
    }
} 