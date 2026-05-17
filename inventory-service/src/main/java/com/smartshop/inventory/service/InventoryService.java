package com.smartshop.inventory.service;

import com.smartshop.inventory.entity.Inventory;
import com.smartshop.inventory.event.LowStockEvent;
import com.smartshop.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Inventory getByProductId(String productId) {
        return inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() -> new RuntimeException(
                        "Inventory not found: " + productId));
    }

    @Transactional
    public Inventory addStock(String productId,
                              Integer quantity) {
        Inventory inv = inventoryRepository
                .findByProductId(productId)
                .orElse(Inventory.builder()
                        .productId(productId)
                        .quantity(0)
                        .reserved(0)
                        .threshold(10)
                        .build());

        inv.setQuantity(inv.getQuantity() + quantity);
        Inventory saved = inventoryRepository.save(inv);
        log.info("Stock added: {} → {}",
                productId, saved.getQuantity());
        return saved;
    }

    @Transactional
    public boolean reserveStock(String productId,
                                Integer quantity) {
        Inventory inv = getByProductId(productId);

        if(inv.getAvailable() < quantity) {
            log.warn("Insufficient stock: {}",
                    productId);
            return false;
        }

        inv.setReserved(inv.getReserved() + quantity);
        inventoryRepository.save(inv);

        if(inv.getAvailable() <= inv.getThreshold()) {
            publishLowStockEvent(inv);
        }
        return true;
    }

    @Transactional
    public void releaseStock(String productId,
                             Integer quantity) {
        Inventory inv = getByProductId(productId);
        inv.setReserved(
                Math.max(0, inv.getReserved() - quantity));
        inventoryRepository.save(inv);
        log.info("Stock released: {}", productId);
    }

    @Transactional
    public void confirmStock(String productId,
                             Integer quantity) {
        Inventory inv = getByProductId(productId);
        inv.setQuantity(inv.getQuantity() - quantity);
        inv.setReserved(
                Math.max(0, inv.getReserved() - quantity));
        inventoryRepository.save(inv);
        log.info("Stock confirmed: {}", productId);
    }

    private void publishLowStockEvent(Inventory inv) {
        LowStockEvent event = new LowStockEvent(
                inv.getProductId(),
                inv.getAvailable(),
                inv.getThreshold(),
                "Low stock alert for: " + inv.getProductId()
        );
        kafkaTemplate.send("low-stock", event);
        log.info("Low stock event published: {}",
                inv.getProductId());
    }
}