package com.swiftly.swiftly.repository;

import com.swiftly.swiftly.model.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {

    SwiftCode findBySwiftCode(String swiftCode);
    List<SwiftCode> findAllByCountryISO2(String countryISO2);
}
