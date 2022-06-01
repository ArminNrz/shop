package com.shop.service.lowlevel;

import com.shop.common.Constant;
import com.shop.dto.stockManager.StockManagerCreateBatchDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class ExcelHelperService {

    private final static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private final static String SHEET = "سهام";

    public boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public List<StockManagerCreateBatchDTO> excelToUserStockManagerDTOS(InputStream inputStream) {
        log.debug("Try to cast excel file to stock manager create DTO");

        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<StockManagerCreateBatchDTO> stockManagerCreateBatchDTOS = new ArrayList<>();
            int rowNumber = 0;

            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellInRow = currentRow.iterator();
                StockManagerCreateBatchDTO dto = new StockManagerCreateBatchDTO();
                int cellIdx = 0;

                while (cellInRow.hasNext()) {
                    Cell currentCell = cellInRow.next();

                    switch (cellIdx) {
                        case 0:
                            long numericCellValue = (long) currentCell.getNumericCellValue();
                            String phoneNumber = "0" + numericCellValue;
                            dto.setPhoneNumber(phoneNumber);
                            break;
                        case 1:
                            String firstName = currentCell.getStringCellValue();
                            dto.setFirstName(firstName);
                            break;
                        case 2:
                            String lastName = currentCell.getStringCellValue();
                            dto.setLastName(lastName);
                            break;
                        case 3:
                            dto.setTotal((long) currentCell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                stockManagerCreateBatchDTOS.add(dto);
            }

            workbook.close();
            return stockManagerCreateBatchDTOS;
        } catch (IOException e) {
            log.error("There is an error in casting excel file to workbook, error: {}", e.getMessage());
            throw Problem.valueOf(Status.INTERNAL_SERVER_ERROR, Constant.INTERNAL_ERROR);
        }
    }
}
