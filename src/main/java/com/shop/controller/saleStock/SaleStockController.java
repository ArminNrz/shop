package com.shop.controller.saleStock;

import com.shop.common.Constant;
import com.shop.dto.proposeStock.ProposeBuyStockCreateDTO;
import com.shop.dto.proposeStock.ProposeBuyStockDetailsDTO;
import com.shop.dto.saleStock.SaleStockCreateDTO;
import com.shop.dto.saleStock.SaleStockResponseDTO;
import com.shop.dto.saleStock.SaleStockUpdateDTO;
import com.shop.service.entity.SaleStockService;
import com.shop.service.higlevel.SaleStockManagerService;
import com.shop.service.higlevel.proposeBuy.ProposeManagerService;
import com.shop.specification.SaleStockSpecification;
import com.shop.utility.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(Constant.BASE_URL + Constant.VERSION + "/sale-stock")
@Slf4j
@RequiredArgsConstructor
public class SaleStockController {

    private final SaleStockManagerService saleStockManagerService;
    private final SaleStockService service;
    private final ProposeManagerService proposeManagerService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<Void> create(@Valid @RequestBody SaleStockCreateDTO createDTO, @RequestHeader("Authorization") String token) {
        log.info("REST request to create sale stock with createDTO: {}", createDTO);
        saleStockManagerService.create(createDTO, token);
        return ResponseEntity.created(URI.create(Constant.BASE_URL + Constant.VERSION + "/sale-stock")).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<Void> update(
            @Valid @RequestBody SaleStockUpdateDTO updateDTO,
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String token
    ) {
        log.info("REST request to update sale stock with updateDTO: {}", updateDTO);
        updateDTO.setId(id);
        saleStockManagerService.update(updateDTO, token);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> getGeneral(
            SaleStockSpecification specification,
            @PageableDefault(sort = "updated", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.info("REST request to get general sale stocks, with specification: {}, pageable: {}", specification, pageable);
        Page<SaleStockResponseDTO> page = service.findGeneral(specification, pageable);
        return ResponseEntity.ok()
                .headers(PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page))
                .body(page);
    }

    @GetMapping("/{id}/get-propose")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<Page<ProposeBuyStockDetailsDTO>> getWithDetails(
            @PathVariable("id") Long saleStockId,
            @PageableDefault(sort = "created", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestHeader("Authorization") String token
    ) {
        log.info("REST request to get sale stock propose, for sale stock id: {}, pageable: {}", saleStockId, pageable);
        Page<ProposeBuyStockDetailsDTO> page = saleStockManagerService.getProposeDetails(saleStockId, pageable, token);
        return ResponseEntity.ok()
                .headers(PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page))
                .body(page);
    }

    @PostMapping("/{saleStockId}/propose-buy")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> createBuyPropose(
            @PathVariable("saleStockId") Long saleStockId,
            @Valid @RequestBody ProposeBuyStockCreateDTO buyStockCreateDTO,
            @RequestHeader("Authorization") String token
    ) {
        log.info("REST request to propose buy stock, for sale stock with id: {}, buyStockDTO: {}", saleStockId, buyStockCreateDTO);
        buyStockCreateDTO.setSaleStockId(saleStockId);
        proposeManagerService.buyPropose(buyStockCreateDTO, token);
        return ResponseEntity.created(URI.create(Constant.BASE_URL + Constant.VERSION + "/sale-stock")).build();
    }

    @DeleteMapping("/propose-buy/{proposeBuyStockId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<Void> deleteProposeBuyStock(
            @PathVariable("proposeBuyStockId") Long proposeBuyStockId,
            @RequestHeader("Authorization") String token
    ) {
        log.info("REST request to delete propose buy stock with id: {}", proposeBuyStockId);
        proposeManagerService.deletePropose(proposeBuyStockId, token);
        return ResponseEntity.noContent().build();
    }
}
