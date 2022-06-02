package com.shop.service.higlevel.proposeBuy;

import com.shop.common.Constant;
import com.shop.dto.acceptanceSaleStock.AcceptanceSaleStockCreateDTO;
import com.shop.dto.proposeStock.ProposeBuyStockCreateDTO;
import com.shop.entity.AppUser;
import com.shop.entity.ProposeBuyStock;
import com.shop.entity.SaleStock;
import com.shop.entity.enumartion.ProposeBuyStockStatus;
import com.shop.entity.enumartion.SaleStockStatus;
import com.shop.service.entity.AcceptanceSaleStockService;
import com.shop.service.entity.ProposeBuyStockService;
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
public class AcceptBuyProposeStockHandler {

    private final ProposeBuyStockService proposeBuyStockService;
    private final AcceptanceSaleStockService acceptanceSaleStockService;
    private final UserStockManagerService userStockManagerService;

    public void accept(Long proposeBuyStockId, AppUser user, AcceptanceSaleStockCreateDTO acceptanceCreateDTO) {
        ProposeBuyStock proposeBuyStock = proposeBuyStockService.findById(proposeBuyStockId);
        if (proposeBuyStock == null) {
            throw Problem.valueOf(Status.NOT_FOUND, Constant.PROPOSE_BUY_STOCK_NOT_FOUND);
        }

        if (!proposeBuyStock.getStatus().equals(ProposeBuyStockStatus.OPEN)) {
            log.error("Propose id: {} is not in open status", proposeBuyStockId);
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.PROPOSE_BUY_STOCK_NOT_IN_OPEN_STATUS);
        }

        SaleStock saleStock = proposeBuyStock.getSaleStock();
        if (!saleStock.getUser().equals(user)) {
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.SALE_STOCK_NOT_BELONG_TO_USER);
        }

        updateStatus(proposeBuyStock, saleStock);
        createAcceptance(proposeBuyStockId, acceptanceCreateDTO, proposeBuyStock, saleStock);
        updateBuyerStockManager(proposeBuyStock, saleStock);
        //todo: send sms to both of seller and buyer
    }

    private void updateBuyerStockManager(ProposeBuyStock proposeBuyStock, SaleStock saleStock) {
        ProposeBuyStockCreateDTO proposeBuyStockCreateDTO = new ProposeBuyStockCreateDTO();
        proposeBuyStockCreateDTO.setSaleStockId(saleStock.getId());
        proposeBuyStockCreateDTO.setProposeCount(proposeBuyStock.getProposeCount());
        proposeBuyStockCreateDTO.setProposeUnitCost(saleStock.getUnitPrice());
        proposeBuyStockCreateDTO.setUserId(proposeBuyStock.getUser().getId());
        userStockManagerService.proposeToBuy(proposeBuyStockCreateDTO);
    }

    private void createAcceptance(Long proposeBuyStockId, AcceptanceSaleStockCreateDTO acceptanceCreateDTO, ProposeBuyStock proposeBuyStock, SaleStock saleStock) {
        acceptanceCreateDTO.setSellerId(saleStock.getUserId());
        acceptanceCreateDTO.setBuyerId(proposeBuyStock.getUser().getId());
        acceptanceCreateDTO.setProposeBuyStockId(proposeBuyStockId);
        acceptanceSaleStockService.create(acceptanceCreateDTO);
    }

    private void updateStatus(ProposeBuyStock proposeBuyStock, SaleStock saleStock) {
        proposeBuyStock.setStatus(ProposeBuyStockStatus.ACCEPTED);
        long remainStock = saleStock.getStockCount() - proposeBuyStock.getProposeCount();

        if (remainStock == 0) {
            saleStock.setSaleStockStatus(SaleStockStatus.CLOSE);
            saleStock.getProposeBuyStocks().stream()
                    .filter(propose -> !propose.getId().equals(proposeBuyStock.getId()))
                    .forEach(propose -> propose.setStatus(ProposeBuyStockStatus.CLOSE));
        }
        else if (remainStock > 0) {
            saleStock.setSaleStockStatus(SaleStockStatus.PENDING);
            saleStock.getProposeBuyStocks().stream()
                    .filter(propose -> !propose.getId().equals(proposeBuyStock.getId()))
                    .forEach(propose -> updateOtherProposeStatus(remainStock, propose));
        }
    }

    private void updateOtherProposeStatus(long remainStock, ProposeBuyStock proposeBuyStock) {
        if (proposeBuyStock.getProposeCount() > remainStock)
            proposeBuyStock.setStatus(ProposeBuyStockStatus.CLOSE);
    }
}
