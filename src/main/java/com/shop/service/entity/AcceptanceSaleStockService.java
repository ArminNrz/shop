package com.shop.service.entity;

import com.shop.common.Constant;
import com.shop.dto.acceptanceSaleStock.create.AcceptanceSaleStockCreateDTO;
import com.shop.dto.acceptanceSaleStock.create.AcceptanceSaleStockResponseDTO;
import com.shop.dto.acceptanceSaleStock.update.AcceptanceSaleStockUpdateDTO;
import com.shop.entity.AcceptanceSaleStock;
import com.shop.entity.enumartion.AcceptanceSaleStockStatus;
import com.shop.mapper.AcceptanceSaleStockMapper;
import com.shop.repository.AcceptanceSaleStockRepository;
import com.shop.specification.AcceptanceSaleStockSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Page<AcceptanceSaleStockResponseDTO> get(AcceptanceSaleStockSpecification specification, Pageable pageable, String userPhoneNumber) {
        List<AcceptanceSaleStockResponseDTO> resultList = repository.findAll(specification).stream()
                .map(mapper::toResponseDTO)
                .filter(responseDTO ->
                        responseDTO.getSellerPhoneNumber().equals(userPhoneNumber) ||
                                responseDTO.getBuyerPhoneNumber().equals(userPhoneNumber)
                )
                .limit((long) pageable.getPageNumber() * pageable.getPageSize())
                .collect(Collectors.toList());

        if (pageable.getPageNumber() > 1)
            resultList = resultList.stream().skip((long) (pageable.getPageNumber() - 1) * pageable.getPageSize()).collect(Collectors.toList());

        return new PageImpl<>(resultList);
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

    public AcceptanceSaleStock getByIdAndSellerId(Long id, Long sellerId) {
        log.debug("Request to get acceptance sale stock by id: {}, sellerId: {}", id, sellerId);
        Optional<AcceptanceSaleStock> optionalEntity = repository.findByIdAndSellerId(id, sellerId);
        if (optionalEntity.isEmpty()) {
            log.error("No such acceptance sale stock exist with id: {}, and sellerId: {}", id, sellerId);
            return null;
        }

        log.debug("Found acceptance sale stock: {}", optionalEntity.get());
        return optionalEntity.get();
    }

    public void update(AcceptanceSaleStock acceptanceSaleStock) {
        log.debug("Try to update acceptance sale stock: {}", acceptanceSaleStock);
        repository.save(acceptanceSaleStock);
    }

    public void updateSellTime(AcceptanceSaleStockUpdateDTO updateDTO) {
        log.debug("Request to update acceptance sale stock times, with dto: {}", updateDTO);
        AcceptanceSaleStock entity = this.getByIdAndSellerId(updateDTO.getId(), updateDTO.getUserId());
        if (entity == null) {
            throw Problem.valueOf(Status.NOT_FOUND, Constant.ACCEPTANCE_SALE_STOCK_NOT_EXIST);
        }

        entity = mapper.fromUpdateDTO(entity, updateDTO);
        this.update(entity);
    }
}
