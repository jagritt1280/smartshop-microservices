package com.smartshop.payment.repository;

import com.smartshop.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {
    Optional<Payment> findByIdempotencyKey(
            String idempotencyKey);
    Optional<Payment> findByOrderId(String orderId);
    List<Payment> findByUserId(String userId);
}