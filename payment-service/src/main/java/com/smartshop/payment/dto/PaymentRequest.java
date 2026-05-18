package com.smartshop.payment.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotBlank(message = "Idempotency key required")
    private String idempotencyKey;

    @NotBlank(message = "Order ID required")
    private String orderId;

    @NotBlank(message = "User ID required")
    private String userId;

    @NotNull(message = "Amount required")
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotNull(message = "Payment method required")
    private String paymentMethod;
}