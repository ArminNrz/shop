package com.shop.mapper;

import com.shop.dto.stockManager.StockManagerCreateBatchDTO;
import com.shop.dto.stockManager.StockManagerCreateDTO;
import com.shop.entity.AppUser;
import com.shop.entity.AppUserStockManagerLog;
import com.shop.entity.AppUserStocksManager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserStockManagerMapper {

    @Mapping(source = "createDTO.userId", target = "user.id")
    @Mapping(source = "createDTO.total", target = "current")
    @Mapping(source = "createDTO.total", target = "total")
    @Mapping(target = "forSale", expression = "java( Long.valueOf(\"0\") )")
    @Mapping(target = "willBuy", expression = "java( Long.valueOf(\"0\") )")
    AppUserStocksManager toEntity(StockManagerCreateDTO createDTO);

    @Mapping(source = "batchDTO.total", target = "total")
    @Mapping(source = "batchDTO.total", target = "current")
    @Mapping(target = "forSale", expression = "java( Long.valueOf(\"0\") )")
    @Mapping(target = "willBuy", expression = "java( Long.valueOf(\"0\") )")
    @Mapping(source = "appUser.id", target = "user.id")
    AppUserStocksManager toEntity(StockManagerCreateBatchDTO batchDTO, AppUser appUser);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(source = "current", target = "lastCurrent")
    @Mapping(source = "forSale", target = "lastForSale")
    @Mapping(source = "total", target = "lastTotal")
    @Mapping(source = "willBuy", target = "lastWillBuy")
    @Mapping(source = "id", target = "stocksManager.id")
    AppUserStockManagerLog toLog(AppUserStocksManager entity);
}
