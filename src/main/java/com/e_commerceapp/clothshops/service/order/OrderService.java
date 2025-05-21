package com.e_commerceapp.clothshops.service.order;

import com.e_commerceapp.clothshops.dto.OrderDTO;
import com.e_commerceapp.clothshops.enums.OrderStatus;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.mapper.OrderMapper;
import com.e_commerceapp.clothshops.model.Cart;
import com.e_commerceapp.clothshops.model.OrderItem;
import com.e_commerceapp.clothshops.model.Orders;
import com.e_commerceapp.clothshops.model.Product;
import com.e_commerceapp.clothshops.repository.OrderRepository;
import com.e_commerceapp.clothshops.repository.ProductRepository;
import com.e_commerceapp.clothshops.service.cart.CartService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, CartService cartService, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.orderMapper = orderMapper;
    }


    public Orders placeOrder(Long userId) {

        Cart cart = cartService.getCartByUserId(userId);

        Orders order = createOrder(cart);

        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));

        order.setTotalPrice(getTotalPrice(orderItemList));

        Orders savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    private Orders createOrder(Cart cart) {
        Orders order = new Orders();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
//        order.setTotalPrice(cart.getTotalPrice());

        return order;

    }

    private List<OrderItem> createOrderItems(Orders order, Cart cart) {
        return cart.getItems().stream().map(
                item -> {
                    Product product = item.getProduct();
                    product.setInventory(product.getInventory() - item.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            order,
                            product,
                            item.getUnitePrice(),
                            item.getQuantity()
                    );
                }
        ).toList();
    }

    private BigDecimal getTotalPrice(List<OrderItem> orderItemList) {
        return orderItemList.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public OrderDTO getOrder(Long orderId) {
        return orderRepository.findById(orderId).map(this::convertToDTO).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no order with id: " + orderId
                )
        );
//        return convertToDTO(order);
    }

    public List<OrderDTO> getUserOrder(Long userId) {
        return orderRepository.findAllByUserId(userId)
                .stream().map(this::convertToDTO).toList();
    }

    private OrderDTO convertToDTO(Orders order) {

        return orderMapper.createOrderDTOFromOrder(order);
    }
}
