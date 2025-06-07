package com.swiftly.swiftly.controller;

import com.swiftly.swiftly.service.ExcelParserService;
import com.swiftly.swiftly.service.SwiftCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SwiftCodeController.class)
class SwiftCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SwiftCodeService swiftCodeService;

    @MockBean
    private ExcelParserService excelParserService;

    @Test
    void getSwiftCodeReturnsNotFound() throws Exception {
        when(swiftCodeService.getBySwiftCode("UNKNOWN")).thenReturn(null);
        mockMvc.perform(get("/v1/swift-codes/UNKNOWN"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCodesByCountryReturnsOk() throws Exception {
        when(swiftCodeService.getAllForCountry("PL")).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/v1/swift-codes/country/PL"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteSwiftCodeNotFound() throws Exception {
        doThrow(new RuntimeException("SWIFT code not found: UNKNOWN"))
                .when(swiftCodeService).deleteBySwiftCode("UNKNOWN");
        mockMvc.perform(delete("/v1/swift-codes/UNKNOWN"))
                .andExpect(status().isNotFound());
    }
}
