package SwiftCodeUnitTests;

import com.app.swiftcodesproject.domain.CountrySwiftCodesDTO;
import com.app.swiftcodesproject.domain.SwiftCode;
import com.app.swiftcodesproject.domain.SwiftCodeDTO;
import com.app.swiftcodesproject.mappers.Mapper;
import com.app.swiftcodesproject.repositories.SwiftCodeRepository;
import com.app.swiftcodesproject.services.SwiftCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SwiftCodeServiceTest {

    @Mock
    private SwiftCodeRepository swiftCodeRepository;

    @Mock
    private Mapper<SwiftCode, SwiftCodeDTO> swiftMapper;

    @InjectMocks
    private SwiftCodeService swiftCodeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSwiftCodeDetails() {
        SwiftCode swiftCodeEntity = new SwiftCode();
        swiftCodeEntity.setSwiftCode("TEST1234XXX");
        swiftCodeEntity.setHeadquarter(true);

        SwiftCodeDTO swiftCodeDTO = new SwiftCodeDTO();
        swiftCodeDTO.setSwiftCode("TEST1234XXX");

        when(swiftCodeRepository.findById("TEST1234XXX")).thenReturn(Optional.of(swiftCodeEntity));
        when(swiftMapper.mapToDto(swiftCodeEntity)).thenReturn(swiftCodeDTO);
        when(swiftCodeRepository.findByHeadquarter(swiftCodeEntity)).thenReturn(Collections.emptyList());

        SwiftCodeDTO result = swiftCodeService.getSwiftCodeDetails("TEST1234XXX");

        assertNotNull(result);
        assertEquals("TEST1234XXX", result.getSwiftCode());
        verify(swiftCodeRepository).findById("TEST1234XXX");
        verify(swiftMapper).mapToDto(swiftCodeEntity);
    }

    @Test
    void testGetSwiftCodesByCountry() {
        SwiftCode swiftCodeEntity = new SwiftCode();
        swiftCodeEntity.setCountryISO2("US");
        swiftCodeEntity.setCountryName("UNITED STATES");

        SwiftCodeDTO swiftCodeDTO = new SwiftCodeDTO();
        swiftCodeDTO.setCountryISO2("US");
        swiftCodeDTO.setCountryName("UNITED STATES");

        when(swiftCodeRepository.findByCountryISO2("US")).thenReturn(Collections.singletonList(swiftCodeEntity));
        when(swiftMapper.mapToDto(swiftCodeEntity)).thenReturn(swiftCodeDTO);

        CountrySwiftCodesDTO result = swiftCodeService.getSwiftCodesByCountry("US");

        assertNotNull(result);
        assertEquals("US", result.getCountryISO2());
        assertEquals("UNITED STATES", result.getCountryName());
        assertEquals(1, result.getSwiftCodes().size());
        verify(swiftCodeRepository).findByCountryISO2("US");
    }

    @Test
    void testAddSwiftCode() {
        SwiftCodeDTO swiftCodeDTO = new SwiftCodeDTO();
        swiftCodeDTO.setSwiftCode("TEST1234XXX");
        swiftCodeDTO.setAddress("123 Bank St");
        swiftCodeDTO.setBankName("Bank of Example");
        swiftCodeDTO.setCountryISO2("US");
        swiftCodeDTO.setCountryName("UNITED STATES");

        SwiftCode swiftCodeEntity = new SwiftCode();
        swiftCodeEntity.setSwiftCode("TEST1234XXX");

        when(swiftCodeRepository.existsById("TEST1234XXX")).thenReturn(false);
        when(swiftMapper.mapToEntity(swiftCodeDTO)).thenReturn(swiftCodeEntity);

        swiftCodeService.addSwiftCode(swiftCodeDTO);

        verify(swiftCodeRepository).save(swiftCodeEntity);
    }

    @Test
    void testDeleteSwiftCode() {
        SwiftCode swiftCodeEntity = new SwiftCode();
        swiftCodeEntity.setSwiftCode("TEST1234XXX");

        when(swiftCodeRepository.findById("TEST1234XXX")).thenReturn(Optional.of(swiftCodeEntity));
        when(swiftCodeRepository.findByHeadquarter(swiftCodeEntity)).thenReturn(Collections.emptyList());

        swiftCodeService.deleteSwiftCode("TEST1234XXX");

        verify(swiftCodeRepository).deleteById("TEST1234XXX");
    }
}