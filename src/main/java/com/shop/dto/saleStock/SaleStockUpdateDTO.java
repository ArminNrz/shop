package com.shop.dto.saleStock;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SaleStockUpdateDTO extends SaleStockBaseDTO {

    private Long id;

    public String toString() {
        return "SaleStockUpdateDTO(id=" + this.getId() + super.toString() + ")";
    }
}
