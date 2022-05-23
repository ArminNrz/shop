package com.shop.mapper;

import com.shop.dto.UserCreateDTO;
import com.shop.dto.UserDTO;
import com.shop.entity.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    AppUser toEntity(UserCreateDTO dto);

    UserDTO toDto(AppUser user);
}
