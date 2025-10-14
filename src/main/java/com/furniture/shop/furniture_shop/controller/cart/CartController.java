package com.furniture.shop.furniture_shop.controller.cart;

import com.furniture.shop.furniture_shop.controller.auth.dto.AddToCartRequest;
import com.furniture.shop.furniture_shop.controller.auth.dto.CartItemResponse;
import com.furniture.shop.furniture_shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    /**
     * 현재 로그인 유저 장바구니 조회
     * @param principal
     * @return 장바구니
     */
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(Principal principal){
        Long userId = resolveUserId(principal);
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    /**
     * 장바구니 담기 (upsert)
     * @param request
     * @param principal
     * @return 장바구니에 상품 추가
     */
    @PostMapping
    public ResponseEntity<List<CartItemResponse>> add(@RequestBody AddToCartRequest request, Principal principal) {
        Long userId = resolveUserId(principal);
        return ResponseEntity.ok(cartService.upsertItem(userId, request.productId(), request.quantiy()));
    }

    /**
     * 아이템 삭제
     * @param itemId
     * @param principal
     * @return 장바구니에서 아이템 삭제
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(@PathVariable("itemId") Long itemId, Principal principal) {
        Long userId = resolveUserId(principal);
        cartService.deleteItem(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    private Long resolveUserId(Principal principal){
        // 예시: principal.getName()을 userId로 쓰거나, username을 ID로 변환하는 로직 필요
        // 실제 환경에 맞게 SecurityUser의 id를 꺼내도록 변경하세요
        try {
            return Long.parseLong(principal.getName());
        } catch (Exception e) {
            throw new IllegalStateException("유저 식별자를 확인할 수 없습니다. principar=" + principal);
        }
    }
}
