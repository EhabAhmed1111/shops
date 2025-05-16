package com.e_commerceapp.clothshops.service.cart;

import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.model.Cart;
import com.e_commerceapp.clothshops.model.CartItem;
import com.e_commerceapp.clothshops.model.Product;
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
        expectiedCart.setId(id);

        //when

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
        expectiedCart.setId(id);

        //when

        when(cartRepository.findById(Mockito.any())).thenReturn(Optional.of(expectiedCart));
        underTest.clearCart(expectiedCart.getId());


        //then
        verify(cartRepository, times(1)).deleteById(expectiedCart.getId());

    }

    @Test
    void updateCart_ShouldThrowExceptionIfCartNotThere() {
//        given
        Long cartId = 1L;
        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);

//        when
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());


//        then
        assertThatThrownBy(() -> underTest.updateCartItemQuantity(cartId, product.getId(), 1))
                .isInstanceOf(GlobalNotFoundException.class)
                .hasMessageContaining("there is no cart with id: " + cartId);


    }

    @Test
    void updateCart_ShouldUpdateQuantityIfCartItemInCart() {
//        given
        Long productId = 1L;
        Long expectedId = 1L;
        Long cartItemId = 1L;
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cart.setId(expectedId);
        Product product = new Product();
        product.setId(productId);

        product.setPrice(new BigDecimal(300));
        int quantity = 2;
        int oldQuantity = 5;
        cartItem.setProduct(product);
        cartItem.setQuantity(oldQuantity);
        cartItem.setUnitePrice(product.getPrice());
        cart.addItem(cartItem);

//        when
        when(cartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));

        underTest.updateCartItemQuantity(cart.getId(), product.getId(), quantity);

//        then
        assertThat(cartItem.getQuantity()).isEqualTo(quantity);


    }

    @Test
    void updateCart_ShouldThrowExceptionIfCartItemNotInCart() {
//        given
        Long productId = 1L;
        Long expectedId = 1L;
        Cart cart = new Cart();
        cart.setId(expectedId);
        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal(300));
        int quantity = 2;


//        when
        when(cartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));


//        then
        assertThatThrownBy(() -> underTest.updateCartItemQuantity(cart.getId(), product.getId(), quantity))
                .isInstanceOf(GlobalNotFoundException.class)
                .hasMessageContaining("there is no product with id: " + product.getId() + " in cart with id: " + cart.getId());


    }

    @Test
    void updateCart_ShouldRecalculateTotalPriceAfterUpdateQuantity() {
//        given
        Long productId = 1L;
        Long expectedId = 1L;
        Long cartItemId = 1L;
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cart.setId(expectedId);
        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal(300));
        int quantity = 2;
        int oldQuantity = 5;
        cartItem.setProduct(product);
        cartItem.setQuantity(oldQuantity);
        cartItem.setUnitePrice(product.getPrice());
        cart.addItem(cartItem);

//        when
        when(cartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));

        underTest.updateCartItemQuantity(cart.getId(), product.getId(), quantity);

        BigDecimal expectedTotalPrice = cart.getItems().stream().map(item ->
             item.getUnitePrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        ).reduce(BigDecimal.ZERO, BigDecimal::add);
//        then
        assertThat(cart.getTotalPrice()).isEqualTo(expectedTotalPrice);

    }

    @Test
    void updateCart_ShouldCheckIfCartSavedOnceIfCartThere() {
//        given
        Long productId = 1L;
        Long expectedId = 1L;
        Long cartItemId = 1L;
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cart.setId(expectedId);
        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal(300));
        int quantity = 2;
        int oldQuantity = 5;
        cartItem.setProduct(product);
        cartItem.setQuantity(oldQuantity);
        cartItem.setUnitePrice(product.getPrice());
        cart.addItem(cartItem);

//        when
        when(cartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));
        underTest.updateCartItemQuantity(cart.getId(), product.getId(), quantity);
        ArgumentCaptor<Cart> cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);
