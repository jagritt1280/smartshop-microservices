package com.smartshop.payment.controller;

import com.smartshop.payment.dto.*;
import com.smartshop.payment.entity.*;
import com.smartshop.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Payments",
        description = "Payment processing with idempotency")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Initiate payment")
    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiate(
            @RequestBody @Valid PaymentRequest request) {
        return ResponseEntity.ok(
                paymentService.initiatePayment(request));
    }

    @Operation(summary = "Confirm payment")
    @PostMapping("/{paymentId}/confirm")
    public ResponseEntity<PaymentResponse> confirm(
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(
                paymentService.confirmPayment(paymentId));
    }

    @Operation(summary = "Get payment by order ID")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getByOrder(
            @PathVariable String orderId) {
        return ResponseEntity.ok(
                paymentService.getByOrderId(orderId));
    }

    @Operation(summary = "Get payments by user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getByUser(
            @PathVariable String userId) {
        return ResponseEntity.ok(
                paymentService.getByUserId(userId));
    }

    @Operation(summary = "Get ledger by order")
    @GetMapping("/ledger/{orderId}")
    public ResponseEntity<List<Ledger>> getLedger(
            @PathVariable String orderId) {
        return ResponseEntity.ok(
                paymentService.getLedgerByOrderId(orderId));
    }

    @Operation(summary = "Health check")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Payment Service UP");
    }
}