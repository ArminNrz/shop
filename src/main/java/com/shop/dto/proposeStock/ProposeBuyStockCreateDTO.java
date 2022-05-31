package com.shop.dto.proposeStock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.common.Constant;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProposeBuyStockCreateDTO {
    @JsonIgnore
    private Long saleStockId;

    @NotNull(message = Constant.PROPOSE_BUY_STOCK_COUNT_EMPTY)
    private Long proposeCount;

    private BigDecimal proposeUnitCost;

    @JsonIgnore
    private Long userId;
}
