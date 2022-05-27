package com.shop.dto.stockManager;

import lombok.Data;

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
    private Long updated;
    private List<StockManagerLog> logs;

    @Data
    public static class StockManagerLog {
        private Long lastCurrent;
        private Long lastForSale;
        private Long lastTotal;
        private Long lastWillBuy;
        private Long created;
    }
}
