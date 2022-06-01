package com.shop.service.higlevel.userStockManager;

import com.shop.common.Constant;
import com.shop.dto.stockManager.StockManagerCreateBatchResponseDTO;
import com.shop.dto.stockManager.StockManagerUpdateDTO;
import com.shop.dto.stockManager.StockManagerResponseDTO;
import com.shop.entity.AppUser;
import com.shop.service.entity.UserService;
import com.shop.service.entity.userStockManager.UserStockManagerService;
import com.shop.service.lowlevel.SecurityService;
import com.shop.specification.AppUserStockManagerSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockManagerService {

    private final StockManagerBatchHandler batchHandler;
    private final UserStockManagerService userStockManagerService;
    private final SecurityService securityService;
    private final UserService userService;

    public StockManagerCreateBatchResponseDTO createBatch(MultipartFile excelFile) {
        return batchHandler.createBatch(excelFile);
    }

    public void reset(StockManagerUpdateDTO updateDTO, String token) {
        AppUser user = userService.getById(updateDTO.getUserId());
        if (user == null) {
            throw Problem.valueOf(Status.NOT_FOUND, Constant.APP_USER_NOT_FOUND);
        }

        AppUser modifierUser = securityService.getUserWithToken(token);
        userStockManagerService.reset(updateDTO, modifierUser.getId());
    }

    public Page<StockManagerResponseDTO> getAll(AppUserStockManagerSpecification specification, Pageable pageable, String token) {

        AppUser user = securityService.getUserWithToken(token);
        List<String> roles = securityService.getTokenRoles(token);

        Page<StockManagerResponseDTO> result;

        if (roles.contains("ROLE_ADMIN")) {
            result = userStockManagerService.findAll(specification, pageable);
        }
        else {
            result = userStockManagerService.findOne(user.getId());
        }

        log.debug("Result: {}", result);
        return result;
    }
}
