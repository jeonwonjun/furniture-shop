package com.furniture.shop.furniture_shop.repository.product;

import com.furniture.shop.furniture_shop.model.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    private final ProductRepository productRepository;

    @Autowired
    ProductRepositoryTest(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Test
    @DisplayName("Product 저장 & 존재 확인")
    void saveAndExists() {
        Product p = new Product("의자 A", 129000L, 50);
        productRepository.save(p);

        assertThat(p.getId()).isNotNull();
        assertThat(productRepository.existsByName("의자 A")).isTrue();
    }
}