//        then
        verify(cartRepository, times(1)).save(cartArgumentCaptor.capture());
        assertThat(cart).isEqualTo(cartArgumentCaptor.getValue());

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
//    @Disabled
    void addCartItemToCart_ShouldInitiateNewCartIfIdNull() {
        //given
        Long productId = 1L;
//        Long expectedId = 1L;
        Product product = new Product();
        product.setId(productId);
//        product.setInventory(5);
//        product.setName("phone");
        product.setPrice(new BigDecimal(300));
        int quantity = 2;

        when(productService.getProductById(product.getId())).thenReturn(product);
//        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation ->{
//            Cart cart = invocation.getArgument(0);
//            cart.setId(expectedId);
//            return cart;
//        });
        //when
        underTest.addCartItemToCart(null, product.getId(), quantity);

        //then
        //for initialize
        verify(cartRepository, times(2)).save(any(Cart.class));

//        verify(cartItemRepository).save(argThat(
//                item ->
//                    item.getQuantity() == quantity &&
//                            item.getProduct().equals(product)
//        ));


    }

    @Test
//    @Disabled
    void addCartItemToCart_ShouldUpdateCartIfThereId() {
        //given
        Long productId = 1L;
        Long expectedId = 1L;
        Cart cart = new Cart();
        cart.setId(expectedId);
        Product product = new Product();
        product.setId(productId);
//        product.setInventory(5);
//        product.setName("phone");
        product.setPrice(new BigDecimal(300));
        int quantity = 2;

        when(productService.getProductById(product.getId())).thenReturn(product);
        when(cartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));
//        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation ->{
//            Cart cart = invocation.getArgument(0);
//            cart.setId(expectedId);
//            return cart;
//        });
        //when
        underTest.addCartItemToCart(cart.getId(), product.getId(), quantity);

        //then
        //cart already there
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(cartRepository, times(1)).findById(cart.getId());

//        verify(cartItemRepository).save(argThat(
//                item ->
//                    item.getQuantity() == quantity &&
//                            item.getProduct().equals(product)
//        ));


    }

    @Test
//    @Disabled
    void addCartItemToCart_ShouldCallGetProductById() {
        //given
        Long productId = 1L;
//        Long expectedId = 1L;
        Product product = new Product();
        product.setId(productId);
//        product.setInventory(5);
//        product.setName("phone");
        product.setPrice(new BigDecimal(300));
        int quantity = 2;

        when(productService.getProductById(product.getId())).thenReturn(product);
//        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation ->{
//            Cart cart = invocation.getArgument(0);
//            cart.setId(expectedId);
//            return cart;
//        });
        //when
        underTest.addCartItemToCart(null, product.getId(), quantity);

        //then
        verify(productService).getProductById(productId);

    }

    @Test
    void addCartItemToCart_ShouldAddNewCartItemIfProductNotThere() {
        //       TODO()
        //given
        Long productId = 1L;
        Long expectedId = 1L;
        Cart cart = new Cart();
        cart.setId(expectedId);
        Product product = new Product();
        product.setId(productId);
//        product.setInventory(5);
//        product.setName("phone");
        product.setPrice(new BigDecimal(300));
        int quantity = 2;

        when(productService.getProductById(product.getId())).thenReturn(product);
        when(cartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));
//        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation ->{
//            Cart cart = invocation.getArgument(0);
//            cart.setId(expectedId);
//            return cart;
//        });
        //when
        int sizeOfCartBefore = cart.getItems().size();
        underTest.addCartItemToCart(cart.getId(), product.getId(), quantity);
        int sizeOfCartAfter = cart.getItems().size();
        //then
        assertThat( sizeOfCartAfter).isEqualTo( sizeOfCartBefore+ 1);
//        assertThat(cart.getItems().stream().filter(item -> {
//            return item.getProduct().equals(product);
//        }).findFirst()).isNotNull();
//        verify(cartItemRepository).save(argThat(
//                item ->
//                    item.getQuantity() == quantity &&
//                            item.getProduct().equals(product)
//        ));


    }

    @Test
    void addCartItemToCart_ShouldUpdateCartItemIfProductThere() {
        //       TODO()
        //given
        Long productId = 1L;
        Long expectedId = 1L;
        Long cartItemId = 1L;
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cart.setId(expectedId);
        Product product = new Product();
        product.setId(productId);
//        product.setInventory(5);
//        product.setName("phone");
        product.setPrice(new BigDecimal(300));
        int quantity = 2;
        int oldQuantity = 5;
        cartItem.setProduct(product);
        cartItem.setQuantity(oldQuantity);
        cartItem.setUnitePrice(new BigDecimal(300));
        cart.addItem(cartItem);

        when(productService.getProductById(product.getId())).thenReturn(product);
        when(cartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));
