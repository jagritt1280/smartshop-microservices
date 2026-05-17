package com.smartshop.inventory.event;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LowStockEvent {
    private String productId;
    private Integer currentStock;
    private Integer threshold;
    private String message;
}