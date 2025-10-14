package com.furniture.shop.furniture_shop.service;

import com.furniture.shop.furniture_shop.controller.auth.dto.CartItemResponse;
import com.furniture.shop.furniture_shop.model.cart.CartItem;
import com.furniture.shop.furniture_shop.model.product.Product;
import com.furniture.shop.furniture_shop.repository.cart.CartItemRepostory;
import com.furniture.shop.furniture_shop.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartItemRepostory cartItemRepostory;
    private final ProductRepository productRepository;

    /**
     * 현재 로그인 유저의 장바구니 조회
     * @param userId
     * @return 장바구니 내 상품 정보
     */
    @Transactional
    public List<CartItemResponse> getCart(Long userId){
        return cartItemRepostory.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 장바구니에 상품 추가
     * @param userId
     * @param productId
     * @param quantity
     * @return 장바구니 내 상품 정보
     */
    @Transactional
    public List<CartItemResponse> upsertItem(Long userId, Long productId, Integer quantity){
        if(quantity == null || quantity <= 0){
            throw new IllegalArgumentException("quantity must be > 0");
        }
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        CartItem item = cartItemRepostory.findByUserIdAndProductId(userId, productId)
                .map(found -> {found.setQuantity(quantity); return found; })
                .orElseGet(() -> new CartItem(userId, productId, quantity));
        cartItemRepostory.save(item);
        return getCart(userId);
    }

    /**
     * 장바구니 아이템 삭제
     * @param userId
     * @param itemId
     */
    @Transactional
    public void deleteItem(Long userId, Long itemId) {
        CartItem owned = cartItemRepostory.findByUserIdAndId(userId, itemId)
                .orElseThrow(() -> new IllegalArgumentException("삭제 권한이 없거나 항목이 없습니다."));
        cartItemRepostory.delete(owned);
    }

    private CartItemResponse toResponse(CartItem cartItem){
        Product p = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new IllegalStateException("Product not found: " + cartItem.getProductId()));
        return new CartItemResponse(
                cartItem.getId(), p.getId(), p.getName(), p.getPrice(), p.getImageUrl(), cartItem.getQuantity()
        );
    }
}
