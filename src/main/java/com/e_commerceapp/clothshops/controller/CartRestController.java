package com.e_commerceapp.clothshops.controller;

import com.e_commerceapp.clothshops.model.Cart;
import com.e_commerceapp.clothshops.response.ApiResponse;
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


    //done
    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponse> getCartById(@PathVariable Long cartId) {
        Cart cart = cartService.getCart(cartId);
        return ResponseEntity.ok(new ApiResponse("Success", cart));
    }

    //done
    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok(new ApiResponse("clear success", null));
    }


    //done
    @DeleteMapping("/{cartId}/{cartItemId}")
    public ResponseEntity<ApiResponse> removeCartItemFromCart(@PathVariable Long cartId, @PathVariable Long cartItemId) {
        cartService.removeCartItemFromCart(cartId,cartItemId);
        return ResponseEntity.ok(new ApiResponse("clear success", null));
    }


    //done
    @GetMapping("/total-price/{cartId}")
    public ResponseEntity<ApiResponse> getCartTotalPrice(@PathVariable Long cartId) {
        BigDecimal totalPrice = cartService.getTotalPrice(cartId);
        return ResponseEntity.ok(new ApiResponse("get total price success", totalPrice));
    }


    //done
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCartIfExistIfNotCreateNewOne(@RequestParam(required = false) Long cartId,
                                                                             @RequestParam Long productId,
                                                                             @RequestParam int quantity) {
        cartService.addCartItemToCart(cartId, productId, quantity);
        return ResponseEntity.ok(new ApiResponse("add success", null));

    }


    //done
    @PutMapping("/update/{cartId}")
    public ResponseEntity<ApiResponse> updateCartItemQuantity(@PathVariable Long cartId,
                                                                             @RequestParam Long productId,
                                                                             @RequestParam int quantity) {
        cartService.updateCartItemQuantity(cartId, productId, quantity);
        return ResponseEntity.ok(new ApiResponse("update success", null));

    }

}
