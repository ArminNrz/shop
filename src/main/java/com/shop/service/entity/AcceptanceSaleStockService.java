package com.shop.service.entity;

import com.shop.dto.acceptanceSaleStock.AcceptanceSaleStockCreateDTO;
import com.shop.dto.acceptanceSaleStock.AcceptanceSaleStockResponseDTO;
import com.shop.entity.AcceptanceSaleStock;
import com.shop.entity.enumartion.AcceptanceSaleStockStatus;
import com.shop.mapper.AcceptanceSaleStockMapper;
import com.shop.repository.AcceptanceSaleStockRepository;
import com.shop.specification.AcceptanceSaleStockSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AcceptanceSaleStockService {

    private final AcceptanceSaleStockRepository repository;
    private final AcceptanceSaleStockMapper mapper;

    public void create(AcceptanceSaleStockCreateDTO createDTO) {
        log.debug("Request to create acceptance sale stock, with: {}", createDTO);
        AcceptanceSaleStock entity = mapper.toEntity(createDTO);
        entity.setStatus(AcceptanceSaleStockStatus.PENDING);
        repository.save(entity);
        log.debug("Saved acceptance sale stock: {}", entity);
    }

    public Page<AcceptanceSaleStockResponseDTO> get(AcceptanceSaleStockSpecification specification, Pageable pageable) {
        return repository.findAll(specification, pageable)
                .map(mapper::toResponseDTO);
    }

    public AcceptanceSaleStock getById(Long id) {
        log.debug("Try to find acceptance sale stock with id: {}", id);
        Optional<AcceptanceSaleStock> optionalEntity = repository.findById(id);
        if (optionalEntity.isEmpty()) {
            log.error("No such acceptance sale stock exist with id: {}", id);
            return null;
        }

        log.debug("Found acceptance sale stock: {}", optionalEntity.get());
        return optionalEntity.get();
    }

    public void update(AcceptanceSaleStock acceptanceSaleStock) {
        log.debug("Try to update acceptance sale stock: {}", acceptanceSaleStock);
        repository.save(acceptanceSaleStock);
    }
}
