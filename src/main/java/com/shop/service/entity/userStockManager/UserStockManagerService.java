package com.shop.service.entity.userStockManager;

import com.shop.common.Constant;
import com.shop.dto.proposeStock.ProposeBuyStockCreateDTO;
import com.shop.dto.stockManager.StockManagerResponseDTO;
import com.shop.dto.stockManager.StockManagerUpdateDTO;
import com.shop.entity.AppUser;
import com.shop.entity.AppUserStocksManager;
import com.shop.mapper.UserStockManagerMapper;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserStockManagerService {

    private final AppUserStockManagerRepository repository;
    private final UserStockManagerLogHandler logService;
    private final UserStockManagerReadHandler readHandler;
    private final UserStockManagerUpdateHandler updateHandler;
    private final UserStockManagerMapper mapper;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    Lock writeLock = lock.writeLock();
    Lock readLock = lock.readLock();

    /////////////////////////////////////////////////
    // CREATE
    ////////////////////////////////////////////////

    @Transactional
    public void createBatch(List<AppUserStocksManager> entities) {
        log.debug("Request to create user stock manager in batch mode, entities: {}", entities);
        repository.saveAllAndFlush(entities);
        //entities.forEach(logService::saveLog);
        log.debug("Saved user stock managers successfully");
    }

    /////////////////////////////////////////////////
    // READ
    ////////////////////////////////////////////////

    public AppUserStocksManager findByUserId(Long userId) {
        log.debug("Try to find user stock manager with userId: {}", userId);
        try {
            readLock.lock();
            return readHandler.findByUserIdInternal(userId);
        } finally {
            readLock.unlock();
        }
    }

    public Page<StockManagerResponseDTO> findAll(AppUserStockManagerSpecification specification, Pageable pageable) {
        log.debug("Request to find all user stock managers");
        try {
            readLock.lock();
            return readHandler.findAllInternal(specification, pageable);
        } finally {
            readLock.unlock();
        }
    }

    public Page<StockManagerResponseDTO> findOne(Long userId) {
        AppUserStocksManager entity = this.findByUserId(userId);
        StockManagerResponseDTO responseDTO = mapper.toResponseDTO(entity);
        return new PageImpl<>(List.of(responseDTO));
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
            updateHandler.sale(stockCount, userStocksManager);
        } finally {
            writeLock.unlock();
        }

        logService.saveLog(userStocksManager, Constant.STOCK_MANAGER_SALE_DESC, userId);
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
            updateHandler.updateSale(formerCount, newCount, foundEntity);
        } finally {
            writeLock.unlock();
        }
        
        logService.saveLog(foundEntity, Constant.STOCK_MANAGER_UPDATE_SALE_STOCK_DESC, userId);
    }

    public void sellStock(AppUser user, Long proposeBuyStockCount, AppUser modifier) {
        log.debug("Try to sell {} stock for user: {}", proposeBuyStockCount, user.getId());

        AppUserStocksManager foundEntity = this.findByUserId(user.getId());

        try {
            writeLock.lock();
            updateHandler.sell(foundEntity, proposeBuyStockCount);
        } finally {
            writeLock.unlock();
        }

        logService.saveLog(foundEntity, Constant.STOCK_MANAGER_FINALIZE_SALE, modifier.getId());
    }

    public void buyStock(AppUser user, Long proposeCount, AppUser modifier) {
        log.debug("Try to buy {} stock for user: {}", proposeCount, user.getId());

        AppUserStocksManager foundEntity = this.findByUserId(user.getId());

        try {
            writeLock.lock();
            updateHandler.buy(foundEntity, proposeCount);
        } finally {
            writeLock.unlock();
        }

        logService.saveLog(foundEntity, Constant.STOCK_MANAGER_FINALIZE_BUY, modifier.getId());
    }

    /////////////////////////////////////////////////
    // PROPOSE TO BUY
    ////////////////////////////////////////////////

    public void proposeToBuy(ProposeBuyStockCreateDTO buyStockCreateDTO) {
        log.debug("propose to buy stock, with buy stock create DTO: {}", buyStockCreateDTO);

        AppUserStocksManager entity = this.findByUserId(buyStockCreateDTO.getUserId());

        try {
            writeLock.lock();
            updateHandler.increaseWillBuy(entity, buyStockCreateDTO.getProposeCount());
        } finally {
            writeLock.unlock();
        }

        logService.saveLog(entity, Constant.STOCK_MANAGER_BUY_STOCK_DESC, buyStockCreateDTO.getUserId());
    }

    public void cancelProposeToBuy(Long proposeCount, Long userId) {
        log.debug("Try to cancel propose to buy for userId: {}", userId);

        AppUserStocksManager entity = this.findByUserId(userId);

        try {
            writeLock.lock();
            updateHandler.cancelProposeToBuyInternal(entity, proposeCount);
        } finally {
            writeLock.unlock();
        }

        logService.saveLog(entity, Constant.STOCK_MANAGER_CANCEL_BUY_DESC, userId);
    }

    /////////////////////////////////////////////////
    // RESET
    ////////////////////////////////////////////////

    @Transactional
    public void reset(StockManagerUpdateDTO updateDTO, Long modifierId) {
        log.debug("Try to reset stock manager with: {}", updateDTO);
        AppUserStocksManager entity = this.findByUserId(updateDTO.getUserId());
        if (entity == null) {
            entity = mapper.toEntity(updateDTO);
        }

        try {
            writeLock.lock();
            updateHandler.resetInternal(entity, updateDTO);
        } finally {
            writeLock.unlock();
        }

        logService.saveLog(entity, Constant.STOCK_MANAGER_RESET_DESC, modifierId);
    }

    /////////////////////////////////////////////////
    // COMMON
    ////////////////////////////////////////////////

    private void validation(Long userId, boolean hasError, String logMessage, Status status, String message) {
        if (hasError) {
            log.error(logMessage, userId);
            throw Problem.valueOf(status, message);
        }
    }
}
