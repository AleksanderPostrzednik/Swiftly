package com.swiftly.swiftly.model;

import jakarta.persistence.*;

@Entity
@Table(name = "swift_codes")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCountryISO2() {
        return countryISO2;
    }

    public void setCountryISO2(String countryISO2) {
        this.countryISO2 = countryISO2;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isHeadquarter() {
        return isHeadquarter;
    }

    public void setHeadquarter(boolean headquarter) {
        isHeadquarter = headquarter;
    }
}
