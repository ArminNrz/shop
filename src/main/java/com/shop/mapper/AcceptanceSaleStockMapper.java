package com.shop.mapper;

import com.shop.dto.acceptanceSaleStock.create.AcceptanceSaleStockCreateDTO;
import com.shop.dto.acceptanceSaleStock.create.AcceptanceSaleStockResponseDTO;
import com.shop.dto.acceptanceSaleStock.update.AcceptanceSaleStockUpdateDTO;
import com.shop.entity.AcceptanceSaleStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AcceptanceSaleStockMapper {

    @Mapping(source = "createDTO.sellerId", target = "seller.id")
    @Mapping(source = "createDTO.buyerId", target = "buyer.id")
    @Mapping(source = "createDTO.proposeBuyStockId", target = "proposeBuyStock.id")
    AcceptanceSaleStock toEntity(AcceptanceSaleStockCreateDTO createDTO);

    @Mapping(source = "seller.firstName", target = "sellerFirstName")
    @Mapping(source = "seller.lastName", target = "sellerLastName")
    @Mapping(source = "seller.phoneNumber", target = "sellerPhoneNumber")
    @Mapping(source = "buyer.firstName", target = "buyerFirstName")
    @Mapping(source = "buyer.lastName", target = "buyerLastName")
    @Mapping(source = "buyer.phoneNumber", target = "buyerPhoneNumber")
    @Mapping(source = "proposeBuyStock.proposeCount", target = "stockCount")
    @Mapping(source = "proposeBuyStock.proposeUnitCost", target = "unitPrice")
    AcceptanceSaleStockResponseDTO toResponseDTO(AcceptanceSaleStock entity);

    @Mapping(source = "updateDTO.sellTime", target = "sellTime")
    @Mapping(source = "updateDTO.sellLocation", target = "sellLocation")
    @Mapping(source = "entity.id", target = "id")
    AcceptanceSaleStock fromUpdateDTO(AcceptanceSaleStock entity, AcceptanceSaleStockUpdateDTO updateDTO);
}
