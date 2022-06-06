package com.shop.controller.acceptanceSaleStock;

import com.shop.common.Constant;
import com.shop.dto.acceptanceSaleStock.AcceptanceSaleStockResponseDTO;
import com.shop.dto.acceptanceSaleStock.AcceptanceSaleStockUpdateDTO;
import com.shop.service.higlevel.proposeBuy.ProposeManagerService;
import com.shop.specification.AcceptanceSaleStockSpecification;
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

@RestController
@RequestMapping(Constant.BASE_URL + Constant.VERSION + "/acceptance")
@Slf4j
@RequiredArgsConstructor
public class AcceptanceSaleStockController {

    private final ProposeManagerService proposeManagerService;

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<Void> updateAcceptance(
            @PathVariable("id") Long acceptanceId,
            @Valid @RequestBody AcceptanceSaleStockUpdateDTO updateDTO,
            @RequestHeader("Authorization") String token
    ) {
        log.info("REST request to update acceptance with id: {}, updateDTO: {}", acceptanceId, updateDTO);
        updateDTO.setAcceptanceId(acceptanceId);
        proposeManagerService.updateAcceptance(token, updateDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<Page<AcceptanceSaleStockResponseDTO>> getAcceptance(
            AcceptanceSaleStockSpecification specification,
            @PageableDefault(sort = "updated", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestHeader("Authorization") String token
    ) {
        log.info("REST request to get acceptance sale stock, with specification: {}, pageable: {}", specification, pageable);
        Page<AcceptanceSaleStockResponseDTO> page = proposeManagerService.getAcceptance(specification, pageable, token);
        return ResponseEntity.ok()
                .headers(PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page))
                .body(page);
    }

    @PutMapping("/{id}/transfer")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Void> transferStock(
            @PathVariable("id") Long acceptanceId,
            @RequestHeader("Authorization") String token
    ) {
        log.info("REST request to transfer acceptance sale stock with id: {}", acceptanceId);
        proposeManagerService.transferAcceptanceStock(acceptanceId, token);
        return ResponseEntity.ok().build();
    }
}
