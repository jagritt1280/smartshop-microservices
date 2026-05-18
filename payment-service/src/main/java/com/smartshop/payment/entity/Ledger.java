package com.smartshop.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ledger")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ledger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentId;
    private String orderId;
    private String userId;

    @Enumerated(EnumType.STRING)
    private EntryType entryType;

    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;

    public enum EntryType {
        DEBIT, CREDIT
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}