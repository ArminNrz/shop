package com.shop.dto.saleStock;

import com.shop.common.Constant;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class SaleStockBaseDTO implements Serializable {

    private Long userId;

    @NotNull(message = Constant.SALE_STOCK_STOCK_COUNT_EMPTY)
    private Long stockCount;

    @NotNull(message = Constant.SALE_STOCK_UNIT_PRICE_EMPTY)
    private BigDecimal unitPrice;
}
