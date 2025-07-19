package com.e_commerceapp.clothshops.rest.controller;

import com.e_commerceapp.clothshops.data.model.Cart;
import com.e_commerceapp.clothshops.rest.response.ApiResponse;
import com.e_commerceapp.clothshops.service.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("${api.prefix}/carts")
public class CartRestController {
    private final CartService cartService;

    public CartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponse> getCartById(@PathVariable Long cartId) {
        Cart cart = cartService.getCart(cartId);
        return ResponseEntity.ok(new ApiResponse("Success", cart));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok(new ApiResponse("clear success", null));
    }

    @DeleteMapping("/{cartId}/{cartItemId}")
    public ResponseEntity<ApiResponse> removeCartItemFromCart(@PathVariable Long cartId, @PathVariable Long cartItemId) {
        cartService.removeCartItemFromCart(cartId,cartItemId);
        return ResponseEntity.ok(new ApiResponse("clear success", null));
    }

    @GetMapping("/total-price/{cartId}")
    public ResponseEntity<ApiResponse> getCartTotalPrice(@PathVariable Long cartId) {
        BigDecimal totalPrice = cartService.getTotalPrice(cartId);
        return ResponseEntity.ok(new ApiResponse("get total price success", totalPrice));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCartIfExistIfNotCreateNewOne(@RequestParam Long userId,
                                                                             @RequestParam Long productId,
                                                                             @RequestParam int quantity) {
        cartService.addCartItemToCart(userId, productId, quantity);
        return ResponseEntity.ok(new ApiResponse("add success", null));

    }


    @PutMapping("/update/{cartId}")
    public ResponseEntity<ApiResponse> updateCartItemQuantity(@PathVariable Long cartId,
                                                                             @RequestParam Long productId,
                                                                             @RequestParam int quantity) {
        cartService.updateCartItemQuantity(cartId, productId, quantity);
        return ResponseEntity.ok(new ApiResponse("update success", null));

    }

}
