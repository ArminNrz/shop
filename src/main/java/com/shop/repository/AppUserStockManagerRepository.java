package com.shop.repository;

import com.shop.entity.AppUserStocksManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserStockManagerRepository extends JpaRepository<AppUserStocksManager, Long>, JpaSpecificationExecutor<AppUserStocksManager> {

    Optional<AppUserStocksManager> findByUserId(Long userId);
}
