package com.example.mcpserver.client;

import com.example.mcpserver.config.FeignClientConfig;
import com.example.mcpserver.model.weather.WeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weather", url = "https://api.openweathermap.org/data/3.0", configuration = FeignClientConfig.class)
public interface WeatherClient {
    
    @GetMapping("/onecall")
    WeatherResponse getWeatherData(
            @RequestParam("lat") Double lat,
            @RequestParam("lon") Double lon,
            @RequestParam("appid") String apiKey,
            @RequestParam(value = "exclude", required = false) String exclude,
            @RequestParam(value = "units", required = false) String units,
            @RequestParam(value = "lang", required = false) String lang
    );
} 