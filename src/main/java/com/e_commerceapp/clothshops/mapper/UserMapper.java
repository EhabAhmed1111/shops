package com.e_commerceapp.clothshops.mapper;

import com.e_commerceapp.clothshops.dto.UserDTO;
import com.e_commerceapp.clothshops.model.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {CartMapper.class, OrderMapper.class})
public interface UserMapper {

    UserDTO createUserDtoFromUserEntity(Users user);
    Users createUserEntityFromUserDto(UserDTO user);
}
