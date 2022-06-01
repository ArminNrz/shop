package com.shop.mapper;

import com.shop.dto.auth.UserDTO;
import com.shop.dto.stockManager.StockManagerCreateBatchDTO;
import com.shop.dto.stockManager.StockManagerUpdateDTO;
import com.shop.dto.stockManager.StockManagerResponseDTO;
import com.shop.entity.AppUserStockManagerLog;
import com.shop.entity.AppUserStocksManager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserStockManagerMapper {

    @Mapping(source = "updateDTO.userId", target = "user.id")
    @Mapping(source = "updateDTO.total", target = "current")
    @Mapping(source = "updateDTO.total", target = "total")
    @Mapping(source = "updateDTO.forSale", target = "forSale")
    @Mapping(target = "willBuy", expression = "java( Long.valueOf(\"0\") )")
    AppUserStocksManager toEntity(StockManagerUpdateDTO updateDTO);

    @Mapping(source = "batchDTO.total", target = "total")
    @Mapping(source = "batchDTO.total", target = "current")
    @Mapping(target = "forSale", expression = "java( Long.valueOf(\"0\") )")
    @Mapping(target = "willBuy", expression = "java( Long.valueOf(\"0\") )")
    @Mapping(source = "appUser.id", target = "user.id")
    AppUserStocksManager toEntity(StockManagerCreateBatchDTO batchDTO, UserDTO appUser);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(source = "current", target = "lastCurrent")
    @Mapping(source = "forSale", target = "lastForSale")
    @Mapping(source = "total", target = "lastTotal")
    @Mapping(source = "willBuy", target = "lastWillBuy")
    @Mapping(source = "id", target = "stocksManager.id")
    AppUserStockManagerLog toLog(AppUserStocksManager entity);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.phoneNumber", target = "phoneNumber")
    StockManagerResponseDTO toResponseDTO(AppUserStocksManager appUserStocksManager);
}
