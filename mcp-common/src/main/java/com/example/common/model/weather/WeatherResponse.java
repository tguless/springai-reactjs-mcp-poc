package com.example.common.model.weather;

import java.util.List;
import lombok.Data;

@Data
public class WeatherResponse {
    private double lat;
    private double lon;
    private String timezone;
    private int timezone_offset;
    private Current current;
    private List<Minutely> minutely;
    private List<Hourly> hourly;
    private List<Daily> daily;
    private List<Alert> alerts;
}

@Data
class Current {
    private long dt;
    private long sunrise;
    private long sunset;
    private double temp;
    private double feels_like;
    private int pressure;
    private int humidity;
    private double dew_point;
    private double uvi;
    private int clouds;
    private int visibility;
    private double wind_speed;
    private int wind_deg;
    private double wind_gust;
    private List<Weather> weather;
    private Rain rain;
}

@Data
class Weather {
    private int id;
    private String main;
    private String description;
    private String icon;
}

@Data
class Rain {
    private double the1h;
}

@Data
class Minutely {
    private long dt;
    private double precipitation;
}

@Data
class Hourly {
    private long dt;
    private double temp;
    private double feels_like;
    private int pressure;
    private int humidity;
    private double dew_point;
    private double uvi;
    private int clouds;
    private int visibility;
    private double wind_speed;
    private int wind_deg;
    private List<Weather> weather;
    private double pop;
    private Rain rain;
}

@Data
class Daily {
    private long dt;
    private long sunrise;
    private long sunset;
    private long moonrise;
    private long moonset;
    private double moon_phase;
    private String summary;
    private Temp temp;
    private FeelsLike feels_like;
    private int pressure;
    private int humidity;
    private double dew_point;
    private double wind_speed;
    private int wind_deg;
    private List<Weather> weather;
    private int clouds;
    private double pop;
    private double rain;
    private double uvi;
}

@Data
class Temp {
    private double day;
    private double min;
    private double max;
    private double night;
    private double eve;
    private double morn;
}

@Data
class FeelsLike {
    private double day;
    private double night;
    private double eve;
    private double morn;
}

@Data
class Alert {
    private String sender_name;
    private String event;
    private long start;
    private long end;
    private String description;
} 