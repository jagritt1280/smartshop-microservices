package com.smartshop.payment.repository;

import com.smartshop.payment.entity.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LedgerRepository
        extends JpaRepository<Ledger, Long> {
    List<Ledger> findByOrderId(String orderId);
}