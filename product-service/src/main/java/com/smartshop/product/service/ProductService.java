package com.smartshop.product.service;

import com.smartshop.product.entity.Product;
import com.smartshop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Cacheable(value = "products")
    public List<Product> getAllProducts() {
        return productRepository.findByAvailableTrue();
    }

    @Cacheable(value = "products",
            key = "#category")
    public List<Product> getByCategory(
            String category) {
        return productRepository
                .findByCategory(category);
    }

    public Product getById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found: " + id));
    }

    @CacheEvict(value = "products",
            allEntries = true)
    public Product createProduct(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    @CacheEvict(value = "products",
            allEntries = true)
    public Product updateProduct(String id,
                                 Product updated) {
        Product existing = getById(id);
        existing.setName(updated.getName());
        existing.setPrice(updated.getPrice());
        existing.setDescription(
                updated.getDescription());
        existing.setCategory(updated.getCategory());
        return productRepository.save(existing);
    }

    @CacheEvict(value = "products",
            allEntries = true)
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchProducts(
            String name) {
        return productRepository
                .findByNameContainingIgnoreCase(name);
    }
}