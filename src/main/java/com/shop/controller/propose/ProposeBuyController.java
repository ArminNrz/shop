package com.shop.controller.propose;

import com.shop.common.Constant;
import com.shop.dto.acceptanceSaleStock.create.AcceptanceSaleStockCreateDTO;
import com.shop.dto.proposeStock.ProposeBuyStockDetailsDTO;
import com.shop.service.higlevel.proposeBuy.ProposeManagerService;
import com.shop.specification.ProposeBuySpecification;
import com.shop.utility.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping(Constant.BASE_URL + Constant.VERSION + "/propose")
@Slf4j
@RequiredArgsConstructor
public class ProposeBuyController {

    private final ProposeManagerService proposeManagerService;

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<Void> acceptPropose(
            @PathVariable("id") Long proposeBuyStockId,
            @Valid @RequestBody AcceptanceSaleStockCreateDTO createDTO,
            @RequestHeader("Authorization") String token
    ) {
        log.info("REST request to accept propose to buy with id: {}, createDTO: {}", proposeBuyStockId, createDTO);
        proposeManagerService.acceptBuyPropose(createDTO, proposeBuyStockId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Page<ProposeBuyStockDetailsDTO>> getPropose(
            ProposeBuySpecification specification,
            @RequestParam("page") int pageCount,
            @RequestParam("size") int pageSize,
            @RequestHeader("Authorization") String token
    ) {
        log.info("REST request to get propose of user");
        Page<ProposeBuyStockDetailsDTO> page = proposeManagerService.getUserPropose(token, specification, pageCount, pageSize);
        return ResponseEntity.ok()
                .headers(PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page))
                .body(page);
    }
}
