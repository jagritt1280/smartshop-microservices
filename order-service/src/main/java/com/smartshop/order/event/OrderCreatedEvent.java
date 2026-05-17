package com.smartshop.order.event;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private String orderNumber;
    private String userId;
    private BigDecimal totalAmount;
    private List<String> productIds;
}