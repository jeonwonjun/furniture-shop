package com.furniture.shop.furniture_shop.repository.cart;

import com.furniture.shop.furniture_shop.model.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepostory extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserIdOrderByCreatedAtDesc(Long userID);
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

//    @Query("select c from CartItem c where c.userId = :userId and c.id = :itemId")
    Optional<CartItem> findByUserIdAndId(Long userId, Long itemId);
}
