package com.swiftly.swiftly.controller;

import com.swiftly.swiftly.dto.SwiftCodeRequest;
import com.swiftly.swiftly.model.SwiftCode;
import com.swiftly.swiftly.service.ExcelParserService;
import com.swiftly.swiftly.service.SwiftCodeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftCodeController {

    private final SwiftCodeService swiftCodeService;
    private final ExcelParserService excelParserService;

    public SwiftCodeController(SwiftCodeService swiftCodeService,
                               ExcelParserService excelParserService) {
        this.swiftCodeService = swiftCodeService;
        this.excelParserService = excelParserService;
    }

    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> getSwiftCode(@PathVariable String swiftCode) {
        SwiftCode code = swiftCodeService.getBySwiftCode(swiftCode);
        if (code == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "SWIFT code not found: " + swiftCode));
        }
        return ResponseEntity.ok(code);
    }

    @GetMapping("/country/{iso2}")
    public ResponseEntity<?> getSwiftCodesByCountry(@PathVariable String iso2) {
        return ResponseEntity.ok(swiftCodeService.getAllForCountry(iso2));
    }

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<?> deleteSwiftCode(@PathVariable String swiftCode) {
        swiftCodeService.deleteBySwiftCode(swiftCode);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }
    @PostMapping("/import")
    public ResponseEntity<?> importSwiftCodes(@RequestParam("file") MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            excelParserService.parseAndSave(is);
            return ResponseEntity.ok(Map.of("message", "File imported successfully"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Cannot parse Excel file: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createSwiftCode(@Valid @RequestBody SwiftCodeRequest request) {
        SwiftCode code = new SwiftCode(
                request.getSwiftCode(),
                request.getBankName(),
                request.getCountryISO2(),
                request.getCountryName(),
                request.getAddress(),
                false
        );
        SwiftCode saved = swiftCodeService.createSwiftCode(code);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "SWIFT code added successfully",
                        "swiftCode", saved.getSwiftCode()));
    }
}
