package com.shop.dto.stockManager;

import lombok.Data;

@Data
public class StockManagerCreateBatchDTO {
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Long total;
}
