package com.shop.service.entity;

import com.shop.dto.proposeStock.ProposeBuyStockCreateDTO;
import com.shop.dto.proposeStock.ProposeBuyStockDetailsDTO;
import com.shop.entity.AppUser;
import com.shop.entity.ProposeBuyStock;
import com.shop.entity.SaleStock;
import com.shop.entity.enumartion.ProposeBuyStockStatus;
import com.shop.mapper.ProposeBuyStockMapper;
import com.shop.repository.ProposeBuyStockRepository;
import com.shop.specification.ProposeBuySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProposeBuyStockService {

    private final ProposeBuyStockMapper mapper;
    private final ProposeBuyStockRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(ProposeBuyStockCreateDTO buyStockCreateDTO, SaleStock saleStock, AppUser user) {
        log.debug("Try to create propose buy stock with buyStockDTO: {}, for user: {}", buyStockCreateDTO, user.getId());
        ProposeBuyStock entity = mapper.toEntity(buyStockCreateDTO, saleStock, user);
        entity.setStatus(ProposeBuyStockStatus.OPEN);
        repository.save(entity);
    }

    public ProposeBuyStock findById(Long id) {
        log.debug("Try to find propose buy stock with id: {}", id);
        Optional<ProposeBuyStock> optionalEntity = repository.findById(id);
        if (optionalEntity.isEmpty()) {
            log.error("No such propose buy stock exist with id: {}", id);
            return null;
        }

        log.debug("Found propose buy stock: {}", optionalEntity.get());
        return optionalEntity.get();
    }

    public Page<ProposeBuyStockDetailsDTO> findBySaleStock(Long saleStockId, Pageable pageable) {
        log.debug("Try to find propose to buy, with sale stock id: {}", saleStockId);
        Page<ProposeBuyStock> foundEntities = repository.findBySaleStockId(saleStockId, pageable);
        log.debug("Found entities: {}", foundEntities);
        return foundEntities.map(mapper::toDetailsDTO);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(ProposeBuyStock proposeBuyStock) {
        long id = proposeBuyStock.getId();
        log.debug("Try to delete propose buy stock with id: {}", id);
        repository.delete(proposeBuyStock);
        log.info("Deleted propose buy stock with id: {}", id);
    }

    public Page<ProposeBuyStockDetailsDTO> findByUserId(Long userId, ProposeBuySpecification specification, int pageCount, int pageSize) {
        log.debug("Try to find propose buy stock for user id: {}", userId);
        List<ProposeBuyStockDetailsDTO> resultList = repository.findAll(specification).stream()
                .filter(proposeBuyStock -> proposeBuyStock.getUser().getId().equals(userId))
                .map(mapper::toDetailsDTO)
                .limit((long) pageCount * pageSize)
                .collect(Collectors.toList());

        if (pageCount > 1)
            resultList = resultList.stream().skip((long) (pageCount - 1) * pageSize).collect(Collectors.toList());

        return new PageImpl<>(resultList);
    }
}
