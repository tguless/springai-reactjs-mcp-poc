package com.example.common.client;

import com.example.common.config.FeignClientConfig;
import com.example.common.model.flights.DuffelApiWrapper;
import com.example.common.model.flights.OfferRequestCreate;
import com.example.common.model.flights.OfferRequestResponse;
import com.example.common.model.flights.OfferResponse;
import com.example.common.model.flights.OrderCreateRequest;
import com.example.common.model.flights.OrderResponse;
import com.example.common.model.flights.PaymentRequest;
import com.example.common.model.flights.PaymentResponse;
import com.example.common.model.flights.SeatMapResponse;
import com.example.common.model.flights.OrderCancellationCreateRequest;
import com.example.common.model.flights.OrderCancellationResponse;
import com.example.common.model.flights.OrderChangeRequestCreate;
import com.example.common.model.flights.OrderChangeRequestResponse;
import com.example.common.model.flights.OrderChangeOfferResponse;
import com.example.common.model.flights.OrderChangeCreateRequest;
import com.example.common.model.flights.OrderChangeConfirmRequest;
import com.example.common.model.flights.OrderChangeResponse;
import com.example.common.model.flights.PartialOfferRequestCreate;
import com.example.common.model.flights.PartialOfferRequestResponse;
import com.example.common.model.flights.PartialOfferFaresResponse;
import com.example.common.model.flights.AirlineInitiatedChangeResponse;
import com.example.common.model.flights.AirlineInitiatedChangeUpdateRequest;
import com.example.common.model.flights.BatchOfferRequestCreate;
import com.example.common.model.flights.BatchOfferRequestResponse;
import com.example.common.model.flights.CustomerUserCreateRequest;
import com.example.common.model.flights.CustomerUserUpdateRequest;
import com.example.common.model.flights.CustomerUserResponse;
import com.example.common.model.flights.CustomerUserGroupCreateRequest;
import com.example.common.model.flights.CustomerUserGroupUpdateRequest;
import com.example.common.model.flights.CustomerUserGroupResponse;
import com.example.common.model.flights.ComponentClientKeyUserRequest;
import com.example.common.model.flights.ComponentClientKeyUserOrderRequest;
import com.example.common.model.flights.ComponentClientKeyUserBookingRequest;
import com.example.common.model.flights.ComponentClientKeyResponse;
import com.example.common.model.flights.AirlineResponse;
import com.example.common.model.flights.AircraftResponse;
import com.example.common.model.flights.AirportResponse;
import com.example.common.model.flights.CityResponse;
import com.example.common.model.flights.PlaceResponse;
import com.example.common.model.flights.LoyaltyProgrammeResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@FeignClient(name = "duffel", url = "${duffel.base-url}", configuration = FeignClientConfig.class)
public interface DuffelClient {
    
    @PostMapping("/offer_requests")
    OfferRequestResponse createOfferRequest(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<OfferRequestCreate> request
    );
    
    @GetMapping("/offers/{id}")
    OfferResponse getOffer(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String offerId,
            @RequestParam(value = "return_available_services", required = false) Boolean returnAvailableServices
    );
    
