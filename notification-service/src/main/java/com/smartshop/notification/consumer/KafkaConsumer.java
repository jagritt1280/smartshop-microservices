package com.smartshop.notification.consumer;

import com.smartshop.notification.event.*;
import com.smartshop.notification.service
        .NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final NotificationService
            notificationService;

    @KafkaListener(
            topics = "order-created",
            groupId = "notification-group",
            containerFactory = "orderKafkaListenerFactory"
    )
    public void consumeOrderCreated(
            OrderCreatedEvent event) {
        log.info("Received order event: {}",
                event.getOrderNumber());
        notificationService
                .sendOrderConfirmation(event);
    }

    @KafkaListener(
            topics = "payment-success",
            groupId = "notification-group",
            containerFactory =
                    "paymentKafkaListenerFactory"
    )
    public void consumePaymentSuccess(
            PaymentSuccessEvent event) {
        log.info("Received payment event: {}",
                event.getTransactionId());
        notificationService
                .sendPaymentReceipt(event);
    }

    @KafkaListener(
            topics = "low-stock",
            groupId = "notification-group",
            containerFactory =
                    "lowStockKafkaListenerFactory"
    )
    public void consumeLowStock(
            LowStockEvent event) {
        log.info("Received low stock event: {}",
                event.getProductId());
        notificationService
                .sendLowStockAlert(event);
    }
}