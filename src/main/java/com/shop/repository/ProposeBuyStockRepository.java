package com.shop.repository;

import com.shop.entity.ProposeBuyStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposeBuyStockRepository extends JpaRepository<ProposeBuyStock, Long>, JpaSpecificationExecutor<ProposeBuyStock> {

    Page<ProposeBuyStock> findBySaleStockId(Long saleStockId, Pageable pageable);
}
