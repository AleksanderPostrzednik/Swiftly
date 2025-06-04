package com.swiftly.swiftly.service;

import com.swiftly.swiftly.repository.SwiftCodeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class SwiftCodeServiceTest {

    @Test
    void testIsHeadquarterCode() {
        SwiftCodeService service = new SwiftCodeService(mock(SwiftCodeRepository.class));
        Assertions.assertTrue(service.isHeadquarterCode("ABCDEF12"));
        Assertions.assertTrue(service.isHeadquarterCode("ABCDEF12XXX"));
        Assertions.assertFalse(service.isHeadquarterCode("ABCDEF12345"));
        Assertions.assertFalse(service.isHeadquarterCode("SHORT"));
        Assertions.assertFalse(service.isHeadquarterCode(null));
    }
}
