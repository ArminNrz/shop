package com.shop.service.higlevel.proposeBuy;

import com.shop.common.Constant;
import com.shop.dto.proposeStock.ProposeBuyStockCreateDTO;
import com.shop.entity.AppUser;
import com.shop.entity.SaleStock;
import com.shop.entity.enumartion.SaleStockStatus;
import com.shop.service.entity.ProposeBuyStockService;
import com.shop.service.entity.SaleStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

@Component
@Slf4j
@RequiredArgsConstructor
public class BuyProposeStockHandler {

    private final SaleStockService saleStockService;
    private final ProposeBuyStockService proposeBuyStockService;

    public void buy(ProposeBuyStockCreateDTO buyStockCreateDTO, AppUser user) {
        SaleStock saleStock = saleStockService.findById(buyStockCreateDTO.getSaleStockId());
        validation(buyStockCreateDTO, saleStock);
        saleStock.setSaleStockStatus(SaleStockStatus.PENDING);
        proposeBuyStockService.create(buyStockCreateDTO, saleStock, user);
    }

    private void validation(ProposeBuyStockCreateDTO buyStockCreateDTO, SaleStock saleStock) {
        if (saleStock == null) {
            throw Problem.valueOf(Status.NOT_FOUND, Constant.SALE_STOCK_NOT_FOUND);
        }

        if (saleStock.getSaleStockStatus() != SaleStockStatus.OPEN && saleStock.getSaleStockStatus() != SaleStockStatus.PENDING) {
            log.error("Sale stock with id: {}, not in propose to buy status", buyStockCreateDTO.getSaleStockId());
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.SALE_STOCK_NOT_IN_PROPOSE_TO_BUY_STATUS);
        }

        if (buyStockCreateDTO.getProposeCount() > saleStock.getStockCount()) {
            log.error("Propose count is more than sale stock count, proposeCount: {}, saleStockCount: {}", buyStockCreateDTO.getProposeCount(), saleStock.getStockCount());
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.PROPOSE_BUY_STOCK_PROPOSE_MORE_THAN_COUNT);
        }

        if (saleStock.getUser().getId().equals(buyStockCreateDTO.getUserId())) {
            log.error("Can not propose to buy your sale stock with id: {}", saleStock.getId());
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.PROPOSE_BUY_YOUR_STOCK_NOT_ALLOWED);
        }

        if (buyStockCreateDTO.getProposeUnitCost() == null) {
            buyStockCreateDTO.setProposeUnitCost(saleStock.getUnitPrice());
        }
    }
}
