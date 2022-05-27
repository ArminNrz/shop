package com.shop.dto.saleStock;

import com.shop.entity.enumartion.SaleStockStatus;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SaleStockResponseDTO implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Long forSaleCount;
    private BigDecimal unitPrice;
    private SaleStockStatus status;
    private Long updated;
}
