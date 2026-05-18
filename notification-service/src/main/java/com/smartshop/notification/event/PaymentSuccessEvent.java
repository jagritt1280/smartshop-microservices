package com.smartshop.notification.event;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessEvent {
    private Long paymentId;
    private String orderId;
    private String userId;
    private BigDecimal amount;
    private String transactionId;
}