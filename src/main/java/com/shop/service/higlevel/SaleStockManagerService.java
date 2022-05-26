package com.shop.service.higlevel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shop.common.Constant;
import com.shop.dto.saleStock.SaleStockCreateDTO;
import com.shop.dto.saleStock.SaleStockUpdateDTO;
import com.shop.entity.AppUser;
import com.shop.entity.AppUserStocksManager;
import com.shop.entity.SaleStock;
import com.shop.entity.enumartion.SaleStockOperation;
import com.shop.entity.enumartion.SaleStockStatus;
import com.shop.jms.ProducerService;
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
    private final ProducerService producerService;

    public void create(SaleStockCreateDTO createDTO, String token) throws JsonProcessingException {
        AppUser user = securityService.getUserWithToken(token);
        createDTO.setUserId(user.getId());
        producerService.sendToStockManagerUpdateQueue(createDTO, SaleStockOperation.CREATE);
    }

    public void update(SaleStockUpdateDTO updateDTO, String token) throws JsonProcessingException {
        AppUser user = securityService.getUserWithToken(token);
        SaleStock foundEntity = saleStockService.findById(updateDTO.getId());
        if (foundEntity == null) {
            throw Problem.valueOf(Status.NOT_FOUND, Constant.SALE_STOCK_NOT_FOUND);
        }

        if (foundEntity.getSaleStockStatus() != SaleStockStatus.OPEN) {
            log.error("Can not update un-open sale stock, sale stock id: {}", foundEntity.getId());
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.SALE_STOCK_CAN_NOT_UPDATE_UN_OPEN);
        }

        updateDTO.setUserId(user.getId());

        producerService.sendToStockManagerUpdateQueue(updateDTO, SaleStockOperation.UPDATE);
    }

    @Transactional
    public void create(SaleStockCreateDTO createDTO) {

        long userId = createDTO.getUserId();

        AppUserStocksManager userStocksManager = stockManagerService.findByUserId(userId);
        if (userStocksManager == null) {
            return;
        }

        if (userStocksManager.getCurrent() < createDTO.getStockCount()) {
            log.error("Stock count is more than user current stocks, for user id: {}", userId);
            return;
        }

        stockManagerService.saleStock(userStocksManager, createDTO.getStockCount());
        saleStockService.create(createDTO);
    }

    @Transactional
    public void update(SaleStockUpdateDTO updateDTO) {

        long userId = updateDTO.getUserId();

        SaleStock foundEntity = saleStockService.findById(updateDTO.getId());
        AppUserStocksManager userStocksManager = stockManagerService.findByUserId(updateDTO.getUserId());

        if (userStocksManager == null) {
            return;
        }

        if (userStocksManager.getCurrent() < updateDTO.getStockCount()) {
            log.error("Stock count is more than user current stocks, for user id: {}", userId);
            return;
        }

        stockManagerService.updateSaleStock(userStocksManager, foundEntity.getStockCount(), updateDTO.getStockCount());
        saleStockService.update(updateDTO, foundEntity);
    }
}
