package com.shop.repository;

import com.shop.entity.AppUserStockManagerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserStockManagerLogRepository extends JpaRepository<AppUserStockManagerLog, Long> {
}
