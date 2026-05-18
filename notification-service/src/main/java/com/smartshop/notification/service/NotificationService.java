package com.smartshop.notification.service;

import com.smartshop.notification.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    public void sendOrderConfirmation(
            OrderCreatedEvent event) {
        log.info("📧 Order confirmation for: {} " +
                        "Order: {}",
                event.getUserId(),
                event.getOrderNumber());
        // JavaMailSender.send() here
        // when mail configured ✅
    }

    public void sendPaymentReceipt(
            PaymentSuccessEvent event) {
        log.info("📧 Payment receipt for: {} " +
                        "TXN: {} Amount: {}",
                event.getUserId(),
                event.getTransactionId(),
                event.getAmount());
    }

    public void sendLowStockAlert(
            LowStockEvent event) {
        log.info("⚠️ Low stock alert: {} " +
                        "Stock: {}/{}",
                event.getProductId(),
                event.getCurrentStock(),
                event.getThreshold());
    }
}