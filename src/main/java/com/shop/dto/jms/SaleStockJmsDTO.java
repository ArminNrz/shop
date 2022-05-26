package com.shop.dto.jms;

import com.shop.entity.enumartion.SaleStockOperation;
import lombok.Data;

@Data
public class SaleStockJmsDTO {
    private SaleStockOperation operation;
    private String message;
}
