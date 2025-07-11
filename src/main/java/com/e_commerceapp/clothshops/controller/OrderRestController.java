package com.e_commerceapp.clothshops.controller;


import com.e_commerceapp.clothshops.dto.OrderDTO;
import com.e_commerceapp.clothshops.mapper.OrderMapper;
import com.e_commerceapp.clothshops.model.Orders;
import com.e_commerceapp.clothshops.response.ApiResponse;
import com.e_commerceapp.clothshops.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderRestController {

    private final OrderService orderService;

    private final OrderMapper orderMapper;

    public OrderRestController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    //what is the different between (path variable , request body and request param) in usage
    //should this become pathVariable or RequestParam
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            /*
            ->instead of doing multi @JsonIgnore we can just make DTO to return what we actually need
            with causing loops
             */
            OrderDTO order = orderService.placeOrder(userId);
            return ResponseEntity.ok(new ApiResponse("order placed", order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error occur", e.getMessage()));
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDTO orderDTO = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("order fetched", orderDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error occur", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        try {
            List<OrderDTO> orderDTOS = orderService.getUserOrder(userId);
            return ResponseEntity.ok(new ApiResponse("user's orders fetched", orderDTOS));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error occur", e.getMessage()));
        }
    }
}
