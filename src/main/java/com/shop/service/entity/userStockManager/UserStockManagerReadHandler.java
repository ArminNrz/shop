package com.shop.service.entity.userStockManager;

import com.shop.dto.stockManager.StockManagerResponseDTO;
import com.shop.entity.AppUserStocksManager;
import com.shop.mapper.UserStockManagerMapper;
import com.shop.repository.AppUserStockManagerRepository;
import com.shop.specification.AppUserStockManagerSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserStockManagerReadHandler {

    private final UserStockManagerMapper mapper;
    private final AppUserStockManagerRepository repository;

    public Page<StockManagerResponseDTO> findAllInternal(AppUserStockManagerSpecification specification, Pageable pageable) {
        Page<AppUserStocksManager> foundEntities = repository.findAll(specification, pageable);
        if (foundEntities.isEmpty()) {
            log.warn("No user stock manager exist");
            return Page.empty();
        }

        return foundEntities.map(mapper::toResponseDTO);
    }

    public AppUserStocksManager findByUserIdInternal(Long userId) {
        Optional<AppUserStocksManager> optionalEntity = repository.findByUserId(userId);
        if (optionalEntity.isEmpty()) {
            log.warn("No such user stock manager exist with userId: {}", userId);
            return null;
        }

        AppUserStocksManager entity = optionalEntity.get();
        log.debug("Found user stock manager: {}", entity);
        return entity;
    }
}
