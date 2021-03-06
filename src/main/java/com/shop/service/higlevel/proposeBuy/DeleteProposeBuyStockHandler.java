package com.shop.service.higlevel.proposeBuy;

import com.shop.common.Constant;
import com.shop.entity.AppUser;
import com.shop.entity.ProposeBuyStock;
import com.shop.entity.SaleStock;
import com.shop.entity.enumartion.ProposeBuyStockStatus;
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
public class DeleteProposeBuyStockHandler {

    private final SaleStockService saleStockService;
    private final ProposeBuyStockService proposeBuyStockService;

    public void delete(Long proposeBuyStockId, AppUser user, boolean isAdmin) {
        ProposeBuyStock proposeBuyStock = proposeBuyStockService.findById(proposeBuyStockId);
        validation(proposeBuyStockId, user, isAdmin, proposeBuyStock);
        updateSaleStockStatus(proposeBuyStock);
        proposeBuyStockService.delete(proposeBuyStock);
    }

    private void updateSaleStockStatus(ProposeBuyStock proposeBuyStock) {
        SaleStock saleStock = proposeBuyStock.getSaleStock();
        if (saleStock.getProposeBuyStocks().size() == 1) {
            saleStock.getProposeBuyStocks().remove(proposeBuyStock);
            saleStockService.updateStatus(saleStock, SaleStockStatus.OPEN);
        }
    }

    private void validation(Long proposeBuyStockId, AppUser user, boolean isAdmin, ProposeBuyStock proposeBuyStock) {
        if (proposeBuyStock == null) {
            throw Problem.valueOf(Status.NOT_FOUND, Constant.PROPOSE_BUY_STOCK_NOT_FOUND);
        }
        if (!proposeBuyStock.getUser().equals(user) && !isAdmin) {
            log.error("This user has not access to delete this propose buy stock with id: {}", proposeBuyStockId);
            throw Problem.valueOf(Status.FORBIDDEN, Constant.FORBIDDEN);
        }
        if (proposeBuyStock.getStatus().equals(ProposeBuyStockStatus.ACCEPTED)) {
            log.error("Can not delete propose: {}, because in status Accepted", proposeBuyStock);
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.PROPOSE_BUY_STOCK_NOT_DELETEABLE);
        }
    }
}
