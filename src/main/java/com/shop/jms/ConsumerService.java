package com.shop.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.common.Constant;
import com.shop.dto.jms.SaleStockJmsDTO;
import com.shop.dto.saleStock.SaleStockCreateDTO;
import com.shop.dto.saleStock.SaleStockUpdateDTO;
import com.shop.service.higlevel.SaleStockManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.shop.entity.enumartion.SaleStockOperation.CREATE;
import static com.shop.entity.enumartion.SaleStockOperation.UPDATE;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {

    private final ObjectMapper objectMapper;
    private final SaleStockManagerService saleStockManagerService;

    @JmsListener(destination = Constant.STOCK_MANAGER_UPDATE_QUEUE)
    public void consumeStockManagerUpdateQueue(String message) throws JsonProcessingException {
        log.info("Consume message: {}, from stock manager update queue", message);
        SaleStockJmsDTO jmsDTO = objectMapper.readValue(message, SaleStockJmsDTO.class);

        if (jmsDTO.getOperation() == CREATE) {
            SaleStockCreateDTO createDTO = objectMapper.readValue(jmsDTO.getMessage(), SaleStockCreateDTO.class);
            saleStockManagerService.create(createDTO);
        }
        else if (jmsDTO.getOperation() == UPDATE) {
            SaleStockUpdateDTO updateDTO = objectMapper.readValue(jmsDTO.getMessage(), SaleStockUpdateDTO.class);
            saleStockManagerService.update(updateDTO);
        }
    }
}
