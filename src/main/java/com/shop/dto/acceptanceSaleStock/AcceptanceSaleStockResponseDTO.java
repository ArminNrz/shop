package com.shop.dto.acceptanceSaleStock;

import com.shop.entity.enumartion.AcceptanceSaleStockStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AcceptanceSaleStockResponseDTO {
    private Long id;

    private String sellerFirstName;
    private String sellerLastName;
    private String sellerPhoneNumber;

    private String buyerFirstName;
    private String buyerLastName;
    private String buyerPhoneNumber;

    private Long stockCount;
    private BigDecimal unitPrice;
    private AcceptanceSaleStockStatus status;
}
