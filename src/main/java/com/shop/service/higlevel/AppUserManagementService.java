package com.shop.service.higlevel;

import com.shop.dto.auth.UserCreateDTO;
import com.shop.dto.auth.UserDTO;
import com.shop.dto.stockManager.StockManagerUpdateDTO;
import com.shop.service.entity.UserService;
import com.shop.service.entity.userStockManager.UserStockManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppUserManagementService {

    private final UserService userService;
    private final UserStockManagerService userStockManagerService;

    @Transactional
    public UserDTO create(UserCreateDTO createDTO) {
        UserDTO userDTO = userService.create(createDTO);
        StockManagerUpdateDTO stockManagerCreateDTO = new StockManagerUpdateDTO();
        stockManagerCreateDTO.setTotal(0L);
        stockManagerCreateDTO.setForSale(0L);
        stockManagerCreateDTO.setUserId(userDTO.getId());
        userStockManagerService.reset(stockManagerCreateDTO, userDTO.getId());
        return userDTO;
    }
}
