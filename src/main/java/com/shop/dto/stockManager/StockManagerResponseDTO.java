package com.shop.dto.stockManager;

import com.shop.entity.AppUserStockManagerLog;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StockManagerResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Long current;
    private Long forSale;
    private Long total;
    private Long willBuy;
    private List<StockManagerLog> logs;

    @Data
    public static class StockManagerLog {
        private Long lastCurrent;
        private Long lastForSale;
        private Long lastTotal;
        private Long lastWillBuy;
    }
}
