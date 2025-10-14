package com.furniture.shop.furniture_shop.controller.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furniture.shop.furniture_shop.controller.auth.dto.AddToCartRequest;
import com.furniture.shop.furniture_shop.model.product.Product;
import com.furniture.shop.furniture_shop.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired private ProductRepository productRepository;

    Long pid;

    @BeforeEach
    void seed() {
        // H2/로컬 DB에 상품 1개 심기
        Product p = new Product("테스트 소파", 123000L, 45, "https://example.com/sofa.jpg");
        pid = productRepository.save(p).getId(); // 자동으로 ID 부여됨
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("GET /api/cart → 빈 장바구니")
    void getEmptyCart() throws Exception{
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("POST /api/cart → 상품 추가 후 조회")
    void addThenGet() throws Exception{
        AddToCartRequest request = new AddToCartRequest(1L, 2);

        // 상품 추가
        mockMvc.perform(post("/api/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId", is(1)))
                .andExpect(jsonPath("$[0].quantity", is(2)))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].price", notNullValue()));

        // 상품 조회
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productId", is(1)))
                .andExpect(jsonPath("$[0].quantity", is(2)));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("DELETE /api/cart/{itemId} → 삭제")
    void deleteItem() throws Exception{
        // 사전 추가
        var addRes = mockMvc.perform(post("/api/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddToCartRequest(3L, 1))))
                .andExpect(status().isOk())
                .andReturn();

        // res에서 itmeId 추출을 단순화: 다시 GET 후 첫 아이템 id 사용
        var getRes = mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andReturn();
        String body = getRes.getResponse().getContentAsString();
        // 아주 단순 파싱(테스트 편의): 첫 항목의 id를 정규식으로 추출
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\\"id\\\":(\\d+)").matcher(body);
        Long itemId = m.find() ? Long.parseLong(m.group(1)) : null;
        assert itemId != null;

        mockMvc.perform(delete("/api/cart/" + itemId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                // 모든 아이템의 id 배열을 가져와서
                .andExpect(jsonPath("$[*].id", not(hasItem(itemId.intValue()))));

    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("POST /api/cart → 유효성 오류(qauntity < 1)")
    void validationError() throws Exception{
        mockMvc.perform(post("/api/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(new AddToCartRequest(1L, 0))))
                .andExpect(status().is4xxClientError());
    }
}
