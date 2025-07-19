package com.e_commerceapp.clothshops.mapper;


import com.e_commerceapp.clothshops.data.dto.OrderDTO;
import com.e_commerceapp.clothshops.data.model.Orders;
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
