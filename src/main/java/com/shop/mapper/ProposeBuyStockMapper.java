package com.shop.mapper;

import com.shop.dto.proposeStock.ProposeBuyStockCreateDTO;
import com.shop.dto.proposeStock.ProposeBuyStockDetailsDTO;
import com.shop.entity.AppUser;
import com.shop.entity.ProposeBuyStock;
import com.shop.entity.SaleStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProposeBuyStockMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "saleStock", source = "saleStock")
    @Mapping(target = "status", ignore = true)
    ProposeBuyStock toEntity(ProposeBuyStockCreateDTO buyStockCreateDTO, SaleStock saleStock, AppUser user);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.phoneNumber", target = "phoneNumber")
    ProposeBuyStockDetailsDTO toDetailsDTO(ProposeBuyStock entity);
}
