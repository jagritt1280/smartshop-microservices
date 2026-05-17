package com.smartshop.order.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemRequest {
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}