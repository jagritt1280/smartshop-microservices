package com.smartshop.product.repository;

import com.smartshop.product.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProductRepository
        extends MongoRepository<Product, String> {

    List<Product> findByCategory(String category);
    List<Product> findByAvailableTrue();
    List<Product> findByNameContainingIgnoreCase(
            String name);
}