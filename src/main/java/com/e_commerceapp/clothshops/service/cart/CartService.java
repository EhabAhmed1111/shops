package com.e_commerceapp.clothshops.service.cart;

import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.mapper.CartMapper;
import com.e_commerceapp.clothshops.mapper.UserMapper;
import com.e_commerceapp.clothshops.data.model.Cart;
import com.e_commerceapp.clothshops.data.model.CartItem;
import com.e_commerceapp.clothshops.data.model.Product;
import com.e_commerceapp.clothshops.data.repository.CartItemRepository;
import com.e_commerceapp.clothshops.data.repository.CartRepository;
import com.e_commerceapp.clothshops.data.repository.UserRepository;
import com.e_commerceapp.clothshops.service.product.ProductService;
import com.e_commerceapp.clothshops.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductService productService;

    private final UserService userService;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final CartMapper cartMapper;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductService productService, UserRepository userRepository, UserService userService, UserMapper userMapper, UserRepository userRepository1, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.userRepository = userRepository1;
        this.cartMapper = cartMapper;
    }


    public Cart getCart(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no cart with id: " + cartId
                )
        );
    }


    /*
        TODO(fixing this method so cart will deleted)
     */
    @Transactional
    public void clearCart(Long cartId) {
        Cart cart = getCart(cartId);
        cartItemRepository.deleteAllByCartId(cartId);
        cart.getItems().clear();//Set

        cartRepository.deleteById(cartId);
    }

    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);

        return cart.getTotalPrice();
    }


 public Cart initializeCart(Long userId){
     return Optional.ofNullable(getCartByUserId(userId)).orElseGet(
             ()->{
                 Cart cart = new Cart();
                 cart.setUser(userMapper.createUserEntityFromUserDto(userService.getUserById(userId)));
                 return cartRepository.save(cart);
             });
 }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public void addCartItemToCart(Long userId, Long productId, int quantity) {
        Cart cart  = initializeCart(userId);
        Product product = productService.getProductById(productId);


        CartItem cartItem = cart.getItems()
                .stream()
                .filter(
                        item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setUnitePrice(product.getPrice());
            cartItem.setQuantity(quantity);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);

        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }


    public void removeCartItemFromCart(Long cartId, Long cartItemId) {
        Cart cart = getCart(cartId);
        CartItem cartItem = getCartItemFromCartIfItFound(cartItemId, cartId);
        cart.removeItem(cartItem);

        cartRepository.save(cart);
    }


    public void updateCartItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = getCart(cartId);
        cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        item -> {
                            item.setQuantity(quantity);
                            item.setTotalPrice();
                        }, () -> {
                            throw new GlobalNotFoundException(
                                    "there is no product with id: " + productId + " in cart with id: " + cartId
                            );
                        });


        BigDecimal totalPrice = cart.getItems().stream().map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(totalPrice);

        cartRepository.save(cart);
    }


    public CartItem getCartItemFromCartIfItFound(Long cartItemId, Long cartId) {
        Cart cart = getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new GlobalNotFoundException("there is no cartItem with id: " + cartItemId));
    }


}
