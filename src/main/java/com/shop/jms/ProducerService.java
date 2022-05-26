package com.shop.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.common.Constant;
import com.shop.dto.jms.SaleStockJmsDTO;
import com.shop.dto.saleStock.SaleStockBaseDTO;
import com.shop.entity.enumartion.SaleStockOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProducerService {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public void sendToStockManagerUpdateQueue(SaleStockBaseDTO createDTO, SaleStockOperation operation) throws JsonProcessingException {
        log.debug("Try to put SaleStockCreateDTO : {} with operation: {}, into stock manager queue", createDTO, operation);
        String message = objectMapper.writeValueAsString(createDTO);

        SaleStockJmsDTO jmsDTO = new SaleStockJmsDTO();
        jmsDTO.setMessage(message);
        jmsDTO.setOperation(operation);

        String queueMessage = objectMapper.writeValueAsString(jmsDTO);
        jmsTemplate.convertAndSend(Constant.STOCK_MANAGER_UPDATE_QUEUE, queueMessage);
        log.info("Put SaleStockManager: {}, into stock manager update queue", createDTO);
    }
}
