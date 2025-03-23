package com.app.swiftcodesproject.services;

import com.app.swiftcodesproject.domain.CountrySwiftCodesDTO;
import com.app.swiftcodesproject.domain.SwiftCode;
import com.app.swiftcodesproject.domain.SwiftCodeDTO;
import com.app.swiftcodesproject.mappers.Mapper;
import com.app.swiftcodesproject.repositories.SwiftCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SwiftCodeService {
    private final SwiftCodeRepository swiftCodeRepository;
    private final Mapper<SwiftCode, SwiftCodeDTO> swiftMapper;

    public SwiftCodeService(SwiftCodeRepository swiftCodeRepository, Mapper<SwiftCode, SwiftCodeDTO> swiftMapper) {
        this.swiftCodeRepository = swiftCodeRepository;
        this.swiftMapper = swiftMapper;
    }

    public SwiftCodeDTO getSwiftCodeDetails(String swiftCode) {
        SwiftCode swiftCodeEntity = swiftCodeRepository.findById(swiftCode)
                .orElseThrow(() -> new RuntimeException("SWIFT code not found"));

        SwiftCodeDTO swiftCodeDTO = swiftMapper.mapToDto(swiftCodeEntity);

        if (swiftCodeEntity.isHeadquarter()) {
            List<SwiftCodeDTO> branches = swiftCodeRepository.findByHeadquarter(swiftCodeEntity)
                    .stream()
                    .map(swiftMapper::mapToDto)
                    .collect(Collectors.toList());
            swiftCodeDTO.setBranches(branches);
        }

        return swiftCodeDTO;
    }

    public CountrySwiftCodesDTO getSwiftCodesByCountry(String countryISO2) {
        List<SwiftCode> swiftCodes = swiftCodeRepository.findByCountryISO2(countryISO2);
        List<SwiftCodeDTO> swiftCodeDTOs = swiftCodes.stream()
                .map(swiftMapper::mapToDto)
                .collect(Collectors.toList());

        String countryName = swiftCodes.isEmpty() ? "" : swiftCodes.get(0).getCountryName();

        return CountrySwiftCodesDTO.builder()
                .countryISO2(countryISO2)
                .countryName(countryName)
                .swiftCodes(swiftCodeDTOs)
                .build();
    }

    @Transactional
    public void addSwiftCode(SwiftCodeDTO swiftCodeDTO) {
        String swiftCode = swiftCodeDTO.getSwiftCode();
        int length = swiftCode.length();

        System.out.println("isHeadquarter = " + swiftCodeDTO.isHeadquarter());

        if (length != 11) {
            throw new RuntimeException("SWIFT code must be 11 characters long");
        }

        String lastThree = swiftCode.substring(8).toUpperCase();
        swiftCodeDTO.setHeadquarter(lastThree.equals("XXX"));
        swiftCodeDTO.setSwiftCode(swiftCode.substring(0, 8) + lastThree);

        if (swiftCodeDTO.getAddress().isEmpty() || swiftCodeDTO.getBankName().isEmpty() ||
                swiftCodeDTO.getCountryISO2().isEmpty() || swiftCodeDTO.getCountryName().isEmpty()) {
            throw new RuntimeException("All fields must be filled");
        }

        if (swiftCodeRepository.existsById(swiftCodeDTO.getSwiftCode())) {
            throw new RuntimeException("SWIFT code must be unique");
        }

        swiftCodeDTO.setCountryISO2(swiftCodeDTO.getCountryISO2().toUpperCase());
        swiftCodeDTO.setCountryName(swiftCodeDTO.getCountryName().toUpperCase());

        SwiftCode swiftCodeEntity = swiftMapper.mapToEntity(swiftCodeDTO);

        if (!swiftCodeDTO.isHeadquarter()) {
            String headquarterCode = swiftCodeDTO.getSwiftCode().substring(0, 8) + "XXX";
            swiftCodeRepository.findById(headquarterCode).ifPresent(swiftCodeEntity::setHeadquarterBranch);
        }
        else{
            swiftCodeEntity.setHeadquarter(true);
        }

        swiftCodeRepository.save(swiftCodeEntity);
    }

    @Transactional
    public void deleteSwiftCode(String swiftCode) {
        SwiftCode swiftCodeEntity = swiftCodeRepository.findById(swiftCode)
                .orElseThrow(() -> new RuntimeException("SWIFT code not found"));

        List<SwiftCode> referencingSwiftCodes = swiftCodeRepository.findByHeadquarter(swiftCodeEntity);
        for (SwiftCode referencingSwiftCode : referencingSwiftCodes) {
            referencingSwiftCode.setHeadquarterBranch(null);
            swiftCodeRepository.save(referencingSwiftCode);
        }

        swiftCodeRepository.deleteById(swiftCode);
    }
}