package com.smartshop.product.controller;

import com.smartshop.product.entity.Product;
import com.smartshop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(
                productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(
            @PathVariable String id) {
        return ResponseEntity.ok(
                productService.getById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>>
    getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(
                productService.getByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(
            @RequestParam String name) {
        return ResponseEntity.ok(
                productService.searchProducts(name));
    }

    @PostMapping
    public ResponseEntity<Product> create(
            @RequestBody Product product) {
        return ResponseEntity.ok(
                productService.createProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable String id,
            @RequestBody Product product) {
        return ResponseEntity.ok(
                productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok(
                "Product Service UP");
    }
}