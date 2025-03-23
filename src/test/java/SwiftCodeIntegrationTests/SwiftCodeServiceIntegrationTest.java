package com.app.swiftcodesproject.services;

import com.app.swiftcodesproject.domain.CountrySwiftCodesDTO;
import com.app.swiftcodesproject.domain.SwiftCode;
import com.app.swiftcodesproject.domain.SwiftCodeDTO;
import com.app.swiftcodesproject.repositories.SwiftCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SwiftCodeServiceIntegrationTest {

    @Autowired
    private SwiftCodeService swiftCodeService;

    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

    @BeforeEach
    void setUp() {
        swiftCodeRepository.deleteAll();
    }

    @Test
    void testGetSwiftCodeDetails() {
        SwiftCode swiftCodeEntity = new SwiftCode();
        swiftCodeEntity.setSwiftCode("TEST1234XXX");
        swiftCodeEntity.setHeadquarter(true);
        swiftCodeRepository.save(swiftCodeEntity);

        SwiftCodeDTO result = swiftCodeService.getSwiftCodeDetails("TEST1234XXX");

        assertNotNull(result);
        assertEquals("TEST1234XXX", result.getSwiftCode());
    }

    @Test
    void testGetSwiftCodesByCountry() {
        SwiftCode swiftCodeEntity = new SwiftCode();
        swiftCodeEntity.setSwiftCode("TEST1234XXX");
        swiftCodeEntity.setCountryISO2("US");
        swiftCodeEntity.setCountryName("UNITED STATES");
        swiftCodeRepository.save(swiftCodeEntity);

        CountrySwiftCodesDTO result = swiftCodeService.getSwiftCodesByCountry("US");

        assertNotNull(result);
        assertEquals("US", result.getCountryISO2());
        assertEquals("UNITED STATES", result.getCountryName());
        assertEquals(1, result.getSwiftCodes().size());
    }

    @Test
    void testAddSwiftCode() {
        SwiftCodeDTO swiftCodeDTO = new SwiftCodeDTO();
        swiftCodeDTO.setSwiftCode("TEST1234XXX");
        swiftCodeDTO.setAddress("123 Bank St");
        swiftCodeDTO.setBankName("Bank of Example");
        swiftCodeDTO.setCountryISO2("US");
        swiftCodeDTO.setCountryName("UNITED STATES");

        swiftCodeService.addSwiftCode(swiftCodeDTO);

        Optional<SwiftCode> result = swiftCodeRepository.findById("TEST1234XXX");
        assertTrue(result.isPresent());
        assertEquals("TEST1234XXX", result.get().getSwiftCode());
    }

    @Test
    void testDeleteSwiftCode() {
        SwiftCode swiftCodeEntity = new SwiftCode();
        swiftCodeEntity.setSwiftCode("TEST1234XXX");
        swiftCodeRepository.save(swiftCodeEntity);

        swiftCodeService.deleteSwiftCode("TEST1234XXX");

        Optional<SwiftCode> result = swiftCodeRepository.findById("TEST1234XXX");
        assertFalse(result.isPresent());
    }
}