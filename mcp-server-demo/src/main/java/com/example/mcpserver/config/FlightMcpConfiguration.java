package com.example.mcpserver.config;

import com.example.mcpserver.model.flights.OfferRequestCreate;
import com.example.mcpserver.model.flights.OfferRequestResponse;
import com.example.mcpserver.model.flights.OfferResponse;
import com.example.mcpserver.model.flights.OrderCreateRequest;
import com.example.mcpserver.model.flights.OrderResponse;
import com.example.mcpserver.model.flights.PaymentResponse;
import com.example.mcpserver.model.flights.SeatMapResponse;
import com.example.mcpserver.model.flights.OrderCancellationResponse;
import com.example.mcpserver.model.flights.OrderChangeRequestCreate;
import com.example.mcpserver.model.flights.OrderChangeRequestResponse;
import com.example.mcpserver.model.flights.OrderChangeOfferResponse;
import com.example.mcpserver.model.flights.OrderChangeResponse;
import com.example.mcpserver.model.flights.CustomerUserResponse;
import com.example.mcpserver.model.flights.CustomerUserGroupResponse;
import com.example.mcpserver.model.flights.ComponentClientKeyResponse;
import com.example.mcpserver.model.flights.AirlineResponse;
import com.example.mcpserver.model.flights.AircraftResponse;
import com.example.mcpserver.service.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration class that registers flight search tools with the MCP server.
 */
