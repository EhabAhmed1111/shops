package com.e_commerceapp.clothshops.service.cart;

import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.model.Cart;
import com.e_commerceapp.clothshops.model.CartItem;
import com.e_commerceapp.clothshops.repository.CartItemRepository;
import com.e_commerceapp.clothshops.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartService implements ICartService{

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }


    @Override
    public Cart getCart(Long id) {
        return cartRepository.findById(id).orElseThrow(
                ()->new GlobalNotFoundException(
                        "there is no cart with id: " + id
                )
        );
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public Cart updateCart(Long CartId, Long cartItemId) {
        return null;
    }

    @Override
    public Cart initializeCart(Cart cart) {
        return null;
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);

        return cart.getTotalPrice();
    }
}
