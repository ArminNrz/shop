package com.shop.mapper;

import com.shop.dto.saleStock.SaleStockCreateDTO;
import com.shop.dto.saleStock.SaleStockResponseDTO;
import com.shop.dto.saleStock.SaleStockUpdateDTO;
import com.shop.entity.SaleStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleStockMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId", target = "user.id")
    SaleStock toEntity(SaleStockCreateDTO createDTO);

    @Mapping(source = "entity.id", target = "id")
    @Mapping(source = "updateDTO.stockCount", target = "stockCount")
    @Mapping(source = "updateDTO.unitPrice", target = "unitPrice")
    @Mapping(source = "entity.userId", target = "user.id")
    @Mapping(source = "entity.userId", target = "userId")
    SaleStock toEntity(SaleStockUpdateDTO updateDTO, SaleStock entity);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.phoneNumber", target = "phoneNumber")
    @Mapping(source = "stockCount", target = "forSaleCount")
    @Mapping(source = "unitPrice", target = "unitPrice")
    @Mapping(source = "saleStockStatus", target = "status")
    SaleStockResponseDTO toResponseDTO(SaleStock saleStock);
}
