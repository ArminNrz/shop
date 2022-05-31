package com.shop.dto.proposeStock;

import com.shop.entity.enumartion.ProposeBuyStockStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProposeBuyStockDetailsDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Long proposeCount;
    private BigDecimal proposeUnitCost;
    private ProposeBuyStockStatus status;
}
