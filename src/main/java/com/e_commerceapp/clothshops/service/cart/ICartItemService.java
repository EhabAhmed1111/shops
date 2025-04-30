package com.e_commerceapp.clothshops.service.cart;

import com.e_commerceapp.clothshops.model.CartItem;

public interface ICartItemService {

    void addCartItemToCart(Long cartId,Long productId,int quantity);

    void removeCartItemFromCart(Long cartId, Long cartItemId);

    void updateCartItemQuantity(Long cartId,Long productId,int quantity );

    CartItem getCartItemFromCartIfItFound(Long cartItemId,Long cartId);
}
