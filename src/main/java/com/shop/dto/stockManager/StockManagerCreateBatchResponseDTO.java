package com.shop.dto.stockManager;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StockManagerCreateBatchResponseDTO {

    private List<ResponseDTO> responses;

    public void addResponse(ResponseDTO responseDTO) {

        if (this.responses == null || this.responses.size() == 0) {
            this.responses = new ArrayList<>();
            this.responses.add(responseDTO);
        }
        else {
            this.responses.add(responseDTO);
        }

    }

    @Data
    public static class ResponseDTO {
        private String phoneNumber;
        private String message;
    }
}
