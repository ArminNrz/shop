package com.shop.controller.stockManager;

import com.shop.common.Constant;
import com.shop.dto.stockManager.StockManagerCreateBatchResponseDTO;
import com.shop.dto.stockManager.StockManagerCreateDTO;
import com.shop.dto.stockManager.StockManagerResponseDTO;
import com.shop.service.higlevel.userStockManager.StockManagerService;
import com.shop.specification.AppUserStockManagerSpecification;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(Constant.BASE_URL + Constant.VERSION + "/stock-manager")
@Slf4j
@RequiredArgsConstructor
public class StockManagerController {

    private static final URI uri = URI.create(Constant.BASE_URL + Constant.VERSION + "/stock-manager");

    private final StockManagerService stockManagerService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Void> create(@Valid @RequestBody StockManagerCreateDTO createDTO) {
        log.info("REST request to create user stock manager with StockManagerDTO: {}", createDTO);
        stockManagerService.create(createDTO);
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/upload-excel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<StockManagerCreateBatchResponseDTO> createBatch(@RequestParam("file") MultipartFile excelFile) {
        log.info("REST request to create user stock manager with excel file");
        return ResponseEntity.created(uri).body(stockManagerService.createBatch(excelFile));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> get(AppUserStockManagerSpecification specification,
                                 @PageableDefault(sort = "updated", direction = Sort.Direction.DESC) Pageable pageable,
                                 @RequestHeader("Authorization") String token
    ) {
        log.info("REST request to get user stock manager with specification: {}, pageable: {}", specification, pageable);
        Page<StockManagerResponseDTO> page = stockManagerService.getAll(specification, pageable, token);
        return ResponseEntity.ok()
                .headers(PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page))
                .body(page);
    }
}
