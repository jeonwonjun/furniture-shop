package com.furniture.shop.furniture_shop.controller.product;

import com.furniture.shop.furniture_shop.model.product.Product;
import com.furniture.shop.furniture_shop.repository.product.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ProductRepository productRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        productRepository.save(Product.builder().name("Oak Chair").price(129000L).stock(50).build());
        productRepository.save(Product.builder().name("Walnut Desk").price(349000L).stock(20).build());
    }

    @Test
    @DisplayName("상품 목록 조회: 200 + 2개 이상")
    void list() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].name", notNullValue()));
    }

    @Test
    @DisplayName("상품 상세 조회: 200 + 필드 확인")
    void detail() throws Exception {
        Long id = productRepository.findAll().get(0).getId();

        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name", not(isEmptyString())));
    }
}