    @GetMapping("/offers")
    OfferResponse listOffers(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestParam("offer_request_id") String offerRequestId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "before", required = false) String before,
            @RequestParam(value = "after", required = false) String after
    );
    
    @PostMapping("/orders")
    OrderResponse createOrder(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<OrderCreateRequest> request
    );
    
    @GetMapping("/orders/{id}")
    OrderResponse getOrder(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String orderId
    );
    
    @GetMapping("/orders")
    OrderResponse listOrders(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "before", required = false) String before,
            @RequestParam(value = "after", required = false) String after,
            @RequestParam(value = "booking_reference", required = false) String bookingReference
    );
    
    @PostMapping("/payments")
    PaymentResponse createPayment(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<PaymentRequest> request
    );
    
    @GetMapping("/seat_maps")
    SeatMapResponse getSeatMaps(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestParam("offer_id") String offerId
    );
    
    // New endpoints for order cancellations
    
    @GetMapping("/order_cancellations")
    OrderCancellationResponse listOrderCancellations(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestParam(value = "order_id", required = false) String orderId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "before", required = false) String before,
            @RequestParam(value = "after", required = false) String after
    );
    
    @PostMapping("/order_cancellations")
    OrderCancellationResponse createOrderCancellation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<OrderCancellationCreateRequest> request
    );
    
    @GetMapping("/order_cancellations/{id}")
    OrderCancellationResponse getOrderCancellation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String cancellationId
    );
    
    @PostMapping("/order_cancellations/{id}/actions/confirm")
    OrderCancellationResponse confirmOrderCancellation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String cancellationId
    );
    
    // New endpoints for order change requests
    
    @PostMapping("/order_change_requests")
    OrderChangeRequestResponse createOrderChangeRequest(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<OrderChangeRequestCreate> request
    );
    
    @GetMapping("/order_change_requests/{id}")
    OrderChangeRequestResponse getOrderChangeRequest(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String orderChangeRequestId
    );
    
    // New endpoints for order change offers
    
    @GetMapping("/order_change_offers")
    OrderChangeOfferResponse listOrderChangeOffers(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestParam("order_change_request_id") String orderChangeRequestId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "before", required = false) String before,
            @RequestParam(value = "after", required = false) String after,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "max_connections", required = false) Integer maxConnections
    );
    
    @GetMapping("/order_change_offers/{id}")
    OrderChangeOfferResponse getOrderChangeOffer(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String orderChangeOfferId
    );
    
    // New endpoints for order changes
    
    @PostMapping("/order_changes")
    OrderChangeResponse createOrderChange(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<OrderChangeCreateRequest> request
    );
    
    @GetMapping("/order_changes/{id}")
    OrderChangeResponse getOrderChange(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String orderChangeId
    );
    
    @PostMapping("/order_changes/{id}/actions/confirm")
    OrderChangeResponse confirmOrderChange(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String orderChangeId,
            @RequestBody(required = false) DuffelApiWrapper<OrderChangeConfirmRequest> request
    );
    
    // New endpoints for partial offer requests
    
    @PostMapping("/partial_offer_requests")
    PartialOfferRequestResponse createPartialOfferRequest(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<PartialOfferRequestCreate> request
    );
    
    @GetMapping("/partial_offer_requests/{id}")
    PartialOfferRequestResponse getPartialOfferRequest(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String partialOfferRequestId
    );
    
    @GetMapping("/partial_offer_requests/{id}/fares")
    PartialOfferFaresResponse getPartialOfferFares(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String partialOfferRequestId,
            @RequestParam("selected_partial_offer[]") List<String> selectedPartialOffers
    );
    
    // New endpoints for airline-initiated changes
    
    @GetMapping("/airline_initiated_changes")
    AirlineInitiatedChangeResponse listAirlineInitiatedChanges(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestParam(value = "order_id", required = false) String orderId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "before", required = false) String before,
            @RequestParam(value = "after", required = false) String after
    );
    
    @GetMapping("/airline_initiated_changes/{id}")
    AirlineInitiatedChangeResponse getAirlineInitiatedChange(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String airlineInitiatedChangeId
    );
    
    @PatchMapping("/airline_initiated_changes/{id}")
    AirlineInitiatedChangeResponse updateAirlineInitiatedChange(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String airlineInitiatedChangeId,
            @RequestBody DuffelApiWrapper<AirlineInitiatedChangeUpdateRequest> request
    );
    
    @PostMapping("/airline_initiated_changes/{id}/actions/accept")
    AirlineInitiatedChangeResponse acceptAirlineInitiatedChange(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String airlineInitiatedChangeId
    );
    
    // New endpoints for batch offer requests
    
    @PostMapping("/batch_offer_requests")
    BatchOfferRequestResponse createBatchOfferRequest(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestParam(value = "supplier_timeout", required = false) Integer supplierTimeout,
            @RequestBody DuffelApiWrapper<BatchOfferRequestCreate> request
    );
    
    @GetMapping("/batch_offer_requests/{id}")
    BatchOfferRequestResponse getBatchOfferRequest(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String batchOfferRequestId
    );
    
    // New endpoints for customer users
    
    @PostMapping("/identity/customer/users")
    CustomerUserResponse createCustomerUser(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<CustomerUserCreateRequest> request
    );
    
    @GetMapping("/identity/customer/users/{id}")
    CustomerUserResponse getCustomerUser(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String userId
    );
    
    @PutMapping("/identity/customer/users/{id}")
    CustomerUserResponse updateCustomerUser(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String userId,
            @RequestBody DuffelApiWrapper<CustomerUserUpdateRequest> request
    );
    
    // New endpoints for customer user groups
    
    @GetMapping("/identity/customer/user_groups")
    CustomerUserGroupResponse listCustomerUserGroups(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion
    );
    
    @PostMapping("/identity/customer/user_groups")
    CustomerUserGroupResponse createCustomerUserGroup(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<CustomerUserGroupCreateRequest> request
    );
    
    @GetMapping("/identity/customer/user_groups/{id}")
    CustomerUserGroupResponse getCustomerUserGroup(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String groupId
    );
    
    @PatchMapping("/identity/customer/user_groups/{id}")
    CustomerUserGroupResponse updateCustomerUserGroup(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String groupId,
            @RequestBody DuffelApiWrapper<CustomerUserGroupUpdateRequest> request
    );
    
    @DeleteMapping("/identity/customer/user_groups/{id}")
    void deleteCustomerUserGroup(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String groupId
    );
    
    // New endpoints for component client keys
    
    @PostMapping("/identity/component_client_keys")
    ComponentClientKeyResponse createComponentClientKey(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion
    );
    
    @PostMapping("/identity/component_client_keys")
    ComponentClientKeyResponse createComponentClientKeyForUser(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<ComponentClientKeyUserRequest> request
    );
    
    @PostMapping("/identity/component_client_keys")
    ComponentClientKeyResponse createComponentClientKeyForUserAndOrder(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<ComponentClientKeyUserOrderRequest> request
    );
    
    @PostMapping("/identity/component_client_keys")
    ComponentClientKeyResponse createComponentClientKeyForUserAndBooking(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestBody DuffelApiWrapper<ComponentClientKeyUserBookingRequest> request
    );
    
    // New endpoints for airlines
    
    @GetMapping("/air/airlines/{id}")
    AirlineResponse getAirline(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String airlineId
    );
    
    @GetMapping("/air/airlines")
    AirlineResponse listAirlines(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "before", required = false) String before,
            @RequestParam(value = "after", required = false) String after
    );
    
    // New endpoints for aircraft
    
    @GetMapping("/air/aircraft/{id}")
    AircraftResponse getAircraft(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String aircraftId
    );
    
    @GetMapping("/air/aircraft")
    AircraftResponse listAircraft(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "before", required = false) String before,
            @RequestParam(value = "after", required = false) String after
    );
    
    // New endpoints for airports
    
    @GetMapping("/air/airports/{id}")
    AirportResponse getAirport(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String airportId
    );
    
    @GetMapping("/air/airports")
    AirportResponse listAirports(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "before", required = false) String before,
            @RequestParam(value = "after", required = false) String after,
            @RequestParam(value = "iata_country_code", required = false) String iataCountryCode
    );
    
    // New endpoints for cities
    
    @GetMapping("/air/cities/{id}")
    CityResponse getCity(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String cityId
    );
    
    @GetMapping("/air/cities")
    CityResponse listCities(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "before", required = false) String before,
            @RequestParam(value = "after", required = false) String after
    );
    
    // New endpoint for place suggestions
    
    @GetMapping("/places/suggestions")
    PlaceResponse listPlaceSuggestions(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "rad", required = false) String rad,
            @RequestParam(value = "lat", required = false) String lat,
            @RequestParam(value = "lng", required = false) String lng
    );
    
    // New endpoints for loyalty programmes
    
    @GetMapping("/air/loyalty_programmes/{id}")
    LoyaltyProgrammeResponse getLoyaltyProgramme(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @PathVariable("id") String loyaltyProgrammeId
    );
    
    @GetMapping("/air/loyalty_programmes")
    LoyaltyProgrammeResponse listLoyaltyProgrammes(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept,
            @RequestHeader("Duffel-Version") String duffelVersion,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "before", required = false) String before,
            @RequestParam(value = "after", required = false) String after
    );
} 