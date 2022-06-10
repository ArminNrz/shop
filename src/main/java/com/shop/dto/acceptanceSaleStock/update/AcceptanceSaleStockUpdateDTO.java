package com.shop.dto.acceptanceSaleStock.update;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.common.Constant;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AcceptanceSaleStockUpdateDTO {

    @JsonIgnore
    private Long id;

    @NotNull(message = Constant.ACCEPTANCE_SALE_STOCK_SELL_TIME_EMPTY)
    private Long sellTime;

    @NotNull(message = Constant.ACCEPTANCE_SALE_STOCK_LOCATION_EMPTY)
    private String sellLocation;

    @JsonIgnore
    private Long userId;
}
