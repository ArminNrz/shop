package com.shop.dto.stockManager;

import com.shop.common.Constant;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class StockManagerCreateDTO {
    @NotNull(message = Constant.STOCK_MANAGER_USER_EMPTY)
    private Long userId;
    @Min(value = 0, message = Constant.STOCK_MANAGER_TOTAL_NOT_NEGATIVE)
    private Long total;
}
