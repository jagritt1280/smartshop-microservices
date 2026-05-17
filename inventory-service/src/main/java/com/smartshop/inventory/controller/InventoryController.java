package com.smartshop.inventory.controller;

import com.smartshop.inventory.entity.Inventory;
import com.smartshop.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getStock(
            @PathVariable String productId) {
        return ResponseEntity.ok(
                inventoryService.getByProductId(productId));
    }

    @PostMapping("/{productId}/add")
    public ResponseEntity<Inventory> addStock(
            @PathVariable String productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(
                inventoryService.addStock(
                        productId, quantity));
    }

    @PostMapping("/{productId}/reserve")
    public ResponseEntity<Boolean> reserve(
            @PathVariable String productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(
                inventoryService.reserveStock(
                        productId, quantity));
    }

    @PostMapping("/{productId}/release")
    public ResponseEntity<Void> release(
            @PathVariable String productId,
            @RequestParam Integer quantity) {
        inventoryService.releaseStock(
                productId, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/confirm")
    public ResponseEntity<Void> confirm(
            @PathVariable String productId,
            @RequestParam Integer quantity) {
        inventoryService.confirmStock(
                productId, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok(
                "Inventory Service UP");
    }
}