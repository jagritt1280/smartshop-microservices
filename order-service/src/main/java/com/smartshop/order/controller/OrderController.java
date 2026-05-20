package com.smartshop.order.controller;

import com.smartshop.order.dto.CreateOrderRequest;
import com.smartshop.order.entity.Order;
import com.smartshop.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders",
        description = "Order lifecycle management")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create new order")
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestBody @Valid
            CreateOrderRequest request) {
        return ResponseEntity.ok(
                orderService.createOrder(request));
    }

    @Operation(summary = "Get orders by user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getByUser(
            @PathVariable String userId) {
        return ResponseEntity.ok(
                orderService.getOrdersByUser(userId));
    }

    @Operation(summary = "Get order by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                orderService.getOrderById(id));
    }

    @Operation(summary = "Get by order number")
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<Order> getByNumber(
            @PathVariable String orderNumber) {
        return ResponseEntity.ok(
                orderService.getOrderByNumber(
                        orderNumber));
    }

    @Operation(summary = "Cancel order")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Order> cancel(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                orderService.cancelOrder(id));
    }

    @Operation(summary = "Update order status")
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long id,
            @RequestParam Order.OrderStatus status) {
        return ResponseEntity.ok(
                orderService.updateStatus(id, status));
    }

    @Operation(summary = "Health check")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Order Service UP");
    }
}