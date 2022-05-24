package com.shop.service.entity;

import com.shop.common.Constant;
import com.shop.dto.stockManager.StockManagerCreateDTO;
import com.shop.entity.AppUserStockManagerLog;
import com.shop.entity.AppUserStocksManager;
import com.shop.mapper.UserStockManagerMapper;
import com.shop.repository.AppUserStockManagerLogRepository;
import com.shop.repository.AppUserStockManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserStockManagerService {

    private final AppUserStockManagerRepository repository;
    private final AppUserStockManagerLogRepository logRepository;
    private final UserStockManagerMapper mapper;

    public AppUserStocksManager findByUserId(Long userId) {
        log.debug("Try to find user stock manager with userId: {}", userId);
        Optional<AppUserStocksManager> optionalEntity = repository.findByUserId(userId);
        if (optionalEntity.isEmpty()) {
            log.warn("No such user stock manager exist with userId: {}", userId);
            return null;
        }

        AppUserStocksManager entity = optionalEntity.get();
        log.debug("Found user stock manager: {}", entity);
        return entity;
    }

    @Transactional
    public void create(StockManagerCreateDTO createDTO) {
        log.debug("Request to create user stock manager with dto: {}", createDTO);
        checkUserStockManagerNotExist(createDTO.getUserId());
        AppUserStocksManager entity = mapper.toEntity(createDTO);
        repository.save(entity);
        saveLog(entity);
        log.debug("Created User stock manager: {}", entity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(AppUserStocksManager entity) {
        AppUserStockManagerLog logEntities = mapper.toLog(entity);
        log.debug("Try to save log for log logEntities: {}", logEntities);
        logRepository.save(logEntities);
        log.debug("Saved logs for user stock managers");
    }

    public void checkUserStockManagerNotExist(Long userId) {
        AppUserStocksManager foundEntity = this.findByUserId(userId);
        if (foundEntity != null) {
            log.error("Can not re-create user stock manager for userId: {}", userId);
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.STOCK_MANAGER_USER_EXIST_BEFORE);
        }
    }

    @Transactional
    public void createBatch(List<AppUserStocksManager> entities) {
        log.debug("Request to create user stock manager in batch mode, entities: {}", entities);
        repository.saveAllAndFlush(entities);
        log.debug("Saved user stock managers successfully");
    }
}
