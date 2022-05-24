package com.shop.controller.stockManager;

import com.shop.common.Constant;
import com.shop.dto.stockManager.StockManagerCreateBatchResponseDTO;
import com.shop.dto.stockManager.StockManagerCreateDTO;
import com.shop.service.entity.UserStockManagerService;
import com.shop.service.higlevel.UserStockManagerBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(Constant.BASE_URL + Constant.VERSION + "/stock-manager")
@Slf4j
@RequiredArgsConstructor
public class StockManagerController {

    private static final URI uri = URI.create(Constant.BASE_URL + Constant.VERSION + "/stock-manager");

    private final UserStockManagerService stockManagerService;
    private final UserStockManagerBatchService stockManagerBatchService;

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
        return ResponseEntity.created(uri).body(stockManagerBatchService.createBatch(excelFile));
    }
}
