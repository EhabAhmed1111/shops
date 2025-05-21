package com.e_commerceapp.clothshops.service.cart;

import com.e_commerceapp.clothshops.repository.CartItemRepository;
import com.e_commerceapp.clothshops.repository.CartRepository;
import com.e_commerceapp.clothshops.service.product.IProductService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {


    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private IProductService productService;


}