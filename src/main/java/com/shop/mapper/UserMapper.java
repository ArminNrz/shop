package com.shop.mapper;

import com.shop.dto.auth.UserCreateDTO;
import com.shop.dto.auth.UserDTO;
import com.shop.dto.stockManager.StockManagerCreateBatchDTO;
import com.shop.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    AppUser toEntity(UserCreateDTO dto);

    UserDTO toDto(AppUser user);

    @Mapping(target = "password", expression = "java( String.valueOf(\"1234\") )")
    UserCreateDTO toCreateDTO(StockManagerCreateBatchDTO stockBatchDTO);
}
