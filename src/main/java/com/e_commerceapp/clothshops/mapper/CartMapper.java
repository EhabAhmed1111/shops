package com.e_commerceapp.clothshops.mapper;


import com.e_commerceapp.clothshops.dto.CartDTO;
import com.e_commerceapp.clothshops.model.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CartMapper.class})
public interface CartMapper {

//    @Mapping(source = "items", target = "items")
    CartDTO createCartDtoFromCartEntity(Cart cart);
    Cart createCartEntityFromCartDto(CartDTO cart);
}
