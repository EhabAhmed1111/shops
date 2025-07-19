package com.e_commerceapp.clothshops.mapper;


import com.e_commerceapp.clothshops.data.dto.CartItemDTO;
import com.e_commerceapp.clothshops.data.model.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {ProductMapper.class})
public interface CartItemMapper {

     CartItemDTO createCartItemDtoFromCartItem(CartItem cartItem);
}
