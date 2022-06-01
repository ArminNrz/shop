package com.shop.service.entity.userStockManager;

import com.shop.entity.AppUserStockManagerLog;
import com.shop.entity.AppUserStocksManager;
import com.shop.mapper.UserStockManagerMapper;
import com.shop.repository.AppUserStockManagerLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserStockManagerLogHandler {

    private final AppUserStockManagerLogRepository repository;
    private final UserStockManagerMapper mapper;

    public void saveLog(AppUserStocksManager entity, String desc, Long modifierId) {
        AppUserStockManagerLog logEntities = mapper.toLog(entity);
        logEntities.setDescription(desc);
        logEntities.setModifier(modifierId);
        log.debug("Try to save log for log logEntities: {}", logEntities);
        repository.save(logEntities);
        log.debug("Saved logs for user stock managers");
    }
}
