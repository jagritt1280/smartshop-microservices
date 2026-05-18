package com.smartshop.payment.service;

import com.smartshop.payment.dto.*;
import com.smartshop.payment.entity.*;
import com.smartshop.payment.event.PaymentSuccessEvent;
import com.smartshop.payment.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final LedgerRepository ledgerRepository;
    private final KafkaTemplate<String, Object>
            kafkaTemplate;

    @Transactional
    public PaymentResponse initiatePayment(
            PaymentRequest request) {

        // Idempotency check
        return paymentRepository
                .findByIdempotencyKey(
                        request.getIdempotencyKey())
                .map(existing -> {
                    log.info("Duplicate payment request" +
                                    " — returning cached: {}",
                            existing.getId());
                    return mapToResponse(existing,
                            "Duplicate — returning cached result");
                })
                .orElseGet(() -> {
                    Payment payment = Payment.builder()
                            .idempotencyKey(
                                    request.getIdempotencyKey())
                            .orderId(request.getOrderId())
                            .userId(request.getUserId())
                            .amount(request.getAmount())
                            .paymentMethod(
                                    Payment.PaymentMethod.valueOf(
                                            request.getPaymentMethod()
                                                    .toUpperCase()))
                            .status(Payment.PaymentStatus.PENDING)
                            .build();

                    Payment saved =
                            paymentRepository.save(payment);
                    log.info("Payment initiated: {}",
                            saved.getId());
                    return mapToResponse(saved,
                            "Payment initiated successfully");
                });
    }

    @Transactional
    public PaymentResponse confirmPayment(Long paymentId) {
        Payment payment = paymentRepository
                .findById(paymentId)
                .orElseThrow(() -> new RuntimeException(
                        "Payment not found: " + paymentId));

        if(payment.getStatus() !=
                Payment.PaymentStatus.PENDING) {
            throw new RuntimeException(
                    "Payment already processed");
        }

        // Update payment status
        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        payment.setTransactionId(
                "TXN-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase());
        Payment saved = paymentRepository.save(payment);

        // Double entry ledger
        Ledger debit = Ledger.builder()
                .paymentId(saved.getId().toString())
                .orderId(saved.getOrderId())
                .userId(saved.getUserId())
                .entryType(Ledger.EntryType.DEBIT)
                .amount(saved.getAmount())
                .description("Payment for order: " +
                        saved.getOrderId())
                .build();
        ledgerRepository.save(debit);

        Ledger credit = Ledger.builder()
                .paymentId(saved.getId().toString())
                .orderId(saved.getOrderId())
                .userId(saved.getUserId())
                .entryType(Ledger.EntryType.CREDIT)
                .amount(saved.getAmount())
                .description("Credit for order: " +
                        saved.getOrderId())
                .build();
        ledgerRepository.save(credit);

        log.info("Payment confirmed: {}",
                saved.getTransactionId());

        // Publish Kafka event
        try {
            PaymentSuccessEvent event =
                    new PaymentSuccessEvent(
                            saved.getId(),
                            saved.getOrderId(),
                            saved.getUserId(),
                            saved.getAmount(),
                            saved.getTransactionId());
            kafkaTemplate.send("payment-success", event);
            log.info("Payment event published: {}",
                    saved.getTransactionId());
        } catch(Exception e) {
            log.warn("Kafka unavailable: {}",
                    e.getMessage());
        }

        return mapToResponse(saved,
                "Payment successful");
    }

    public PaymentResponse getByOrderId(
            String orderId) {
        Payment payment = paymentRepository
                .findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException(
                        "Payment not found for order: " +
                                orderId));
        return mapToResponse(payment, "Success");
    }

    public List<Payment> getByUserId(String userId) {
        return paymentRepository.findByUserId(userId);
    }

    public List<Ledger> getLedgerByOrderId(
            String orderId) {
        return ledgerRepository.findByOrderId(orderId);
    }

    private PaymentResponse mapToResponse(
            Payment p, String message) {
        return PaymentResponse.builder()
                .id(p.getId())
                .orderId(p.getOrderId())
                .transactionId(p.getTransactionId())
                .amount(p.getAmount())
                .status(p.getStatus().name())
                .paymentMethod(p.getPaymentMethod().name())
                .createdAt(p.getCreatedAt())
                .message(message)
                .build();
    }
}