package com.shop.controller.saleStock;

import com.shop.common.Constant;
import com.shop.dto.saleStock.SaleStockCreateDTO;
import com.shop.dto.saleStock.SaleStockResponseDTO;
import com.shop.dto.saleStock.SaleStockUpdateDTO;
import com.shop.service.entity.SaleStockService;
import com.shop.service.higlevel.SaleStockManagerService;
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

    private final SaleStockManagerService managerService;
    private final SaleStockService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<Void> create(@Valid @RequestBody SaleStockCreateDTO createDTO, @RequestHeader("Authorization") String token) {
        log.info("REST request to create sale stock with createDTO: {}", createDTO);
        managerService.create(createDTO, token);
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
        managerService.update(updateDTO, token);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> getGeneral(
            SaleStockSpecification specification,
            @PageableDefault(sort = "updated", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.info("REST request to get general sale stocks, with specification: {}, pageable: {}", specification, pageable);
        Page<SaleStockResponseDTO> page = service.findByUserId(specification, pageable);
        return ResponseEntity.ok()
                .headers(PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page))
                .body(page);
    }
}
