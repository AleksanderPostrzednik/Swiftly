package com.swiftly.swiftly.service;

import com.swiftly.swiftly.model.SwiftCode;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ExcelParserService {

    private final SwiftCodeService swiftCodeService;

    public ExcelParserService(SwiftCodeService swiftCodeService) {
        this.swiftCodeService = swiftCodeService;
    }

    public void parseAndSave(InputStream excelFile) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(excelFile)) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }

                String countryISO2 = getCellValue(row, 0);
                String swiftCode    = getCellValue(row, 1);

                String bankName     = getCellValue(row, 3);
                String address      = getCellValue(row, 4);

                String countryName  = getCellValue(row, 6);

                if (countryISO2.isEmpty() && swiftCode.isEmpty() && bankName.isEmpty()) {
                    continue;
                }

                SwiftCode code = new SwiftCode(
                        swiftCode,
                        bankName,
                        countryISO2,
                        countryName,
                        address,
                        false
                );

                swiftCodeService.createSwiftCode(code);
            }
        }
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return "";
        }
        return cell.toString().trim();
    }
}
