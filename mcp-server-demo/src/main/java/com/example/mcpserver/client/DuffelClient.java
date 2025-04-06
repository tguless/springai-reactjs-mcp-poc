package com.example.mcpserver.client;

import com.example.mcpserver.config.FeignClientConfig;
import com.example.mcpserver.model.flights.DuffelApiWrapper;
import com.example.mcpserver.model.flights.OfferRequestCreate;
import com.example.mcpserver.model.flights.OfferRequestResponse;
import com.example.mcpserver.model.flights.OfferResponse;
import com.example.mcpserver.model.flights.OrderCreateRequest;
import com.example.mcpserver.model.flights.OrderResponse;
import com.example.mcpserver.model.flights.PaymentRequest;
import com.example.mcpserver.model.flights.PaymentResponse;
import com.example.mcpserver.model.flights.SeatMapResponse;
import com.example.mcpserver.model.flights.OrderCancellationCreateRequest;
import com.example.mcpserver.model.flights.OrderCancellationResponse;
import com.example.mcpserver.model.flights.OrderChangeRequestCreate;
import com.example.mcpserver.model.flights.OrderChangeRequestResponse;
import com.example.mcpserver.model.flights.OrderChangeOfferResponse;
import com.example.mcpserver.model.flights.OrderChangeCreateRequest;
import com.example.mcpserver.model.flights.OrderChangeConfirmRequest;
import com.example.mcpserver.model.flights.OrderChangeResponse;
import com.example.mcpserver.model.flights.PartialOfferRequestCreate;
import com.example.mcpserver.model.flights.PartialOfferRequestResponse;
import com.example.mcpserver.model.flights.PartialOfferFaresResponse;
import com.example.mcpserver.model.flights.AirlineInitiatedChangeResponse;
import com.example.mcpserver.model.flights.AirlineInitiatedChangeUpdateRequest;
import com.example.mcpserver.model.flights.BatchOfferRequestCreate;
import com.example.mcpserver.model.flights.BatchOfferRequestResponse;
import com.example.mcpserver.model.flights.CustomerUserCreateRequest;
import com.example.mcpserver.model.flights.CustomerUserUpdateRequest;
import com.example.mcpserver.model.flights.CustomerUserResponse;
import com.example.mcpserver.model.flights.CustomerUserGroupCreateRequest;
import com.example.mcpserver.model.flights.CustomerUserGroupUpdateRequest;
import com.example.mcpserver.model.flights.CustomerUserGroupResponse;
import com.example.mcpserver.model.flights.ComponentClientKeyUserRequest;
import com.example.mcpserver.model.flights.ComponentClientKeyUserOrderRequest;
import com.example.mcpserver.model.flights.ComponentClientKeyUserBookingRequest;
import com.example.mcpserver.model.flights.ComponentClientKeyResponse;
import com.example.mcpserver.model.flights.AirlineResponse;
import com.example.mcpserver.model.flights.AircraftResponse;

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
} 