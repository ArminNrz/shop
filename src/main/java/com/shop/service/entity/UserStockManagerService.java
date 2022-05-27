package com.shop.service.entity;

import com.shop.common.Constant;
import com.shop.dto.stockManager.StockManagerCreateDTO;
import com.shop.dto.stockManager.StockManagerResponseDTO;
import com.shop.entity.AppUserStockManagerLog;
import com.shop.entity.AppUserStocksManager;
import com.shop.mapper.UserStockManagerMapper;
import com.shop.repository.AppUserStockManagerLogRepository;
import com.shop.repository.AppUserStockManagerRepository;
import com.shop.specification.AppUserStockManagerSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserStockManagerService {

    private final AppUserStockManagerRepository repository;
    private final AppUserStockManagerLogRepository logRepository;
    private final UserStockManagerMapper mapper;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    Lock writeLock = lock.writeLock();
    Lock readLock = lock.readLock();

    /////////////////////////////////////////////////
    // CREATE
    ////////////////////////////////////////////////

    @Transactional
    public void create(StockManagerCreateDTO createDTO) {
        log.debug("Request to create user stock manager with dto: {}", createDTO);
        checkUserStockManagerNotExist(createDTO.getUserId());
        AppUserStocksManager entity = mapper.toEntity(createDTO);
        repository.save(entity);
        saveLog(entity);
        log.debug("Created User stock manager: {}", entity);
    }

    @Transactional
    public void createBatch(List<AppUserStocksManager> entities) {
        log.debug("Request to create user stock manager in batch mode, entities: {}", entities);
        repository.saveAllAndFlush(entities);
        log.debug("Saved user stock managers successfully");
    }

    /////////////////////////////////////////////////
    // SAVE-LOG
    ////////////////////////////////////////////////

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(AppUserStocksManager entity) {
        AppUserStockManagerLog logEntities = mapper.toLog(entity);
        log.debug("Try to save log for log logEntities: {}", logEntities);
        logRepository.save(logEntities);
        log.debug("Saved logs for user stock managers");
    }

    /////////////////////////////////////////////////
    // READ
    ////////////////////////////////////////////////

    public AppUserStocksManager findByUserId(Long userId) {
        log.debug("Try to find user stock manager with userId: {}", userId);
        try {
            readLock.lock();
            return findByUserIdInternal(userId);
        } finally {
            readLock.unlock();
        }
    }

    public Page<StockManagerResponseDTO> findAll(AppUserStockManagerSpecification specification, Pageable pageable) {
        log.debug("Request to find all user stock managers");
        try {
            readLock.lock();
            return findAllInternal(specification, pageable);
        } finally {
            readLock.unlock();
        }
    }

    public Page<StockManagerResponseDTO> findOne(Long userId) {
        AppUserStocksManager entity = this.findByUserId(userId);
        StockManagerResponseDTO responseDTO = mapper.toResponseDTO(entity);
        return new PageImpl<>(List.of(responseDTO));
    }

    private Page<StockManagerResponseDTO> findAllInternal(AppUserStockManagerSpecification specification, Pageable pageable) {
        Page<AppUserStocksManager> foundEntities = repository.findAll(specification, pageable);
        if (foundEntities.isEmpty()) {
            log.warn("No user stock manager exist");
            return Page.empty();
        }

        return foundEntities.map(mapper::toResponseDTO);
    }

    private AppUserStocksManager findByUserIdInternal(Long userId) {
        Optional<AppUserStocksManager> optionalEntity = repository.findByUserId(userId);
        if (optionalEntity.isEmpty()) {
            log.warn("No such user stock manager exist with userId: {}", userId);
            return null;
        }

        AppUserStocksManager entity = optionalEntity.get();
        log.debug("Found user stock manager: {}", entity);
        return entity;
    }

    /////////////////////////////////////////////////
    // SALE-STOCK
    ////////////////////////////////////////////////

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saleStock(Long userId, Long stockCount) {
        log.debug("Request to sale stock for user id: {}, stockCount: {}", userId, stockCount);

        AppUserStocksManager userStocksManager = this.findByUserId(userId);
        saleStockValidations(userId, stockCount, userStocksManager);

        try {
            writeLock.lock();
            sale(stockCount, userStocksManager);
        } finally {
            writeLock.unlock();
        }

        saveLog(userStocksManager);
    }

    private void sale(Long stockCount, AppUserStocksManager userStocksManager) {
        long current = userStocksManager.getCurrent();
        long forSale = userStocksManager.getForSale();

        forSale += stockCount;
        current -= stockCount;

        userStocksManager.setCurrent(current);
        userStocksManager.setForSale(forSale);

        repository.save(userStocksManager);
        log.info("Updated user stock manager for user id: {}, stock manager: {}", userStocksManager.getUser().getId(), userStocksManager);
    }

    private void saleStockValidations(Long userId, Long stockCount, AppUserStocksManager userStocksManager) {
        validation(userId, userStocksManager == null, "Sale stock for user with id: {}, not exist", Status.NOT_FOUND, Constant.SALE_STOCK_NOT_FOUND);
        validation(userId, userStocksManager.getCurrent() < stockCount, "Stock count is more than user current stocks, for user id: {}", Status.BAD_REQUEST, Constant.SALE_STOCK_STOCK_COUNT_MORE_THAN_USER_CURRENT_STOCK);
    }

    /////////////////////////////////////////////////
    // UPDATE
    ////////////////////////////////////////////////

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateSaleStock(Long userId, Long formerCount, Long newCount) {
        log.debug("Request to update user id: {}, formerSaleCount: {}, newSaleCount: {}", userId, formerCount, newCount);

        AppUserStocksManager foundEntity = this.findByUserId(userId);
        validation(userId, foundEntity.getCurrent() < formerCount, "Stock count is more than user current stocks, for user id: {}", Status.BAD_REQUEST, Constant.SALE_STOCK_STOCK_COUNT_MORE_THAN_USER_CURRENT_STOCK);

        try {
            writeLock.lock();
            updateSale(formerCount, newCount, foundEntity);
        } finally {
            writeLock.unlock();
        }
        
        saveLog(foundEntity);
    }

    private void updateSale(Long formerCount, Long newCount, AppUserStocksManager foundEntity) {
        long current = foundEntity.getCurrent();
        long forSale = foundEntity.getForSale();

        // revert last update operation
        forSale -= formerCount;
        current += formerCount;

        // update new operation
        forSale += newCount;
        current -= newCount;

        foundEntity.setCurrent(current);
        foundEntity.setForSale(forSale);
        repository.save(foundEntity);
        log.info("Updated user stock manager for user id: {}, stock manager: {}", foundEntity.getUser().getId(), foundEntity);
    }

    /////////////////////////////////////////////////
    // COMMON
    ////////////////////////////////////////////////

    public void checkUserStockManagerNotExist(Long userId) {
        AppUserStocksManager foundEntity = this.findByUserId(userId);
        validation(userId, foundEntity != null, "Can not re-create user stock manager for userId: {}", Status.BAD_REQUEST, Constant.STOCK_MANAGER_USER_EXIST_BEFORE);
    }

    private void validation(Long userId, boolean hasError, String logMessage, Status status, String message) {
        if (hasError) {
            log.error(logMessage, userId);
            throw Problem.valueOf(status, message);
        }
    }
}
