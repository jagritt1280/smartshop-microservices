package com.smartshop.order.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {

    @NotBlank(message = "User ID required")
    private String userId;

    @NotEmpty(message = "Order items required")
    private List<OrderItemRequest> items;

    @NotBlank(message = "Shipping address required")
    private String shippingAddress;
}