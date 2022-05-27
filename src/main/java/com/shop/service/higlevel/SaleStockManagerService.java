package com.shop.service.higlevel;

import com.shop.common.Constant;
import com.shop.dto.saleStock.SaleStockCreateDTO;
import com.shop.dto.saleStock.SaleStockUpdateDTO;
import com.shop.entity.AppUser;
import com.shop.entity.SaleStock;
import com.shop.entity.enumartion.SaleStockStatus;
import com.shop.service.entity.SaleStockService;
import com.shop.service.entity.UserStockManagerService;
import com.shop.service.lowlevel.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaleStockManagerService {

    private final SaleStockService saleStockService;
    private final SecurityService securityService;
    private final UserStockManagerService stockManagerService;

    @Transactional
    public void create(SaleStockCreateDTO createDTO, String token) {
        AppUser user = securityService.getUserWithToken(token);
        createDTO.setUserId(user.getId());
        stockManagerService.saleStock(user.getId(), createDTO.getStockCount());
        saleStockService.create(createDTO);
    }

    public void update(SaleStockUpdateDTO updateDTO, String token) {
        AppUser user = securityService.getUserWithToken(token);
        SaleStock foundEntity = saleStockService.findById(updateDTO.getId());

        if (foundEntity == null) {
            throw Problem.valueOf(Status.NOT_FOUND, Constant.SALE_STOCK_NOT_FOUND);
        }

        if (foundEntity.getSaleStockStatus() != SaleStockStatus.OPEN) {
            log.error("Can not update un-open sale stock, sale stock id: {}", foundEntity.getId());
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.SALE_STOCK_CAN_NOT_UPDATE_UN_OPEN);
        }

        stockManagerService.updateSaleStock(user.getId(), foundEntity.getStockCount(), updateDTO.getStockCount());
        saleStockService.update(updateDTO, foundEntity);
    }
}
