package com.furniture.shop.furniture_shop.controller.auth.dto;

public record CartItemResponse(
        Long id,        // cart_items.id
        Long productId, // product_id
        String name,
        Long price,
        String imageUrl,
        Integer quantity
) {
}
