package com.shop.service.higlevel.proposeBuy;

import com.shop.dto.acceptanceSaleStock.AcceptanceSaleStockCreateDTO;
import com.shop.dto.acceptanceSaleStock.AcceptanceSaleStockResponseDTO;
import com.shop.dto.proposeStock.ProposeBuyStockCreateDTO;
import com.shop.dto.proposeStock.ProposeBuyStockDetailsDTO;
import com.shop.entity.AppUser;
import com.shop.service.entity.AcceptanceSaleStockService;
import com.shop.service.entity.ProposeBuyStockService;
import com.shop.service.lowlevel.SecurityService;
import com.shop.specification.AcceptanceSaleStockSpecification;
import com.shop.specification.ProposeBuySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProposeManagerService {

    private final SecurityService securityService;
    private final BuyProposeStockHandler buyProposeStockHandler;
    private final DeleteProposeBuyStockHandler deleteProposeBuyStockHandler;
    private final AcceptBuyProposeStockHandler acceptBuyProposeStockHandler;
    private final AcceptanceSaleStockService acceptanceSaleStockService;
    private final TransferAcceptanceStockHandler transferAcceptanceStockHandler;
    private final ProposeBuyStockService proposeBuyStockService;

    public void buyPropose(ProposeBuyStockCreateDTO buyStockCreateDTO, String token) {
        AppUser user = securityService.getUserWithToken(token);
        buyStockCreateDTO.setUserId(user.getId());
        buyProposeStockHandler.buy(buyStockCreateDTO, user);
    }

    public void deletePropose(Long proposeBuyStockId, String token) {
        AppUser user = securityService.getUserWithToken(token);
        List<String> roles = securityService.getTokenRoles(token);
        boolean isAdmin = roles.contains("ROLE_ADMIN");
        deleteProposeBuyStockHandler.delete(proposeBuyStockId, user, isAdmin);
    }

    public Page<ProposeBuyStockDetailsDTO> getUserPropose(String token, ProposeBuySpecification specification, int pageCount, int pageSize) {
        AppUser user = securityService.getUserWithToken(token);
        return proposeBuyStockService.findByUserId(user.getId(), specification, pageCount, pageSize);
    }

    public void acceptBuyPropose(AcceptanceSaleStockCreateDTO createDTO, Long proposeBuyStockId, String token) {
        AppUser user = securityService.getUserWithToken(token);
        acceptBuyProposeStockHandler.accept(proposeBuyStockId, user, createDTO);
    }

    public Page<AcceptanceSaleStockResponseDTO> getAcceptance(AcceptanceSaleStockSpecification specification, Pageable pageable, String token) {
        List<String> roles = securityService.getTokenRoles(token);
        boolean isAdmin = roles.contains("ROLE_ADMIN");

        if (isAdmin) {
            return acceptanceSaleStockService.get(specification, pageable);
        }
        else {
            AppUser user = securityService.getUserWithToken(token);
            List<AcceptanceSaleStockResponseDTO> resultList = acceptanceSaleStockService.get(specification, pageable).stream()
                    .filter(responseDTO ->
                            responseDTO.getSellerPhoneNumber().equals(user.getPhoneNumber()) ||
                            responseDTO.getBuyerPhoneNumber().equals(user.getPhoneNumber())
                    ).collect(Collectors.toList());
            return new PageImpl<>(resultList);
        }
    }

    public void transferAcceptanceStock(Long acceptanceId, String token) {
        AppUser modifier = securityService.getUserWithToken(token);
        transferAcceptanceStockHandler.transfer(acceptanceId, modifier);
    }
}
