package com.shop.service.higlevel.proposeBuy;

import com.shop.dto.proposeStock.ProposeBuyStockCreateDTO;
import com.shop.entity.AppUser;
import com.shop.service.lowlevel.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProposeManagerService {

    private final SecurityService securityService;
    private final BuyProposeStockHandler buyProposeStockHandler;
    private final DeleteProposeBuyStockHandler deleteProposeBuyStockHandler;

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
}