@Configuration
public class FlightMcpConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(FlightMcpConfiguration.class);
    private final FlightService flightService;

    public FlightMcpConfiguration(FlightService flightService) {
        this.flightService = flightService;
        logger.info("FlightMcpConfiguration initialized");
    }

    /**
     * Registers the flight search tools as a tool callback provider.
     * This provider is used by the MCP server to expose tools to clients.
     */
    @Bean
    public ToolCallbackProvider flightTools() {
        logger.info("Registering flight search tools with MCP server");
        return MethodToolCallbackProvider.builder()
                .toolObjects(new FlightTools(flightService))
                .build();
    }

    /**
     * Inner class that holds the flight tool methods exposed by the MCP server.
     */
    public static class FlightTools {

        private final FlightService service;
        private static final Logger logger = LoggerFactory.getLogger(FlightTools.class);

        public FlightTools(FlightService service) {
            this.service = service;
            logger.info("FlightTools initialized");
        }

        @Tool(description = "Search for flights between two airports on a specific date")
        public OfferRequestResponse searchFlights(
                @ToolParam(description = "Origin airport IATA code (e.g., 'JFK')") String origin,
                @ToolParam(description = "Destination airport IATA code (e.g., 'LAX')") String destination,
                @ToolParam(description = "Departure date in YYYY-MM-DD format") String departureDate,
                @ToolParam(description = "Number of adult passengers") Integer adults,
                @ToolParam(description = "Number of children (optional)") Integer children,
                @ToolParam(description = "Number of infants (optional)") Integer infants,
                @ToolParam(description = "Cabin class: economy, premium_economy, business, first") String cabinClass) {
            
            // Set defaults for optional parameters
            if (children == null) children = 0;
            if (infants == null) infants = 0;
            
            // Parse cabin class
            OfferRequestCreate.CabinClass cabin;
            try {
                cabin = OfferRequestCreate.CabinClass.valueOf(cabinClass);
            } catch (IllegalArgumentException e) {
                // Default to economy if invalid
                cabin = OfferRequestCreate.CabinClass.economy;
            }
            
            return service.searchFlights(origin, destination, departureDate, adults, children, infants, cabin);
        }
        
        @Tool(description = "Get detailed information about a specific flight offer")
        public OfferResponse getOfferDetails(
                @ToolParam(description = "The ID of the offer to retrieve (e.g., 'off_00009htYpSCXrwaB9DnUm0')") String offerId,
                @ToolParam(description = "Whether to include available services in the response (optional)") Boolean returnAvailableServices) {
            
            return service.getOffer(offerId, returnAvailableServices);
        }
        
        @Tool(description = "List all offers for a previous flight search")
        public OfferResponse listOffersForSearch(
                @ToolParam(description = "The offer request ID from a previous flight search") String offerRequestId,
                @ToolParam(description = "Maximum number of offers to return (optional)") Integer limit,
                @ToolParam(description = "Pagination cursor for items before this ID (optional)") String before,
                @ToolParam(description = "Pagination cursor for items after this ID (optional)") String after) {
            
            return service.listOffers(offerRequestId, limit, before, after);
        }
        
        @Tool(description = "Create an order for a selected flight offer")
        public OrderResponse createOrder(
                @ToolParam(description = "The ID of the offer to book") String offerId,
                @ToolParam(description = "Type of order: 'instant' (immediate payment) or 'hold' (reserved without payment)") String type,
                @ToolParam(description = "Passenger's title (e.g., 'Mr', 'Mrs', 'Ms')") String title,
                @ToolParam(description = "Passenger's first name") String givenName,
                @ToolParam(description = "Passenger's last name") String familyName,
                @ToolParam(description = "Passenger's birth date (YYYY-MM-DD)") String birthDate,
                @ToolParam(description = "Passenger's email address") String email,
                @ToolParam(description = "Passenger's phone number with country code") String phone,
                @ToolParam(description = "Passenger's gender (male or female)") String gender,
                @ToolParam(description = "Payment amount (must match the offer amount)") String paymentAmount,
                @ToolParam(description = "Payment currency (must match the offer currency)") String paymentCurrency,
                @ToolParam(description = "Customer reference for tracking (optional)") String customerReference) {
            
            // Create passenger information
            List<OrderCreateRequest.Passenger> passengers = new ArrayList<>();
            
            LocalDate bornOn = LocalDate.parse(birthDate, DateTimeFormatter.ISO_DATE);
            
            OrderCreateRequest.Passenger passenger = OrderCreateRequest.Passenger.builder()
                    .title(title)
                    .givenName(givenName)
                    .familyName(familyName)
                    .bornOn(bornOn)
                    .email(email)
                    .phone(phone)
                    .gender(gender)
                    .build();
            
            passengers.add(passenger);
            
            // Create payment details
            OrderCreateRequest.PaymentDetails payment = OrderCreateRequest.PaymentDetails.builder()
                    .type("balance")  // Using account balance for payment
                    .amount(paymentAmount)
                    .currency(paymentCurrency)
                    .build();
            
            // Add optional customer reference
            Map<String, Object> metadata = new HashMap<>();
            if (customerReference != null && !customerReference.isEmpty()) {
                metadata.put("customerReference", customerReference);
            }
            
            return service.createOrder(offerId, passengers, type, payment, metadata);
        }
        
        @Tool(description = "Get detailed information about a specific order")
        public OrderResponse getOrderDetails(
                @ToolParam(description = "The ID of the order to retrieve") String orderId) {
            
            return service.getOrder(orderId);
        }
        
        @Tool(description = "List all orders with optional filters")
        public OrderResponse listOrders(
                @ToolParam(description = "Maximum number of orders to return (optional)") Integer limit,
                @ToolParam(description = "Pagination cursor for items before this ID (optional)") String before,
                @ToolParam(description = "Pagination cursor for items after this ID (optional)") String after,
                @ToolParam(description = "Filter by booking reference (optional)") String bookingReference) {
            
            return service.listOrders(limit, before, after, bookingReference);
        }
        
        @Tool(description = "Process payment for a hold order")
        public PaymentResponse processPayment(
                @ToolParam(description = "The ID of the order to pay for") String orderId,
                @ToolParam(description = "Payment type: 'balance' (using Duffel account's balance), 'card', or 'arc_bsp_cash' (for IATA agents)") String paymentType,
                @ToolParam(description = "Payment amount (must match the order's total amount)") String amount,
                @ToolParam(description = "Payment currency (must match the order's currency)") String currency) {
            
            return service.createPayment(orderId, paymentType, amount, currency);
        }
        
        @Tool(description = "Get seat maps for a specific flight offer")
        public SeatMapResponse getSeatMaps(
                @ToolParam(description = "The ID of the offer to get seat maps for") String offerId) {
            
            return service.getSeatMaps(offerId);
        }
        
        @Tool(description = "List order cancellations with optional filters")
        public OrderCancellationResponse listOrderCancellations(
                @ToolParam(description = "Filter by order ID (optional)") String orderId,
                @ToolParam(description = "Maximum number of results to return (optional)") Integer limit,
                @ToolParam(description = "Pagination cursor for items before this ID (optional)") String before,
                @ToolParam(description = "Pagination cursor for items after this ID (optional)") String after) {
                
            return service.listOrderCancellations(orderId, limit, before, after);
        }
        
        @Tool(description = "Create a pending order cancellation (first step in cancellation process)")
        public OrderCancellationResponse createOrderCancellation(
                @ToolParam(description = "The ID of the order to be cancelled") String orderId) {
                
            return service.createOrderCancellation(orderId);
        }
        
        @Tool(description = "Get details of a specific order cancellation")
        public OrderCancellationResponse getOrderCancellation(
                @ToolParam(description = "The ID of the order cancellation to retrieve") String cancellationId) {
                
            return service.getOrderCancellation(cancellationId);
        }
        
        @Tool(description = "Confirm an order cancellation (final step that actually cancels the order)")
        public OrderCancellationResponse confirmOrderCancellation(
                @ToolParam(description = "The ID of the order cancellation to confirm") String cancellationId) {
                
            return service.confirmOrderCancellation(cancellationId);
        }
        
        @Tool(description = "Create an order change request to modify an existing paid order")
        public OrderChangeRequestResponse createOrderChangeRequest(
                @ToolParam(description = "The ID of the order to be changed") String orderId,
                @ToolParam(description = "ID of the slice to remove (e.g., 'sli_00009htYpSCXrwaB9Dn123')") String removeSliceId,
                @ToolParam(description = "Origin airport IATA code for the new slice (e.g., 'LHR')") String newOrigin,
                @ToolParam(description = "Destination airport IATA code for the new slice (e.g., 'JFK')") String newDestination,
                @ToolParam(description = "Departure date for the new slice in YYYY-MM-DD format") String newDepartureDate,
                @ToolParam(description = "Cabin class for the new slice: economy, premium_economy, business, first") String newCabinClass) {
                
            // Create the remove slice
            OrderChangeRequestCreate.RemoveSlice removeSlice = OrderChangeRequestCreate.RemoveSlice.builder()
                    .sliceId(removeSliceId)
                    .build();
            
            // Create the add slice
            LocalDate departureDate = LocalDate.parse(newDepartureDate, DateTimeFormatter.ISO_DATE);
            
            OrderChangeRequestCreate.AddSlice addSlice = OrderChangeRequestCreate.AddSlice.builder()
                    .origin(newOrigin)
                    .destination(newDestination)
                    .departureDate(departureDate)
                    .cabinClass(newCabinClass)
                    .build();
            
            return service.createOrderChangeRequest(
                    orderId,
                    List.of(removeSlice),
                    List.of(addSlice),
                    null // No private fares in this simplified version
            );
        }
        
        @Tool(description = "Get details of a specific order change request")
        public OrderChangeRequestResponse getOrderChangeRequest(
                @ToolParam(description = "The ID of the order change request to retrieve") String orderChangeRequestId) {
                
            return service.getOrderChangeRequest(orderChangeRequestId);
        }
        
        @Tool(description = "List order change offers for a specific order change request")
        public OrderChangeOfferResponse listOrderChangeOffers(
                @ToolParam(description = "The ID of the order change request to list offers for") String orderChangeRequestId,
                @ToolParam(description = "Maximum number of offers to return (optional)") Integer limit,
                @ToolParam(description = "Sort order: 'change_total_amount' or 'total_duration' (optional)") String sort,
                @ToolParam(description = "Maximum number of connections per flight (optional)") Integer maxConnections,
                @ToolParam(description = "Pagination cursor for items before this ID (optional)") String before,
                @ToolParam(description = "Pagination cursor for items after this ID (optional)") String after) {
                
            return service.listOrderChangeOffers(
                    orderChangeRequestId,
                    limit,
                    before,
                    after,
                    sort,
                    maxConnections);
        }
        
        @Tool(description = "Get details of a specific order change offer")
        public OrderChangeOfferResponse getOrderChangeOffer(
                @ToolParam(description = "The ID of the order change offer to retrieve") String orderChangeOfferId) {
                
            return service.getOrderChangeOffer(orderChangeOfferId);
        }
        
        @Tool(description = "Create a pending order change from a selected order change offer")
        public OrderChangeResponse createOrderChange(
                @ToolParam(description = "The ID of the order change offer to use") String orderChangeOfferId) {
                
            return service.createOrderChange(orderChangeOfferId);
        }
        
        @Tool(description = "Get details of a specific order change")
        public OrderChangeResponse getOrderChange(
                @ToolParam(description = "The ID of the order change to retrieve") String orderChangeId) {
                
            return service.getOrderChange(orderChangeId);
        }
        
        @Tool(description = "Confirm an order change and complete the booking modification process")
        public OrderChangeResponse confirmOrderChange(
                @ToolParam(description = "The ID of the order change to confirm") String orderChangeId,
                @ToolParam(description = "Payment type: 'balance' (using Duffel account's balance), 'card', etc. (optional if no payment required)") String paymentType,
                @ToolParam(description = "Payment amount (required if change has a cost)") String amount,
                @ToolParam(description = "Payment currency (required if change has a cost)") String currency,
                @ToolParam(description = "3D Secure session ID for card payments (optional)") String threeDSecureSessionId) {
                
            return service.confirmOrderChange(orderChangeId, paymentType, amount, currency, threeDSecureSessionId);
        }
        
        @Tool(description = "Create a new customer user in the Duffel platform")
        public CustomerUserResponse createCustomerUser(
                @ToolParam(description = "Customer's email address") String email,
                @ToolParam(description = "Customer's first name") String givenName,
                @ToolParam(description = "Customer's last name") String familyName,
                @ToolParam(description = "Customer's phone number in E.164 (international) format (e.g., +442080160509)") String phoneNumber,
                @ToolParam(description = "ID of the group to associate the customer with (optional)") String groupId) {
                
            return service.createCustomerUser(email, givenName, familyName, phoneNumber, groupId);
        }
        
        @Tool(description = "Get details of a specific customer user")
        public CustomerUserResponse getCustomerUser(
                @ToolParam(description = "The ID of the customer user to retrieve") String userId) {
                
            return service.getCustomerUser(userId);
        }
        
        @Tool(description = "Update an existing customer user's information")
        public CustomerUserResponse updateCustomerUser(
                @ToolParam(description = "The ID of the customer user to update") String userId,
                @ToolParam(description = "Customer's updated email address") String email,
                @ToolParam(description = "Customer's updated first name") String givenName,
                @ToolParam(description = "Customer's updated last name") String familyName,
                @ToolParam(description = "Customer's updated phone number in E.164 (international) format") String phoneNumber,
                @ToolParam(description = "Updated ID of the group to associate the customer with (optional)") String groupId) {
                
            return service.updateCustomerUser(userId, email, givenName, familyName, phoneNumber, groupId);
        }
        
        @Tool(description = "List all customer user groups")
        public CustomerUserGroupResponse listCustomerUserGroups() {
            return service.listCustomerUserGroups();
        }
        
        @Tool(description = "Create a new customer user group")
        public CustomerUserGroupResponse createCustomerUserGroup(
                @ToolParam(description = "Name of the customer user group") String name,
                @ToolParam(description = "Comma-separated list of user IDs to add to the group (optional)") String userIds) {
            
            List<String> userIdList = null;
            if (userIds != null && !userIds.isEmpty()) {
                userIdList = Arrays.asList(userIds.split(","));
            } else {
                userIdList = new ArrayList<>();
            }
            
            return service.createCustomerUserGroup(name, userIdList);
        }
        
        @Tool(description = "Get details of a specific customer user group")
        public CustomerUserGroupResponse getCustomerUserGroup(
                @ToolParam(description = "The ID of the customer user group to retrieve") String groupId) {
            
            return service.getCustomerUserGroup(groupId);
        }
        
        @Tool(description = "Update an existing customer user group")
        public CustomerUserGroupResponse updateCustomerUserGroup(
                @ToolParam(description = "The ID of the customer user group to update") String groupId,
                @ToolParam(description = "Updated name of the customer user group") String name,
                @ToolParam(description = "Comma-separated list of user IDs to include in the group") String userIds) {
            
            List<String> userIdList = null;
            if (userIds != null && !userIds.isEmpty()) {
                userIdList = Arrays.asList(userIds.split(","));
            } else {
                userIdList = new ArrayList<>();
            }
            
            return service.updateCustomerUserGroup(groupId, name, userIdList);
        }
        
        @Tool(description = "Delete a customer user group")
        public void deleteCustomerUserGroup(
                @ToolParam(description = "The ID of the customer user group to delete") String groupId) {
            
            service.deleteCustomerUserGroup(groupId);
        }
        
        @Tool(description = "Create a component client key with no scope for authenticating Duffel UI components")
        public ComponentClientKeyResponse createComponentClientKey() {
            return service.createComponentClientKey();
        }
        
        @Tool(description = "Create a component client key scoped to a specific user for authenticating Duffel UI components")
        public ComponentClientKeyResponse createComponentClientKeyForUser(
                @ToolParam(description = "ID of the customer user to associate with this key") String userId) {
            
            return service.createComponentClientKeyForUser(userId);
        }
        
        @Tool(description = "Create a component client key scoped to a specific user and order for authenticating Duffel UI components")
        public ComponentClientKeyResponse createComponentClientKeyForUserAndOrder(
                @ToolParam(description = "ID of the customer user to associate with this key") String userId,
                @ToolParam(description = "ID of the order to associate with this key") String orderId) {
            
            return service.createComponentClientKeyForUserAndOrder(userId, orderId);
        }
        
        @Tool(description = "Create a component client key scoped to a specific user and booking for authenticating Duffel UI components")
        public ComponentClientKeyResponse createComponentClientKeyForUserAndBooking(
                @ToolParam(description = "ID of the customer user to associate with this key") String userId,
                @ToolParam(description = "ID of the booking to associate with this key") String bookingId) {
            
            return service.createComponentClientKeyForUserAndBooking(userId, bookingId);
        }
        
        @Tool(description = "Get details about a specific airline")
        public AirlineResponse getAirline(
                @ToolParam(description = "The ID of the airline to retrieve") String airlineId) {
            
            return service.getAirline(airlineId);
        }
        
        @Tool(description = "List airlines with optional pagination parameters")
        public AirlineResponse listAirlines(
                @ToolParam(description = "Maximum number of airlines to return per page (1-200)") Integer limit,
                @ToolParam(description = "Pagination cursor for items before this ID (optional)") String before,
                @ToolParam(description = "Pagination cursor for items after this ID (optional)") String after) {
            
            return service.listAirlines(limit, before, after);
        }
        
        @Tool(description = "Get details about a specific aircraft")
        public AircraftResponse getAircraft(
                @ToolParam(description = "The ID of the aircraft to retrieve") String aircraftId) {
            
            return service.getAircraft(aircraftId);
        }
        
        @Tool(description = "List aircraft with optional pagination parameters")
        public AircraftResponse listAircraft(
                @ToolParam(description = "Maximum number of aircraft to return per page (1-200)") Integer limit,
                @ToolParam(description = "Pagination cursor for items before this ID (optional)") String before,
                @ToolParam(description = "Pagination cursor for items after this ID (optional)") String after) {
            
            return service.listAircraft(limit, before, after);
        }
    }
} 