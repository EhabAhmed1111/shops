package com.e_commerceapp.clothshops.mapper;


import com.e_commerceapp.clothshops.data.dto.CartDTO;
import com.e_commerceapp.clothshops.data.model.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CartMapper.class})
public interface CartMapper {

    CartDTO createCartDtoFromCartEntity(Cart cart);
    Cart createCartEntityFromCartDto(CartDTO cart);
}
