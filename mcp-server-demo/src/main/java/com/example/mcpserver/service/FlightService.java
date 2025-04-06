package com.example.mcpserver.service;

import com.example.mcpserver.client.DuffelClient;
import com.example.mcpserver.config.DuffelConfig;
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
import com.example.mcpserver.model.flights.AirportResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FlightService {

    private final DuffelClient duffelClient;
    private final DuffelConfig duffelConfig;
    private static final String DUFFEL_API_VERSION = "v1";
    private static final String DUFFEL_IDENTITY_API_VERSION = "v2";
    private static final String DUFFEL_AIR_API_VERSION = "v2";
    
    public FlightService(DuffelClient duffelClient, DuffelConfig duffelConfig) {
        this.duffelClient = duffelClient;
        this.duffelConfig = duffelConfig;
    }
    
    /**
     * Search for flights based on given criteria
     */
    public OfferRequestResponse searchFlights(
            String origin, 
            String destination, 
            String departureDate, 
            Integer adults, 
            Integer children, 
            Integer infants, 
            OfferRequestCreate.CabinClass cabinClass) {
        
        log.debug("Searching flights from {} to {} on {} for {} adults, {} children, {} infants in {} class",
                origin, destination, departureDate, adults, children, infants, cabinClass);
        
        // Create the offer request
        OfferRequestCreate offerRequest = buildOfferRequest(origin, destination, departureDate, 
                adults, children, infants, cabinClass);
        
        // Wrap the request in a data object as required by Duffel API
        DuffelApiWrapper<OfferRequestCreate> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(offerRequest);
        
        // Headers required by Duffel API
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            // Make the API call
            return duffelClient.createOfferRequest(authorization, accept, DUFFEL_API_VERSION, wrapper);
        } catch (Exception e) {
            log.error("Error searching flights", e);
            throw new RuntimeException("Failed to search flights: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get detailed information about a specific offer
     */
    public OfferResponse getOffer(String offerId, Boolean returnAvailableServices) {
        log.debug("Getting offer details for offer ID: {}", offerId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.getOffer(authorization, accept, DUFFEL_API_VERSION, offerId, returnAvailableServices);
        } catch (Exception e) {
            log.error("Error retrieving offer details", e);
            throw new RuntimeException("Failed to get offer details: " + e.getMessage(), e);
        }
    }
    
    /**
     * List all offers for a given offer request
     */
    public OfferResponse listOffers(String offerRequestId, Integer limit, String before, String after) {
        log.debug("Listing offers for offer request ID: {}", offerRequestId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.listOffers(authorization, accept, DUFFEL_API_VERSION, offerRequestId, limit, before, after);
        } catch (Exception e) {
            log.error("Error listing offers", e);
            throw new RuntimeException("Failed to list offers: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create an order for a selected offer
     */
    public OrderResponse createOrder(
            String offerId,
            List<OrderCreateRequest.Passenger> passengers,
            String type,
            OrderCreateRequest.PaymentDetails payment,
            Map<String, Object> metadata) {
        
        log.debug("Creating order for offer ID: {}", offerId);
        
        // Build the order request
        OrderCreateRequest orderRequest = OrderCreateRequest.builder()
                .offerId(offerId)
                .passengers(passengers)
                .type(type)
                .payments(payment)
                .metadata(metadata != null ? 
                    OrderCreateRequest.Metadata.builder()
                        .customerReference(metadata.get("customerReference") != null ? 
                            metadata.get("customerReference").toString() : null)
                        .build() : null)
                .build();
        
        // Wrap the request
        DuffelApiWrapper<OrderCreateRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(orderRequest);
        
        // Headers required by Duffel API
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            // Make the API call
            return duffelClient.createOrder(authorization, accept, DUFFEL_API_VERSION, wrapper);
        } catch (Exception e) {
            log.error("Error creating order", e);
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get details of a specific order
     */
    public OrderResponse getOrder(String orderId) {
        log.debug("Getting order details for order ID: {}", orderId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.getOrder(authorization, accept, DUFFEL_API_VERSION, orderId);
        } catch (Exception e) {
            log.error("Error retrieving order details", e);
            throw new RuntimeException("Failed to get order details: " + e.getMessage(), e);
        }
    }
    
    /**
     * List all orders with optional filters
     */
    public OrderResponse listOrders(Integer limit, String before, String after, String bookingReference) {
        log.debug("Listing orders");
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.listOrders(authorization, accept, DUFFEL_API_VERSION, limit, before, after, bookingReference);
        } catch (Exception e) {
            log.error("Error listing orders", e);
            throw new RuntimeException("Failed to list orders: " + e.getMessage(), e);
        }
    }
    
    /**
     * Process payment for a hold order
     */
    public PaymentResponse createPayment(String orderId, String paymentType, String amount, String currency) {
        log.debug("Processing payment for order ID: {}", orderId);
        
        // Build the payment request
        PaymentRequest.Payment payment = PaymentRequest.Payment.builder()
                .type(paymentType)
                .amount(amount)
                .currency(currency)
                .build();
        
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(orderId)
                .payment(payment)
                .build();
        
        // Wrap the request
        DuffelApiWrapper<PaymentRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(paymentRequest);
        
        // Headers required by Duffel API
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            // Make the API call
            return duffelClient.createPayment(authorization, accept, DUFFEL_API_VERSION, wrapper);
        } catch (Exception e) {
            log.error("Error processing payment", e);
            throw new RuntimeException("Failed to process payment: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get seat maps for a specific offer
     */
    public SeatMapResponse getSeatMaps(String offerId) {
        log.debug("Getting seat maps for offer ID: {}", offerId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.getSeatMaps(authorization, accept, DUFFEL_API_VERSION, offerId);
        } catch (Exception e) {
            log.error("Error retrieving seat maps", e);
            throw new RuntimeException("Failed to get seat maps: " + e.getMessage(), e);
        }
    }
    
    /**
     * List all order cancellations with optional filters
     */
    public OrderCancellationResponse listOrderCancellations(String orderId, Integer limit, String before, String after) {
        log.debug("Listing order cancellations");
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.listOrderCancellations(authorization, accept, DUFFEL_API_VERSION, orderId, limit, before, after);
        } catch (Exception e) {
            log.error("Error listing order cancellations", e);
            throw new RuntimeException("Failed to list order cancellations: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a pending order cancellation
     */
    public OrderCancellationResponse createOrderCancellation(String orderId) {
        log.debug("Creating pending order cancellation for order ID: {}", orderId);
        
        // Build the order cancellation request
        OrderCancellationCreateRequest request = OrderCancellationCreateRequest.builder()
                .orderId(orderId)
                .build();
        
        // Wrap the request
        DuffelApiWrapper<OrderCancellationCreateRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        // Headers required by Duffel API
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            // Make the API call
            return duffelClient.createOrderCancellation(authorization, accept, DUFFEL_API_VERSION, wrapper);
        } catch (Exception e) {
            log.error("Error creating order cancellation", e);
            throw new RuntimeException("Failed to create order cancellation: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get details of a specific order cancellation
     */
    public OrderCancellationResponse getOrderCancellation(String cancellationId) {
        log.debug("Getting order cancellation details for ID: {}", cancellationId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.getOrderCancellation(authorization, accept, DUFFEL_API_VERSION, cancellationId);
        } catch (Exception e) {
            log.error("Error retrieving order cancellation details", e);
            throw new RuntimeException("Failed to get order cancellation details: " + e.getMessage(), e);
        }
    }
    
    /**
     * Confirm an order cancellation
     */
    public OrderCancellationResponse confirmOrderCancellation(String cancellationId) {
        log.debug("Confirming order cancellation with ID: {}", cancellationId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.confirmOrderCancellation(authorization, accept, DUFFEL_API_VERSION, cancellationId);
        } catch (Exception e) {
            log.error("Error confirming order cancellation", e);
            throw new RuntimeException("Failed to confirm order cancellation: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create an order change request to modify an existing paid order
     */
    public OrderChangeRequestResponse createOrderChangeRequest(
            String orderId, 
            List<OrderChangeRequestCreate.RemoveSlice> removeSlices,
            List<OrderChangeRequestCreate.AddSlice> addSlices,
            Map<String, List<OrderChangeRequestCreate.PrivateFare>> privateFares) {
        
        log.debug("Creating order change request for order ID: {}", orderId);
        
        // Build the order change request
        OrderChangeRequestCreate.Slices slices = OrderChangeRequestCreate.Slices.builder()
                .remove(removeSlices)
                .add(addSlices)
                .build();
                
        OrderChangeRequestCreate request = OrderChangeRequestCreate.builder()
                .orderId(orderId)
                .slices(slices)
                .privateFares(privateFares)
                .build();
        
        // Wrap the request
        DuffelApiWrapper<OrderChangeRequestCreate> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        // Headers required by Duffel API
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            // Make the API call
            return duffelClient.createOrderChangeRequest(authorization, accept, DUFFEL_API_VERSION, wrapper);
        } catch (Exception e) {
            log.error("Error creating order change request", e);
            throw new RuntimeException("Failed to create order change request: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get details of a specific order change request
     */
    public OrderChangeRequestResponse getOrderChangeRequest(String orderChangeRequestId) {
        log.debug("Getting order change request details for ID: {}", orderChangeRequestId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.getOrderChangeRequest(authorization, accept, DUFFEL_API_VERSION, orderChangeRequestId);
        } catch (Exception e) {
            log.error("Error retrieving order change request details", e);
            throw new RuntimeException("Failed to get order change request details: " + e.getMessage(), e);
        }
    }
    
    /**
     * List order change offers for a specific order change request
     */
    public OrderChangeOfferResponse listOrderChangeOffers(
            String orderChangeRequestId,
            Integer limit,
            String before,
            String after,
            String sort,
            Integer maxConnections) {
        
        log.debug("Listing order change offers for request ID: {}", orderChangeRequestId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.listOrderChangeOffers(
                    authorization,
                    accept,
                    DUFFEL_API_VERSION,
                    orderChangeRequestId,
                    limit,
                    before,
                    after,
                    sort,
                    maxConnections);
        } catch (Exception e) {
            log.error("Error listing order change offers", e);
            throw new RuntimeException("Failed to list order change offers: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get details of a specific order change offer
     */
    public OrderChangeOfferResponse getOrderChangeOffer(String orderChangeOfferId) {
        log.debug("Getting order change offer details for ID: {}", orderChangeOfferId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.getOrderChangeOffer(authorization, accept, DUFFEL_API_VERSION, orderChangeOfferId);
        } catch (Exception e) {
            log.error("Error retrieving order change offer details", e);
            throw new RuntimeException("Failed to get order change offer details: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a pending order change using an order change offer
     */
    public OrderChangeResponse createOrderChange(String orderChangeOfferId) {
        log.debug("Creating pending order change for offer ID: {}", orderChangeOfferId);
        
        // Build the order change request
        OrderChangeCreateRequest request = OrderChangeCreateRequest.builder()
                .orderChangeOfferId(orderChangeOfferId)
                .build();
        
        // Wrap the request
        DuffelApiWrapper<OrderChangeCreateRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        // Headers required by Duffel API
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            // Make the API call
            return duffelClient.createOrderChange(authorization, accept, DUFFEL_API_VERSION, wrapper);
        } catch (Exception e) {
            log.error("Error creating order change", e);
            throw new RuntimeException("Failed to create order change: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get details of a specific order change
     */
    public OrderChangeResponse getOrderChange(String orderChangeId) {
        log.debug("Getting order change details for ID: {}", orderChangeId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.getOrderChange(authorization, accept, DUFFEL_API_VERSION, orderChangeId);
        } catch (Exception e) {
            log.error("Error retrieving order change details", e);
            throw new RuntimeException("Failed to get order change details: " + e.getMessage(), e);
        }
    }
    
    /**
     * Confirm an order change with optional payment details
     */
    public OrderChangeResponse confirmOrderChange(String orderChangeId, String paymentType, String amount, String currency, String threeDSecureSessionId) {
        log.debug("Confirming order change with ID: {}", orderChangeId);
        
        // Headers required by Duffel API
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            // If no payment details are required (free change or refund)
            if (paymentType == null || amount == null || currency == null) {
                return duffelClient.confirmOrderChange(authorization, accept, DUFFEL_API_VERSION, orderChangeId, null);
            }
            
            // Build the payment details for the confirm request
            OrderChangeConfirmRequest.Payment payment = OrderChangeConfirmRequest.Payment.builder()
                    .type(paymentType)
                    .amount(amount)
                    .currency(currency)
                    .threeDSecureSessionId(threeDSecureSessionId)
                    .build();
            
            OrderChangeConfirmRequest confirmRequest = OrderChangeConfirmRequest.builder()
                    .payment(payment)
                    .build();
            
            // Wrap the request
            DuffelApiWrapper<OrderChangeConfirmRequest> wrapper = new DuffelApiWrapper<>();
            wrapper.setData(confirmRequest);
            
            // Make the API call
            return duffelClient.confirmOrderChange(authorization, accept, DUFFEL_API_VERSION, orderChangeId, wrapper);
        } catch (Exception e) {
            log.error("Error confirming order change", e);
            throw new RuntimeException("Failed to confirm order change: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a new customer user
     */
    public CustomerUserResponse createCustomerUser(String email, String givenName, String familyName, String phoneNumber, String groupId) {
        log.debug("Creating customer user with email: {}, name: {} {}", email, givenName, familyName);
        
        // Build the customer user request
        CustomerUserCreateRequest request = CustomerUserCreateRequest.builder()
                .email(email)
                .givenName(givenName)
                .familyName(familyName)
                .phoneNumber(phoneNumber)
                .groupId(groupId)
                .build();
        
        // Wrap the request
        DuffelApiWrapper<CustomerUserCreateRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        // Headers required by Duffel API
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            // Make the API call
            return duffelClient.createCustomerUser(authorization, accept, DUFFEL_IDENTITY_API_VERSION, wrapper);
        } catch (Exception e) {
            log.error("Error creating customer user", e);
            throw new RuntimeException("Failed to create customer user: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get details of a specific customer user
     */
    public CustomerUserResponse getCustomerUser(String userId) {
        log.debug("Getting customer user details for ID: {}", userId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.getCustomerUser(authorization, accept, DUFFEL_IDENTITY_API_VERSION, userId);
        } catch (Exception e) {
            log.error("Error retrieving customer user details", e);
            throw new RuntimeException("Failed to get customer user details: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update an existing customer user
     */
    public CustomerUserResponse updateCustomerUser(String userId, String email, String givenName, String familyName, String phoneNumber, String groupId) {
        log.debug("Updating customer user with ID: {}", userId);
        
        // Build the customer user update request
        CustomerUserUpdateRequest request = CustomerUserUpdateRequest.builder()
                .email(email)
                .givenName(givenName)
                .familyName(familyName)
                .phoneNumber(phoneNumber)
                .groupId(groupId)
                .build();
        
        // Wrap the request
        DuffelApiWrapper<CustomerUserUpdateRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        // Headers required by Duffel API
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            // Make the API call
            return duffelClient.updateCustomerUser(authorization, accept, DUFFEL_IDENTITY_API_VERSION, userId, wrapper);
        } catch (Exception e) {
            log.error("Error updating customer user", e);
            throw new RuntimeException("Failed to update customer user: " + e.getMessage(), e);
        }
    }
    
    /**
     * List all customer user groups
     */
    public CustomerUserGroupResponse listCustomerUserGroups() {
        log.debug("Listing customer user groups");
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.listCustomerUserGroups(authorization, accept, DUFFEL_IDENTITY_API_VERSION);
        } catch (Exception e) {
            log.error("Error listing customer user groups", e);
            throw new RuntimeException("Failed to list customer user groups: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a new customer user group
     */
    public CustomerUserGroupResponse createCustomerUserGroup(String name, List<String> userIds) {
        log.debug("Creating customer user group with name: {}", name);
        
        // Build the customer user group request
        CustomerUserGroupCreateRequest request = CustomerUserGroupCreateRequest.builder()
                .name(name)
                .userIds(userIds)
                .build();
        
        // Wrap the request
        DuffelApiWrapper<CustomerUserGroupCreateRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        // Headers required by Duffel API
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            // Make the API call
            return duffelClient.createCustomerUserGroup(authorization, accept, DUFFEL_IDENTITY_API_VERSION, wrapper);
        } catch (Exception e) {
            log.error("Error creating customer user group", e);
            throw new RuntimeException("Failed to create customer user group: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get details of a specific customer user group
     */
    public CustomerUserGroupResponse getCustomerUserGroup(String groupId) {
        log.debug("Getting customer user group details for ID: {}", groupId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.getCustomerUserGroup(authorization, accept, DUFFEL_IDENTITY_API_VERSION, groupId);
        } catch (Exception e) {
            log.error("Error retrieving customer user group details", e);
            throw new RuntimeException("Failed to get customer user group details: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update an existing customer user group
     */
    public CustomerUserGroupResponse updateCustomerUserGroup(String groupId, String name, List<String> userIds) {
        log.debug("Updating customer user group with ID: {}", groupId);
        
        // Build the customer user group update request
        CustomerUserGroupUpdateRequest request = CustomerUserGroupUpdateRequest.builder()
                .name(name)
                .userIds(userIds)
                .build();
        
        // Wrap the request
        DuffelApiWrapper<CustomerUserGroupUpdateRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        // Headers required by Duffel API
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            // Make the API call
            return duffelClient.updateCustomerUserGroup(authorization, accept, DUFFEL_IDENTITY_API_VERSION, groupId, wrapper);
        } catch (Exception e) {
            log.error("Error updating customer user group", e);
            throw new RuntimeException("Failed to update customer user group: " + e.getMessage(), e);
        }
    }
    
    /**
     * Delete a customer user group
     */
    public void deleteCustomerUserGroup(String groupId) {
        log.debug("Deleting customer user group with ID: {}", groupId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            duffelClient.deleteCustomerUserGroup(authorization, accept, DUFFEL_IDENTITY_API_VERSION, groupId);
        } catch (Exception e) {
            log.error("Error deleting customer user group", e);
            throw new RuntimeException("Failed to delete customer user group: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a component client key with no scope
     */
    public ComponentClientKeyResponse createComponentClientKey() {
        log.debug("Creating component client key with no scope");
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.createComponentClientKey(authorization, accept, DUFFEL_IDENTITY_API_VERSION);
        } catch (Exception e) {
            log.error("Error creating component client key", e);
            throw new RuntimeException("Failed to create component client key: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a component client key scoped to a specific user
     */
    public ComponentClientKeyResponse createComponentClientKeyForUser(String userId) {
        log.debug("Creating component client key for user: {}", userId);
        
        // Build the request
        ComponentClientKeyUserRequest request = ComponentClientKeyUserRequest.builder()
                .userId(userId)
                .build();
        
        // Wrap the request
        DuffelApiWrapper<ComponentClientKeyUserRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        // Headers required by Duffel API
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            // Make the API call
            return duffelClient.createComponentClientKeyForUser(authorization, accept, DUFFEL_IDENTITY_API_VERSION, wrapper);
        } catch (Exception e) {
            log.error("Error creating component client key for user", e);
            throw new RuntimeException("Failed to create component client key for user: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a component client key scoped to a specific user and order
     */
    public ComponentClientKeyResponse createComponentClientKeyForUserAndOrder(String userId, String orderId) {
        log.debug("Creating component client key for user: {} and order: {}", userId, orderId);
        
        // Build the request
        ComponentClientKeyUserOrderRequest request = ComponentClientKeyUserOrderRequest.builder()
                .userId(userId)
                .orderId(orderId)
                .build();
        
        // Wrap the request
        DuffelApiWrapper<ComponentClientKeyUserOrderRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        // Headers required by Duffel API
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            // Make the API call
            return duffelClient.createComponentClientKeyForUserAndOrder(authorization, accept, DUFFEL_IDENTITY_API_VERSION, wrapper);
        } catch (Exception e) {
            log.error("Error creating component client key for user and order", e);
            throw new RuntimeException("Failed to create component client key for user and order: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a component client key scoped to a specific user and booking
     */
    public ComponentClientKeyResponse createComponentClientKeyForUserAndBooking(String userId, String bookingId) {
        log.debug("Creating component client key for user: {} and booking: {}", userId, bookingId);
        
        // Build the request
        ComponentClientKeyUserBookingRequest request = ComponentClientKeyUserBookingRequest.builder()
                .userId(userId)
                .bookingId(bookingId)
                .build();
        
        // Wrap the request
        DuffelApiWrapper<ComponentClientKeyUserBookingRequest> wrapper = new DuffelApiWrapper<>();
        wrapper.setData(request);
        
        // Headers required by Duffel API
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            // Make the API call
            return duffelClient.createComponentClientKeyForUserAndBooking(authorization, accept, DUFFEL_IDENTITY_API_VERSION, wrapper);
        } catch (Exception e) {
            log.error("Error creating component client key for user and booking", e);
            throw new RuntimeException("Failed to create component client key for user and booking: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get details of a specific airline
     */
    public AirlineResponse getAirline(String airlineId) {
        log.debug("Getting airline details for ID: {}", airlineId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.getAirline(authorization, accept, DUFFEL_AIR_API_VERSION, airlineId);
        } catch (Exception e) {
            log.error("Error retrieving airline details", e);
            throw new RuntimeException("Failed to get airline details: " + e.getMessage(), e);
        }
    }
    
    /**
     * List airlines with optional pagination
     */
    public AirlineResponse listAirlines(Integer limit, String before, String after) {
        log.debug("Listing airlines with limit: {}, before: {}, after: {}", limit, before, after);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.listAirlines(authorization, accept, DUFFEL_AIR_API_VERSION, limit, before, after);
        } catch (Exception e) {
            log.error("Error listing airlines", e);
            throw new RuntimeException("Failed to list airlines: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get details of a specific aircraft
     */
    public AircraftResponse getAircraft(String aircraftId) {
        log.debug("Getting aircraft details for ID: {}", aircraftId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.getAircraft(authorization, accept, DUFFEL_AIR_API_VERSION, aircraftId);
        } catch (Exception e) {
            log.error("Error retrieving aircraft details", e);
            throw new RuntimeException("Failed to get aircraft details: " + e.getMessage(), e);
        }
    }
    
    /**
     * List aircraft with optional pagination
     */
    public AircraftResponse listAircraft(Integer limit, String before, String after) {
        log.debug("Listing aircraft with limit: {}, before: {}, after: {}", limit, before, after);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.listAircraft(authorization, accept, DUFFEL_AIR_API_VERSION, limit, before, after);
        } catch (Exception e) {
            log.error("Error listing aircraft", e);
            throw new RuntimeException("Failed to list aircraft: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get details of a specific airport
     */
    public AirportResponse getAirport(String airportId) {
        log.debug("Getting airport details for ID: {}", airportId);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.getAirport(authorization, accept, DUFFEL_AIR_API_VERSION, airportId);
        } catch (Exception e) {
            log.error("Error retrieving airport details", e);
            throw new RuntimeException("Failed to get airport details: " + e.getMessage(), e);
        }
    }
    
    /**
     * List airports with optional pagination and filtering
     */
    public AirportResponse listAirports(Integer limit, String before, String after, String iataCountryCode) {
        log.debug("Listing airports with limit: {}, before: {}, after: {}, country code: {}", 
                limit, before, after, iataCountryCode);
        
        String authorization = "Bearer " + duffelConfig.getApiKey();
        String accept = "application/json";
        
        try {
            return duffelClient.listAirports(authorization, accept, DUFFEL_AIR_API_VERSION, 
                    limit, before, after, iataCountryCode);
        } catch (Exception e) {
            log.error("Error listing airports", e);
            throw new RuntimeException("Failed to list airports: " + e.getMessage(), e);
        }
    }
    
    private OfferRequestCreate buildOfferRequest(
            String origin, 
            String destination, 
            String departureDateStr, 
            Integer adults, 
            Integer children, 
            Integer infants, 
            OfferRequestCreate.CabinClass cabinClass) {
        
        // Parse the departure date
        LocalDate departureDate = LocalDate.parse(departureDateStr, DateTimeFormatter.ISO_DATE);
        
        // Create the slice (flight segment)
        OfferRequestCreate.Slice slice = OfferRequestCreate.Slice.builder()
                .origin(origin)
                .destination(destination)
                .departureDate(departureDate)
                .build();
        
        // Create the list of passengers
        List<OfferRequestCreate.Passenger> passengers = new ArrayList<>();
        
        // Add adult passengers
        for (int i = 0; i < adults; i++) {
            passengers.add(OfferRequestCreate.Passenger.builder()
                    .type(OfferRequestCreate.PassengerType.adult)
                    .build());
        }
        
        // Add child passengers if any
        if (children != null && children > 0) {
            for (int i = 0; i < children; i++) {
                passengers.add(OfferRequestCreate.Passenger.builder()
                        .type(OfferRequestCreate.PassengerType.child)
                        .build());
            }
        }
        
        // Add infant passengers if any
        if (infants != null && infants > 0) {
            for (int i = 0; i < infants; i++) {
                passengers.add(OfferRequestCreate.Passenger.builder()
                        .type(OfferRequestCreate.PassengerType.infant_without_seat)
                        .build());
            }
        }
        
        // Build the complete offer request
        return OfferRequestCreate.builder()
                .slices(List.of(slice))
                .passengers(passengers)
                .cabinClass(cabinClass)
                .build();
    }
} 