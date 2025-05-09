package com.e_commerceapp.clothshops.service.cart;

import com.e_commerceapp.clothshops.model.Cart;
import com.e_commerceapp.clothshops.repository.CartItemRepository;
import com.e_commerceapp.clothshops.repository.CartRepository;
import com.e_commerceapp.clothshops.service.product.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    private CartService cartService;

    @Mock
    private  CartRepository cartRepository;

    @Mock
    private  CartItemRepository cartItemRepository;

    @Mock
    private  IProductService productService;


    @BeforeEach
    void setUp() {
        cartService =new CartService(cartRepository,cartItemRepository,productService);
    }

    @Test
    @Disabled
    void getCart() {
        //given


        //when
//        cartService.getCart();


        //then
//verify(cartRepository).findById();

    }

    @Test
    @Disabled
    void clearCart() {
    }

    @Test
    @Disabled
    void updateCart() {
    }

    @Test
    @Disabled
    void initializeCart() {
    }

    @Test
    @Disabled
    void getTotalPrice() {
    }

    @Test
    @Disabled
    void addCartItemToCart() {
    }

    @Test
    @Disabled
    void removeCartItemFromCart() {
    }

    @Test
    @Disabled
    void updateCartItemQuantity() {
    }

    @Test
    @Disabled
    void getCartItemFromCartIfItFound() {
    }
}