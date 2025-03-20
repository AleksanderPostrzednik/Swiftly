package com.swiftly.swiftly.service;

import com.swiftly.swiftly.model.SwiftCode;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
                if (row == null) continue;

                String countryISO2 = row.getCell(0).getStringCellValue();
                String swiftCode = row.getCell(1).getStringCellValue();
                String bankName = row.getCell(3).getStringCellValue();
                String address = row.getCell(4).getStringCellValue();
                String countryName = row.getCell(6).getStringCellValue();

                SwiftCode code = new SwiftCode(swiftCode, bankName, countryISO2, countryName, address, swiftCode.endsWith("XXX"));
                swiftCodeService.createSwiftCode(code);
            }
        }
    }
}
