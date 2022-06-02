package com.shop.repository;

import com.shop.entity.AcceptanceSaleStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AcceptanceSaleStockRepository extends JpaRepository<AcceptanceSaleStock, Long>, JpaSpecificationExecutor<AcceptanceSaleStock> {
}
