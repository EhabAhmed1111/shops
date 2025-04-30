package com.e_commerceapp.clothshops.service.cart;

import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.model.Cart;
import com.e_commerceapp.clothshops.model.CartItem;
import com.e_commerceapp.clothshops.model.Product;
import com.e_commerceapp.clothshops.repository.CartItemRepository;
import com.e_commerceapp.clothshops.repository.CartRepository;
import com.e_commerceapp.clothshops.repository.ProductRepository;
import com.e_commerceapp.clothshops.service.product.IProductService;
import org.springframework.stereotype.Service;

@Service
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;

    public CartItemService(CartItemRepository cartItemRepository, CartRepository cartRepository, IProductService productService, ICartService cartService) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.cartService = cartService;
    }

    @Override
    public void addCartItemToCart(Long cartId, Long productId, int quantity) {
         /*
        1. Get the cart with given id (done)
        2. Get the product (done)
        3. Check if product already in cart
        4. If yes, then increase the quantity with the requested quantity
        5. If no. then initiate a new CartItem entry.
         */
        Cart cart = cartService.getCart(cartId);
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

    @Override
    public void removeCartItemFromCart(Long cartId, Long cartItemId) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = getCartItemFromCartIfItFound(cartItemId, cartId);
        cart.removeItem(cartItem);

        //we need to update cart
        cartRepository.save(cart);
    }

    @Override
    public void updateCartItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);

        //        CartItem cartItem = //this is wrong ifPresent dose not return any thing
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

        //we need to update cart
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItemFromCartIfItFound(Long cartItemId, Long cartId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new GlobalNotFoundException("there is no cartItem with id: " + cartItemId));
    }
}
