package com.shop.repository;

import com.shop.entity.SaleStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleStockRepository extends JpaRepository<SaleStock, Long>, JpaSpecificationExecutor<SaleStock> {
}
