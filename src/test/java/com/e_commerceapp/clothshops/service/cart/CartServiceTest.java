package com.e_commerceapp.clothshops.service.cart;

import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.model.Cart;
import com.e_commerceapp.clothshops.model.CartItem;
import com.e_commerceapp.clothshops.model.User;
import com.e_commerceapp.clothshops.repository.CartItemRepository;
import com.e_commerceapp.clothshops.repository.CartRepository;
import com.e_commerceapp.clothshops.service.product.IProductService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {


    @InjectMocks
    private CartService underTest;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private IProductService productService;


    @Test
//    @Disabled
    void ifGetCartCallFindById() {
        //given
        Long id = 1L;
        Cart expectiedCart = new Cart();
        expectiedCart.setId(1L);

        //when
        when(cartRepository.findById(id)).thenReturn(Optional.of(expectiedCart));
        Cart cart = underTest.getCart(id);
        ArgumentCaptor<Long> idArgCaptor = ArgumentCaptor.forClass(Long.class);

        //then
        verify(cartRepository).findById(idArgCaptor.capture());

        assertThat(id).isEqualTo(idArgCaptor.getValue());

    }

    @Test
//    @Disabled
    void shouldGetCartThrowExceptionIfIdNotFound() {
        //given
        Long id = 1L;
        Cart expectiedCart = new Cart();
        expectiedCart.setId(1L);

//        given(cartRepository.findById(id)).willReturn(Optional.empty());
        when(cartRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getCart(id))
                .isInstanceOf(GlobalNotFoundException.class)
                .hasMessageContaining("there is no cart with id: " + id);

        verify(cartRepository, times(1)).findById(Mockito.any());

    }


    @Test
//    @Disabled
    void shouldClearCartCallDeleteAllByCartId() {
        //given
        Long id = 1L;
        Cart expectiedCart = new Cart();
        expectiedCart.setId(1L);

        //when
//        when(cartService.getCart(Mockito.any())).thenReturn(expectiedCart);
        /*
        you cant do this because you still testing cart service
         */
        when(cartRepository.findById(Mockito.any())).thenReturn(Optional.of(expectiedCart));
        underTest.clearCart(expectiedCart.getId());


        //then
        verify(cartItemRepository, times(1)).deleteAllByCartId(expectiedCart.getId());

        assert (expectiedCart.getItems().isEmpty());
    }

    @Test
//    @Disabled
    void shouldClearCartCallDeleteById() {
        //given
        Long id = 1L;
        Cart expectiedCart = new Cart();
        expectiedCart.setId(1L);

        //when
//        when(cartService.getCart(Mockito.any())).thenReturn(expectiedCart);
        /*
        you cant do this because you still testing cart service
         */
        when(cartRepository.findById(Mockito.any())).thenReturn(Optional.of(expectiedCart));
        underTest.clearCart(expectiedCart.getId());


        //then
        verify(cartRepository, times(1)).deleteById(expectiedCart.getId());

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
//    @Disabled
    void getTotalPrice_ShouldCallFindById() {
        //given
        Long id = 1L;
        Cart expectiedCart = new Cart();
        expectiedCart.setId(1L);

        //when
        when(cartRepository.findById(id)).thenReturn(Optional.of(expectiedCart));
        underTest.getTotalPrice(expectiedCart.getId());
        //then
        verify(cartRepository).findById(expectiedCart.getId());


    }

    @Test
//    @Disabled
    void getTotalPrice_ShouldReturnTotalPrice() {
        //given
        Long id = 1L;
        BigDecimal expectedTotalPrice = new BigDecimal(10000);
        Cart expectiedCart = new Cart();
        expectiedCart.setId(1L);
        expectiedCart.setTotalPrice(expectedTotalPrice);

        //when

        when(cartRepository.findById(id)).thenReturn(Optional.of(expectiedCart));
        BigDecimal actualTotalPrice = underTest.getTotalPrice(expectiedCart.getId());

        //then

        assertThat(actualTotalPrice).isEqualTo(expectedTotalPrice);


    }

    @Test
    @Disabled
    void addCartItemToCart() {
    }

    @Test
//    @Disabled
    void removeCartItemFromCart_ShouldCallFindById() {
        //given
        Long cartId = 1L;
        Long cartItemId = 1L;
        Cart expectiedCart = new Cart();
        expectiedCart.setId(cartId);
        CartItem expectedCartItem = new CartItem();
        expectedCartItem.setId(cartItemId);
        expectiedCart.addItem(expectedCartItem);

        //when
        when(cartRepository.findById(expectiedCart.getId())).thenReturn(Optional.of(expectiedCart));
        underTest.removeCartItemFromCart(expectiedCart.getId(), expectedCartItem.getId());
        //then
        assertThatThrownBy(() -> underTest.removeCartItemFromCart(expectiedCart.getId(), expectedCartItem.getId()))
                .isInstanceOf(GlobalNotFoundException.class)
                .hasMessageContaining("there is no cartItem with id: " + expectedCartItem.getId());

    }

    @Test
//    @Disabled
    void removeCartItemFromCart_ShouldRemoveItem() {
        //given
        Long cartId = 1L;
        Long cartItemId = 1L;
        Cart expectiedCart = new Cart();
        expectiedCart.setId(cartId);
        CartItem expectedCartItem = new CartItem();
        expectedCartItem.setId(cartItemId);
        expectiedCart.addItem(expectedCartItem);

        //when
        when(cartRepository.findById(expectiedCart.getId())).thenReturn(Optional.of(expectiedCart));
        underTest.removeCartItemFromCart(expectiedCart.getId(), expectedCartItem.getId());

        //then
        //first in same method
        //the other time in invoke of another method
        verify(cartRepository, times(2)).findById(expectiedCart.getId());
    }

    @Test
//    @Disabled
    void removeCartItemFromCart_shouldCallSave() {
        //given
        Long cartId = 1L;
        Long cartItemId = 1L;
        Cart expectiedCart = new Cart();
        expectiedCart.setId(cartId);
        CartItem expectedCartItem = new CartItem();
        expectedCartItem.setId(cartItemId);
        expectiedCart.addItem(expectedCartItem);

        //when
        when(cartRepository.findById(expectiedCart.getId())).thenReturn(Optional.of(expectiedCart));
        underTest.removeCartItemFromCart(expectiedCart.getId(), expectedCartItem.getId());

        //then
        //first in same method
        //the other time in invoke of another method
        verify(cartRepository, times(1)).save(expectiedCart);
    }

    @Test
    @Disabled
    void updateCartItemQuantity() {
    }

    @Test
//    @Disabled
    void shouldGetCartItemFromCartIfItFoundCallFindById() {
        //given
        Long cartId = 1L;
        Long cartItemId = 1L;
        Cart expectiedCart = new Cart();
        expectiedCart.setId(cartId);
        CartItem expectedCartItem = new CartItem();
        expectedCartItem.setId(cartItemId);
        expectiedCart.addItem(expectedCartItem);

        //when
        when(cartRepository.findById(Mockito.any())).thenReturn(Optional.of(expectiedCart));
        underTest.getCartItemFromCartIfItFound(cartItemId, expectiedCart.getId());

        //then

        verify(cartRepository, times(1)).findById(expectiedCart.getId());
    }

    @Test
//    @Disabled
    void shouldGetCartItemFromCartIfItFound() {
        //given
        Long cartId = 1L;
        Long cartItemId = 1L;
        Cart expectiedCart = new Cart();
        expectiedCart.setId(cartId);
        CartItem expectedCartItem = new CartItem();
        expectedCartItem.setId(cartItemId);
        expectiedCart.addItem(expectedCartItem);

        //when
        when(cartRepository.findById(Mockito.any())).thenReturn(Optional.of(expectiedCart));
        CartItem actualCartItem = underTest.getCartItemFromCartIfItFound(cartItemId, expectiedCart.getId());

        //then
        assertThat(expectedCartItem).isEqualTo(actualCartItem);
    }

    @Test
//    @Disabled
    void shouldGetCartItemFromCartThrowExceptionIfNotFound() {
        //given
        Long cartId = 1L;
        Long cartItemId = 1L;
        Cart expectiedCart = new Cart();
        expectiedCart.setId(cartId);
        CartItem expectedCartItem = new CartItem();
        expectedCartItem.setId(cartItemId);

        Set<CartItem> expectedCartItems = new HashSet<>();
        expectiedCart.setItems(expectedCartItems);
        //when
        when(cartRepository.findById(Mockito.any())).thenReturn(Optional.of(expectiedCart));


        //then
        assertThatThrownBy(() -> underTest.getCartItemFromCartIfItFound(expectedCartItem.getId(), cartId))
                .isInstanceOf(GlobalNotFoundException.class)
                .hasMessageContaining("there is no cartItem with id: " + cartItemId);
    }

    @Test
    void getCartByUserId_ShouldReturnCart() {
        //given
        Long cartId = 1L;
        Cart expectiedCart = new Cart();
        expectiedCart.setId(cartId);
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setCart(expectiedCart);

        //when
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(expectiedCart));
        Cart actualCart = underTest.getCartByUserId(user.getId());
        //then
        assertThat(actualCart).isEqualTo(expectiedCart);
    }

    @Test
    void getCartByUserId_ShouldThrowExceptionIfThereIsNoCart() {
        //given
        Long cartId = 1L;
        Cart expectiedCart = new Cart();
        expectiedCart.setId(cartId);
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
//        user.setCart(expectiedCart);

        //when
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> underTest.getCartByUserId(user.getId()))
                .isInstanceOf(GlobalNotFoundException.class)
                .hasMessageContaining("there is no cart attached to user with id: " + user.getId());
    }
}