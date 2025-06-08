package com.swiftly.swiftly.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Simple DTO used for creating new {@code SwiftCode} entries via the REST API.
 */
public class SwiftCodeRequest {

    @NotBlank(message = "SWIFT code cannot be blank")
    @Size(min = 8, max = 11, message = "SWIFT code must be between 8 and 11 characters")
    private String swiftCode;

    @NotBlank(message = "Bank name cannot be blank")
    private String bankName;

    @NotBlank(message = "Country ISO2 must be provided")
    @Size(min = 2, max = 2, message = "Country ISO2 must be 2 letters")
    private String countryISO2;

    @NotBlank(message = "Country name cannot be blank")
    private String countryName;

    private String address;

    public SwiftCodeRequest() {
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
}
