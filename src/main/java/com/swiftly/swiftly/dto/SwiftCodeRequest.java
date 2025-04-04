package com.swiftly.swiftly.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
