package com.shop.controller.acceptanceSaleStock;

import com.shop.common.Constant;
import com.shop.dto.acceptanceSaleStock.create.AcceptanceSaleStockResponseDTO;
import com.shop.dto.acceptanceSaleStock.update.AcceptanceSaleStockUpdateDTO;
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

    @PutMapping("/{id}/update-location")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<Void> updateLocation(
            @PathVariable("id") Long id,
            @Valid @RequestBody AcceptanceSaleStockUpdateDTO updateDTO,
            @RequestHeader("Authorization") String token
    ) {
        log.info("REST request to update acceptance sale stock location and time, with dto: {}", updateDTO);
        updateDTO.setId(id);
        proposeManagerService.updateAcceptance(updateDTO, token);
        return ResponseEntity.ok().build();
    }
}
