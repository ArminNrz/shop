package com.shop.service.higlevel.proposeBuy;

import com.shop.common.Constant;
import com.shop.entity.AcceptanceSaleStock;
import com.shop.entity.AppUser;
import com.shop.entity.ProposeBuyStock;
import com.shop.entity.SaleStock;
import com.shop.entity.enumartion.AcceptanceSaleStockStatus;
import com.shop.entity.enumartion.ProposeBuyStockStatus;
import com.shop.entity.enumartion.SaleStockStatus;
import com.shop.service.entity.AcceptanceSaleStockService;
import com.shop.service.entity.userStockManager.UserStockManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TransferAcceptanceStockHandler {

    private final AcceptanceSaleStockService acceptanceSaleStockService;
    private final UserStockManagerService userStockManagerService;

    public void transfer(Long acceptanceId, AppUser modifier) {
        AcceptanceSaleStock acceptanceSaleStock = acceptanceSaleStockService.getById(acceptanceId);
        acceptanceSaleStockValidation(acceptanceId, acceptanceSaleStock);

        ProposeBuyStock proposeBuyStock = acceptanceSaleStock.getProposeBuyStock();
        SaleStock saleStock = proposeBuyStock.getSaleStock();

        updateStatus(acceptanceSaleStock, proposeBuyStock, saleStock);
        transferStock(modifier, acceptanceSaleStock);
    }

    private void transferStock(AppUser modifier, AcceptanceSaleStock acceptanceSaleStock) {
        AppUser seller = acceptanceSaleStock.getSeller();
        userStockManagerService.sellStock(seller, acceptanceSaleStock.getProposeBuyStock().getProposeCount(), modifier);

        AppUser buyer = acceptanceSaleStock.getBuyer();
        userStockManagerService.buyStock(buyer, acceptanceSaleStock.getProposeBuyStock().getProposeCount(), modifier);
    }

    private void updateStatus(AcceptanceSaleStock acceptanceSaleStock, ProposeBuyStock proposeBuyStock, SaleStock saleStock) {
        acceptanceSaleStock.setStatus(AcceptanceSaleStockStatus.FINISHED);
        proposeBuyStock.setStatus(ProposeBuyStockStatus.FINISHED);
        saleStock.setSaleStockStatus(SaleStockStatus.FINISHED);
        acceptanceSaleStockService.update(acceptanceSaleStock);
    }

    private void acceptanceSaleStockValidation(Long acceptanceId, AcceptanceSaleStock acceptanceSaleStock) {
        if (acceptanceSaleStock == null) {
            throw Problem.valueOf(Status.NOT_FOUND, Constant.ACCEPTANCE_SALE_STOCK_NOT_EXIST);
        }

        if (!acceptanceSaleStock.getStatus().equals(AcceptanceSaleStockStatus.PENDING)) {
            log.error("Acceptance sale stock with id: {}, is not in pending status", acceptanceId);
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.ACCEPTANCE_SALE_STOCK_NOT_IN_PROPER_STATUS);
        }
    }
}
