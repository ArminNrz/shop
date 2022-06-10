package com.shop.service.entity;

import com.shop.dto.saleStock.SaleStockCreateDTO;
import com.shop.dto.saleStock.SaleStockResponseDTO;
import com.shop.dto.saleStock.SaleStockUpdateDTO;
import com.shop.entity.SaleStock;
import com.shop.entity.enumartion.SaleStockStatus;
import com.shop.mapper.SaleStockMapper;
import com.shop.repository.SaleStockRepository;
import com.shop.specification.SaleStockSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaleStockService {

    private final SaleStockRepository repository;
    private final SaleStockMapper mapper;

    public void create(SaleStockCreateDTO createDTO) {
        SaleStock entity = mapper.toEntity(createDTO);
        entity.setSaleStockStatus(SaleStockStatus.OPEN);
        repository.save(entity);
        log.debug("Saved sale stock: {}", entity);
    }

    public void update(SaleStockUpdateDTO updateDTO, SaleStock foundEntity) {
        SaleStock newEntity = mapper.toEntity(updateDTO, foundEntity);
        repository.save(newEntity);
        log.debug("Updated sale stock: {}", newEntity);
    }

    public SaleStock findById(Long id) {
        log.debug("Try to find sale stock by id: {}", id);
        Optional<SaleStock> saleStockOptional = repository.findById(id);
        if (saleStockOptional.isEmpty()) {
            log.error("No such sale stock exist for id: {}", id);
            return null;
        }

        log.debug("Found sale stock for id: {}, is: {}", id, saleStockOptional.get());
        return saleStockOptional.get();
    }

    public SaleStock findByIdAndUserId(Long id, Long userId) {
        log.debug("Try to find sale stock by id: {}, userId: {}", id, userId);
        Optional<SaleStock> entityOptional = repository.findByIdAndUserId(id, userId);
        if (entityOptional.isEmpty()) {
            log.error("No such sale stock exist for id: {}, userId: {}", id, userId);
            return null;
        }

        log.debug("Found sale stock for id: {} and userId: {}, is: {}", id, userId, entityOptional.get());
        return entityOptional.get();
    }

    public Page<SaleStockResponseDTO> findGeneral(SaleStockSpecification specification, Pageable pageable, String userPhoneNumber) {
        log.debug("Try to general find sale stock with specification: {}, pageable: {}", specification, pageable);

        List<SaleStockResponseDTO> resultList = repository.findAll(specification).stream()
                .map(mapper::toResponseDTO)
                .filter(saleStockResponseDTO -> !saleStockResponseDTO.getPhoneNumber().equals(userPhoneNumber))
                .limit((long) pageable.getPageNumber() * pageable.getPageSize())
                .collect(Collectors.toList());

        if (pageable.getPageNumber() > 1)
            resultList = resultList.stream().skip((long) (pageable.getPageNumber() - 1) * pageable.getPageSize()).collect(Collectors.toList());

        log.debug("Found saleStocks: {}", resultList);
        return new PageImpl<>(resultList);
    }

    public void updateStatus(SaleStock saleStock, SaleStockStatus newStatus) {
        log.debug("Try to update status of sale stock: {}, to status: {}", saleStock, newStatus);
        saleStock.setSaleStockStatus(newStatus);
        repository.save(saleStock);
        log.debug("Updated sale stock: {}", saleStock);
    }

    public Page<SaleStockResponseDTO> findUserSaleStock(SaleStockSpecification specification, Pageable pageable, String userPhoneNumber) {

        log.debug("Try to get user sale stock, with specification: {}, pageable: {}, for user: {}", specification, pageable, userPhoneNumber);

        List<SaleStockResponseDTO> resultList = repository.findAll(specification).stream()
                .map(mapper::toResponseDTO)
                .filter(saleStockResponseDTO -> saleStockResponseDTO.getPhoneNumber().equals(userPhoneNumber))
                .limit((long) pageable.getPageNumber() * pageable.getPageSize())
                .collect(Collectors.toList());

        if (pageable.getPageNumber() > 1)
            resultList = resultList.stream().skip((long) (pageable.getPageNumber() - 1) * pageable.getPageSize()).collect(Collectors.toList());

        log.debug("Found saleStocks: {}", resultList);
        return new PageImpl<>(resultList);
    }
}
