package com.shop.service.higlevel.userStockManager;

import com.shop.common.Constant;
import com.shop.dto.stockManager.StockManagerCreateBatchDTO;
import com.shop.dto.stockManager.StockManagerCreateBatchResponseDTO;
import com.shop.entity.AppUser;
import com.shop.entity.AppUserStocksManager;
import com.shop.mapper.UserStockManagerMapper;
import com.shop.service.entity.UserService;
import com.shop.service.entity.UserStockManagerService;
import com.shop.service.lowlevel.ExcelHelperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class StockManagerBatchHandler {

    private final ExcelHelperService excelHelperService;
    private final UserStockManagerService userStockManagerService;
    private final UserService userService;
    private final UserStockManagerMapper stockManagerMapper;

    public StockManagerCreateBatchResponseDTO createBatch(MultipartFile excelFile) {
        log.debug("Request to create batch user stock manager with excel file");

        checkExcelFileFormat(excelFile);

        List<StockManagerCreateBatchDTO> createBatchDTOS = getCreateBatchDTOList(excelFile);

        List<AppUserStocksManager> appUserStocksManagers = new ArrayList<>();
        StockManagerCreateBatchResponseDTO responseDTO = new StockManagerCreateBatchResponseDTO();
        createBatchDTOS.forEach(batchDTO -> normalizedExcelDTO(appUserStocksManagers, responseDTO, batchDTO));

        userStockManagerService.createBatch(appUserStocksManagers);
        return responseDTO;
    }

    private void normalizedExcelDTO(List<AppUserStocksManager> appUserStocksManagers, StockManagerCreateBatchResponseDTO responseDTO, StockManagerCreateBatchDTO batchDTO) {

        StockManagerCreateBatchResponseDTO.ResponseDTO response = new StockManagerCreateBatchResponseDTO.ResponseDTO();
        response.setPhoneNumber(batchDTO.getPhoneNumber());
        AppUser user = userService.getByPhoneNumber(batchDTO.getPhoneNumber());

        if (user == null) {
            response.setMessage(Constant.APP_USER_NOT_FOUND);
        }
        else {
            AppUserStocksManager entity = stockManagerMapper.toEntity(batchDTO, user);

            try {
                userStockManagerService.checkUserStockManagerNotExist(user.getId());
                appUserStocksManagers.add(entity);
                response.setMessage(Constant.STOCK_MANAGER_SAVED_SUCCESS);
            } catch (DefaultProblem exception) {
                response.setMessage(Constant.STOCK_MANAGER_USER_EXIST_BEFORE);
            }
        }

        responseDTO.addResponse(response);
    }

    private void checkExcelFileFormat(MultipartFile excelFile) {
        if (!excelHelperService.hasExcelFormat(excelFile)) {
            log.error("Uploaded excel file has not correct format");
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.EXCEL_FILE_HAS_PROBLEM);
        }
    }

    private List<StockManagerCreateBatchDTO> getCreateBatchDTOList(MultipartFile excelFile) {
        try {
            return excelHelperService.excelToUserStockManagerDTOS(excelFile.getInputStream());
        } catch (IOException e) {
            log.error("There is an error in casting excel file to user stock manager dto, error message: {}", e.getMessage());
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.EXCEL_FILE_FORMAT_NOT_CORRECT);
        }
    }
}
