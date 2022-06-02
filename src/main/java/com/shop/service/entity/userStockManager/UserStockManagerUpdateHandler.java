package com.shop.service.entity.userStockManager;

import com.shop.dto.stockManager.StockManagerUpdateDTO;
import com.shop.entity.AppUserStocksManager;
import com.shop.repository.AppUserStockManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserStockManagerUpdateHandler {

    private final AppUserStockManagerRepository repository;

    public void sale(Long stockCount, AppUserStocksManager userStocksManager) {
        long current = userStocksManager.getCurrent();
        long forSale = userStocksManager.getForSale();

        forSale += stockCount;
        current -= stockCount;

        userStocksManager.setCurrent(current);
        userStocksManager.setForSale(forSale);

        this.update(userStocksManager);
        log.info("Updated user stock manager for user id: {}, stock manager: {}", userStocksManager.getUser().getId(), userStocksManager);
    }

    public void updateSale(Long formerCount, Long newCount, AppUserStocksManager foundEntity) {
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
        this.update(foundEntity);
        log.info("Updated user stock manager for user id: {}, stock manager: {}", foundEntity.getUser().getId(), foundEntity);
    }

    public void increaseWillBuy(AppUserStocksManager entity, Long proposeCount) {
        long willBuy = entity.getWillBuy();
        willBuy += proposeCount;
        entity.setWillBuy(willBuy);
        this.update(entity);
        log.info("increase will buy user stock manager for user id: {}, stock manager: {}", entity.getUser().getId(), entity);
    }

    public void cancelProposeToBuyInternal(AppUserStocksManager entity, Long proposeCount) {
        long willBuy = entity.getWillBuy();
        willBuy = willBuy - proposeCount;
        entity.setWillBuy(willBuy);
        this.update(entity);
    }

    private void update(AppUserStocksManager entity) {
        repository.save(entity);
    }

    public void resetInternal(AppUserStocksManager entity, StockManagerUpdateDTO updateDTO) {
        entity.setCurrent(updateDTO.getTotal());
        entity.setTotal(updateDTO.getTotal());
        entity.setForSale(updateDTO.getForSale());
        entity.setWillBuy(0L);
        this.update(entity);
    }

    public void sell(AppUserStocksManager entity, Long proposeBuyStockCount) {
        Long total = entity.getTotal();
        Long forSale = entity.getForSale();
        long current;

        total = total - proposeBuyStockCount;
        forSale = forSale - proposeBuyStockCount;
        current = total - forSale;

        entity.setTotal(total);
        entity.setCurrent(current);
        entity.setForSale(forSale);

        this.update(entity);
    }

    public void buy(AppUserStocksManager entity, Long proposeCount) {
        long total = entity.getTotal();
        long willBuy = entity.getWillBuy();
        long current = entity.getCurrent();

        total = total + proposeCount;
        willBuy = willBuy - proposeCount;
        current = current + proposeCount;

        entity.setTotal(total);
        entity.setWillBuy(willBuy);
        entity.setCurrent(current);

        this.update(entity);
    }
}
