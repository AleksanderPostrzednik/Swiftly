package com.swiftly.swiftly.repository;

import com.swiftly.swiftly.model.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {

    // znajdź rekord na podstawie swiftCode (unikalny)
    SwiftCode findBySwiftCode(String swiftCode);

    // znajdź wszystkie rekordy z danego kraju
    List<SwiftCode> findAllByCountryISO2(String countryISO2);

    // możesz też dodać inne metody, jeśli będą potrzebne
}
