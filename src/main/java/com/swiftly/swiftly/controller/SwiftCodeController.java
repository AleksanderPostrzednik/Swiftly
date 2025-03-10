package com.swiftly.swiftly.controller;

import com.swiftly.swiftly.model.SwiftCode;
import com.swiftly.swiftly.service.SwiftCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftCodeController {

    private final SwiftCodeService swiftCodeService;

    public SwiftCodeController(SwiftCodeService swiftCodeService) {
        this.swiftCodeService = swiftCodeService;
    }

    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> getSwiftCode(@PathVariable String swiftCode) {
        SwiftCode code = swiftCodeService.getBySwiftCode(swiftCode);
        if (code == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "SWIFT code not found"));
        }

        if (code.isHeadquarter()) {

            List<SwiftCode> branches = swiftCodeService.getBranchesForHQ(swiftCode);

            Map<String, Object> response = new HashMap<>();
            response.put("address", code.getAddress());
            response.put("bankName", code.getBankName());
            response.put("countryISO2", code.getCountryISO2());
            response.put("countryName", code.getCountryName());
            response.put("isHeadquarter", true);
            response.put("swiftCode", code.getSwiftCode());

            List<Map<String, Object>> branchList = branches.stream().map(b -> {
                Map<String, Object> m = new HashMap<>();
                m.put("address", b.getAddress());
                m.put("bankName", b.getBankName());
                m.put("countryISO2", b.getCountryISO2());
                m.put("isHeadquarter", b.isHeadquarter());
                m.put("swiftCode", b.getSwiftCode());
                return m;
            }).toList();

            response.put("branches", branchList);

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("address", code.getAddress());
            response.put("bankName", code.getBankName());
            response.put("countryISO2", code.getCountryISO2());
            response.put("countryName", code.getCountryName());
            response.put("isHeadquarter", false);
            response.put("swiftCode", code.getSwiftCode());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/country/{iso2}")
    public ResponseEntity<?> getAllCodesForCountry(@PathVariable String iso2) {
        List<SwiftCode> list = swiftCodeService.getAllForCountry(iso2);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No SWIFT codes found for country " + iso2));
        }

        String countryName = list.get(0).getCountryName();

        Map<String, Object> response = new HashMap<>();
        response.put("countryISO2", iso2.toUpperCase());
        response.put("countryName", countryName);

        List<Map<String, Object>> swiftCodesList = list.stream().map(code -> {
            Map<String, Object> m = new HashMap<>();
            m.put("address", code.getAddress());
            m.put("bankName", code.getBankName());
            m.put("countryISO2", code.getCountryISO2());
            m.put("isHeadquarter", code.isHeadquarter());
            m.put("swiftCode", code.getSwiftCode());
            return m;
        }).toList();

        response.put("swiftCodes", swiftCodesList);
        return ResponseEntity.ok(response);
    }

    // POST /v1/swift-codes
    @PostMapping
    public ResponseEntity<?> createSwiftCode(@RequestBody SwiftCode body) {
        SwiftCode saved = null;
        try {
            saved = swiftCodeService.createSwiftCode(body);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }

        return ResponseEntity.ok(Map.of("message", "SWIFT code added successfully",
                "swiftCode", saved.getSwiftCode()));
    }

    // DELETE /v1/swift-codes/{swift-code}
    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<?> deleteSwiftCode(@PathVariable String swiftCode) {
        try {
            swiftCodeService.deleteBySwiftCode(swiftCode);
            return ResponseEntity.ok(Map.of("message",
                    "SWIFT code " + swiftCode + " has been deleted."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
