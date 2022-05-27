package com.shop.dto.saleStock;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SaleStockCreateDTO extends SaleStockBaseDTO {
    public String toString() {
        return super.toString();
    }
}
