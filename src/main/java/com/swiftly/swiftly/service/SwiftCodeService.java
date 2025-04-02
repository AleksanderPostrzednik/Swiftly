package com.swiftly.swiftly.service;

import com.swiftly.swiftly.model.SwiftCode;
import com.swiftly.swiftly.repository.SwiftCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwiftCodeService {

    private final SwiftCodeRepository swiftCodeRepository;

    @Autowired
    public SwiftCodeService(SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
    }

    public SwiftCode getBySwiftCode(String swiftCode) {
        return swiftCodeRepository.findBySwiftCode(swiftCode);
    }

    public List<SwiftCode> getAllForCountry(String iso2) {
        iso2 = iso2.toUpperCase();
        return swiftCodeRepository.findAllByCountryISO2(iso2);
    }

    public SwiftCode createSwiftCode(SwiftCode code) {

        code.setCountryISO2(code.getCountryISO2().toUpperCase());
        code.setCountryName(code.getCountryName().toUpperCase());

        boolean isHQ = isHeadquarterCode(code.getSwiftCode());
        code.setHeadquarter(isHQ);

        return swiftCodeRepository.save(code);
    }

    public void deleteBySwiftCode(String swiftCode) {
        SwiftCode existing = swiftCodeRepository.findBySwiftCode(swiftCode);
        if (existing != null) {
            swiftCodeRepository.delete(existing);
        } else {
            throw new RuntimeException("SWIFT code not found: " + swiftCode);
        }
    }

    public boolean isHeadquarterCode(String swiftCode) {

        if (swiftCode.length() <= 8) {
            return true;
        }
        return swiftCode.length() == 11 && swiftCode.endsWith("XXX");
    }
}
