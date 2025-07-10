package com.e_commerceapp.clothshops.service.cart;

import com.e_commerceapp.clothshops.dto.CartDTO;
import com.e_commerceapp.clothshops.dto.UserDTO;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.mapper.CartMapper;
import com.e_commerceapp.clothshops.mapper.UserMapper;
import com.e_commerceapp.clothshops.model.Cart;
import com.e_commerceapp.clothshops.model.CartItem;
import com.e_commerceapp.clothshops.model.Product;
import com.e_commerceapp.clothshops.repository.CartItemRepository;
import com.e_commerceapp.clothshops.repository.CartRepository;
import com.e_commerceapp.clothshops.repository.UserRepository;
import com.e_commerceapp.clothshops.service.product.IProductService;
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

    private final IProductService productService;

    private final UserService userService;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final CartMapper cartMapper;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, IProductService productService, UserRepository userRepository, UserService userService, UserMapper userMapper, UserRepository userRepository1, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.userRepository = userRepository1;
        this.cartMapper = cartMapper;
    }


    public Cart getCart(Long id) {
        return cartRepository.findById(id).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no cart with id: " + id
                )
        );
    }


    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);

        return cart.getTotalPrice();
    }



    //TODO(There is an error here we need to handle it)
    // It show that the cart is not attach to user


    //here he path user and take id from him
 public Cart initializeCart(Long userId){
//    UserDTO user = userService.getUserById(userId);
//        CartDTO cart = new CartDTO();
//        user.setCart(cart);
//     cartRepository.save(cartMapper.createCartEntityFromCartDto(cart));
//     userRepository.save(userMapper.createUserEntityFromUserDto(user));
//     return cart;
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
         /*
        1. Get the cart with given id (done)
        2. Get the product (done)
        3. Check if product already in cart
        4. If yes, then increase the quantity with the requested quantity
        5. If no. then initiate a new CartItem entry.
         */
        Cart cart  = initializeCart(userId);
//        if (userService.getUserById(userId).getCart() == null){
//            cart =initializeCart(userId);
//        }else {
//            cart = getCartByUserId(userId);
////            cartMapper.createCartEntityFromCartDto(userService.getUserById(userId).getCart());
//        }

//        if (cartId == null) {
//            cart = new Cart();
//            cartRepository.save(cart);
//        } else {
//            cart = getCart(cartId);
//        }
        Product product = productService.getProductById(productId);


        CartItem cartItem = cart.getItems()
                .stream()
                .filter(
                        /*
                        in this filter we will loop through all cart item and from every cart item
                        we will get product, and then we get id of that product
                        Q:why we don not use product to compare directly?
                        it will work but with id will be more fast

                        then we compare productId with id in request if it is true then the item will remain in stream
                        if false item will remove,
                        and then we try to fount item or if there is no item (because all compare was false)

                        note findFirst()-> will return the first item in stream as item not product

                        then we will create new cart item and next will pass product to it
                         */
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
        //we need to add cartItem to cart to update total price
        cart.addItem(cartItem);

        //then we need to update cartItem as well as cart
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }


    public void removeCartItemFromCart(Long cartId, Long cartItemId) {
        Cart cart = getCart(cartId);
        CartItem cartItem = getCartItemFromCartIfItFound(cartItemId, cartId);
        cart.removeItem(cartItem);

        //we need to update cart
        cartRepository.save(cart);
    }


    public void updateCartItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = getCart(cartId);

        //       CartItem cartItem = //this is wrong ifPresent dose not return any thing
        cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()

                //ifPresent return void so you can not assign it to value
                .ifPresentOrElse(
                        item -> {
                            item.setQuantity(quantity);
                            //this one is redundant because i already has the product
                            //which mean i already has unitePrice of this product
                            //item.setUnitePrice(item.getProduct().getPrice());
                            item.setTotalPrice();
                        }, () -> {
                            //if item not exist will throw exception
                            throw new GlobalNotFoundException(
                                    "there is no product with id: " + productId + " in cart with id: " + cartId
                            );
                        });


        //here we will get every price for each item and then add all then but it to cart
        BigDecimal totalPrice = cart.getItems().stream().map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(totalPrice);

        //we need to update cart
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
