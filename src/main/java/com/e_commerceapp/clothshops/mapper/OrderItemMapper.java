package com.e_commerceapp.clothshops.mapper;

import com.e_commerceapp.clothshops.data.dto.OrderItemDTO;
import com.e_commerceapp.clothshops.data.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mappings({
            @Mapping(source ="product.id" ,target = "productId"),
            @Mapping(source ="product.name" ,target = "productName"),
            @Mapping(source ="product.brand" ,target = "productBrand")
    })
    OrderItemDTO createOrderItemDTO(OrderItem orderItem);


}
