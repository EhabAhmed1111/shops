package com.e_commerceapp.clothshops.service.cart;

import com.e_commerceapp.clothshops.model.Cart;
import com.e_commerceapp.clothshops.model.CartItem;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long id);

    void clearCart(Long id);

    Cart updateCart(Long cartId, Long cartItemId);

    Cart initializeCart(Cart cart);

    BigDecimal getTotalPrice(Long id);
}