//        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation ->{
//            Cart cart = invocation.getArgument(0);
//            cart.setId(expectedId);
//            return cart;
//        });
        //when
        underTest.addCartItemToCart(cart.getId(), product.getId(), quantity);
        //then
        assertThat(cartItem.getQuantity()).isEqualTo(oldQuantity + quantity);
//        assertThat(cart.getItems().stream().filter(item -> {
//            return item.getProduct().equals(product);
//        }).findFirst()).isNotNull();
//        verify(cartItemRepository).save(argThat(
//                item ->
//                    item.getQuantity() == quantity &&
//                            item.getProduct().equals(product)
//        ));


    }

    @Test
    void addCartItemToCart_ShouldUpdateTotalPriceIfProductThere() {
        //       TODO()
        //given
        Long productId = 1L;
        Long expectedId = 1L;
        Long cartItemId = 1L;
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cart.setId(expectedId);
        Product product = new Product();
        product.setId(productId);
//        product.setInventory(5);
//        product.setName("phone");
        product.setPrice(new BigDecimal(300));
        int quantity = 2;
        int oldQuantity = 5;
        cartItem.setProduct(product);
        cartItem.setQuantity(oldQuantity);
        cartItem.setUnitePrice(new BigDecimal(300));
        cart.addItem(cartItem);

        when(productService.getProductById(product.getId())).thenReturn(product);
        when(cartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));
//        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation ->{
//            Cart cart = invocation.getArgument(0);
//            cart.setId(expectedId);
//            return cart;
//        });
        //when
        underTest.addCartItemToCart(cart.getId(), product.getId(), quantity);
        //then
        assertThat(cart.getTotalPrice()).isEqualTo(cartItem.getUnitePrice().multiply(BigDecimal.valueOf(quantity + oldQuantity)));
//        assertThat(cart.getItems().stream().filter(item -> {
//            return item.getProduct().equals(product);
//        }).findFirst()).isNotNull();
//        verify(cartItemRepository).save(argThat(
//                item ->
//                    item.getQuantity() == quantity &&
//                            item.getProduct().equals(product)
//        ));


    }

    @Test
    void addCartItemToCart_ShouldThrowExceptionIfProductNotThere() {
        //given
        Long productId = 999L;
        Long expectedId = 1L;
        Cart cart = new Cart();

        cart.setId(expectedId);

        int quantity = 2;


        when(productService.getProductById(productId)).thenThrow(new GlobalNotFoundException(
                "there is no product with id:" + productId
        ));
        when(cartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));

        //when//then
        assertThatThrownBy(() -> underTest.addCartItemToCart(cart.getId(), productId, quantity))
                .isInstanceOf(GlobalNotFoundException.class)
                .hasMessageContaining("there is no product with id:" + productId);


    }

    @Test
    @Disabled
    void addCartItemToCart_IfCartItemNullQuantityMustEqualAddedOne() {
        //       TODO()
        // i cant do it
        //given
        Long productId = 1L;
        Long expectedId = 1L;
//        Long cartItemId = 1L;
        Cart cart = new Cart();
//        CartItem cartItem = new CartItem();
//        cartItem.setId(cartItemId);
        cart.setId(expectedId);
        Product product = new Product();
        product.setId(productId);
//        product.setInventory(5);
//        product.setName("phone");
        product.setPrice(new BigDecimal(300));
        int quantity = 2;
//        int oldQuantity = 5;
//        cartItem.setProduct(product);
//        cartItem.setQuantity(oldQuantity);
//        cartItem.setUnitePrice(new BigDecimal(300));
//        cart.addItem(cartItem);

        when(productService.getProductById(product.getId())).thenReturn(product);
        when(cartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));
//        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation ->{
//            Cart cart = invocation.getArgument(0);
//            cart.setId(expectedId);
//            return cart;
//        });
        //when
        underTest.addCartItemToCart(cart.getId(), product.getId(), quantity);

        //then

//        assertThat();
//        assertThat(cart.getItems().stream().filter(item -> {
//            return item.getProduct().equals(product);
//        }).findFirst()).isNotNull();
//        verify(cartItemRepository).save(argThat(
//                item ->
//                    item.getQuantity() == quantity &&
//                            item.getProduct().equals(product)
//        ));


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

        //when
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> underTest.getCartByUserId(user.getId()))
                .isInstanceOf(GlobalNotFoundException.class)
                .hasMessageContaining("there is no cart attached to user with id: " + user.getId());
    }
}