package com.example.common.client;

import com.example.common.config.FeignClientConfig;
import com.example.common.model.geocoding.GeocodingLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "geocoding", url = "https://api.openweathermap.org/geo/1.0", configuration = FeignClientConfig.class)
public interface GeocodingClient {
    
    @GetMapping("/direct")
    List<GeocodingLocation> getLocationsByName(
            @RequestParam("q") String cityName,
            @RequestParam("limit") Integer limit,
            @RequestParam("appid") String apiKey
    );
    
    @GetMapping("/zip")
    GeocodingLocation getLocationByZipCode(
            @RequestParam("zip") String zipCode,
            @RequestParam("appid") String apiKey
    );
    
    @GetMapping("/reverse")
    List<GeocodingLocation> getLocationsByCoordinates(
            @RequestParam("lat") Double lat,
            @RequestParam("lon") Double lon,
            @RequestParam("limit") Integer limit,
            @RequestParam("appid") String apiKey
    );
} 