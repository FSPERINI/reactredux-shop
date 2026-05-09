package com.ecommerce.api.controller;

import com.ecommerce.api.dto.CreateOrderRequest;
import com.ecommerce.api.model.Order;
import com.ecommerce.api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order management operations")
@SecurityRequirement(name = "Bearer Token")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create order", description = "Place an order with current cart items and delivery information")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(orderService.createOrder(request.getDeliveryInfo(), email));
    }

    @GetMapping
    @Operation(summary = "Get my orders", description = "Get all orders for the authenticated user")
    public ResponseEntity<List<Order>> getMyOrders(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(orderService.getOrdersByEmail(email));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Get order confirmation details")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}
