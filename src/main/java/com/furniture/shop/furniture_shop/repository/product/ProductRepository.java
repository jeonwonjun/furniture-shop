package com.furniture.shop.furniture_shop.repository.product;

import com.furniture.shop.furniture_shop.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
}
