package com.e_commerceapp.clothshops.mapper;


import com.e_commerceapp.clothshops.dto.OrderDTO;
import com.e_commerceapp.clothshops.model.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mappings({
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "orderStatus", target = "orderStatus"),
            @Mapping(source = "orderItems", target = "orderItemDTOS")
    })
    OrderDTO createOrderDTOFromOrder(Orders order);
}


//final private OrderItemMapper orderItemMapper;
//OrderDTO createOrderDTOFromOrder(Order order, User user){
////    private Long id;
////    private Long userId;
////    private LocalDateTime orderDate;
////    private BigDecimal totalPrice;
////    private String orderStatus;
////    private List<OrderItemDTO> orderItemDTOS;
//
//    OrderDTO orderDTO = new OrderDTO();
//    orderDTO.setOrderItemDTOS(orderItemMapper.createListOrderItemDTO(order.getOrderItems()));
//
//
//}