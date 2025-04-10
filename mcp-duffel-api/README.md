# MCP Duffel API Module

This module provides integration with the Duffel Flights API for searching, booking, and managing flight reservations.

## Overview

Duffel is a modern API for flight booking that provides access to hundreds of airlines through a single API. This module
wraps the Duffel API in a Spring Boot friendly way, providing Java models and services for easy integration with other
components of the application.

## Features

- Search for flight offers across multiple airlines
- Retrieve detailed information about specific offers
- Book flights with passenger details
- Manage bookings (cancel, modify, etc.)

## Configuration

To use the Duffel API integration, add the following configuration to your `application.properties` or `application.yml` file:

```yaml
duffel:
  api:
    base-url: https://api.duffel.com/air
    key: your_duffel_api_key_here
```

## Usage

To use this module in your project, add the following dependency to your Maven POM file:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>mcp-duffel-api</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### Example: Searching for Flights

```java
@Service
@RequiredArgsConstructor
public class FlightSearchService {
    private final DuffelService duffelService;
    
    public List<Offer> searchFlights(String origin, String destination, String departureDate) {
        OfferRequest request = OfferRequest.builder()
            .slices(List.of(
                OfferRequest.SliceRequest.builder()
                    .origin(origin)
                    .destination(destination)
                    .departure_date(departureDate)
                    .build()
            ))
            .passengers(List.of(
                OfferRequest.PassengerRequest.builder()
                    .type("adult")
                    .build()
            ))
            .cabin_class("economy")
            .build();
            
        return duffelService.searchOffers(request);
    }
}
```

## Development

This module uses Spring Cloud OpenFeign for API integration. The main components are:

- **Models**: Java classes representing the Duffel API data structures
- **Client**: Feign client interface for communicating with the Duffel API
- **Configuration**: Configuration for the Feign client, including authentication
- **Service**: Higher-level service that wraps the client and provides additional functionality 