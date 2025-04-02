package com.swiftly.swiftly.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "swift_codes")
@Getter
@Setter
public class SwiftCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "swift_code", nullable = false, unique = true)
    private String swiftCode;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "country_iso2", nullable = false)
    private String countryISO2;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Column(name = "address")
    private String address;

    @Column(name = "is_headquarter", nullable = false)
    private boolean isHeadquarter;

    public SwiftCode() {
    }

    public SwiftCode(String swiftCode,
                     String bankName,
                     String countryISO2,
                     String countryName,
                     String address,
                     boolean isHeadquarter) {
        this.swiftCode = swiftCode;
        this.bankName = bankName;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.address = address;
        this.isHeadquarter = isHeadquarter;
    }
}
