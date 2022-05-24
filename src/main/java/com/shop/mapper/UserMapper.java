package com.shop.mapper;

import com.shop.dto.auth.UserCreateDTO;
import com.shop.dto.auth.UserDTO;
import com.shop.entity.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    AppUser toEntity(UserCreateDTO dto);

    UserDTO toDto(AppUser user);
}
