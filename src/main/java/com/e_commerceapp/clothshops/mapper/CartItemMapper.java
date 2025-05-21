package com.e_commerceapp.clothshops.mapper;


import com.e_commerceapp.clothshops.dto.CartItemDTO;
import com.e_commerceapp.clothshops.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {ProductMapper.class})
public interface CartItemMapper {

//    @Mapping(source = "product", target = "product")
     CartItemDTO createCartItemDtoFromCartItem(CartItem cartItem);
}
