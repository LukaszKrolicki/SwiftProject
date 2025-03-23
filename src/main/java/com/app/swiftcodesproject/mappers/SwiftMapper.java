package com.app.swiftcodesproject.mappers;

import com.app.swiftcodesproject.domain.SwiftCode;
import com.app.swiftcodesproject.domain.SwiftCodeDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SwiftMapper implements Mapper<SwiftCode, SwiftCodeDTO> {

    private final ModelMapper mapper;

    public SwiftMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public SwiftCodeDTO mapToDto(SwiftCode swiftCode) {
        return mapper.map(swiftCode, SwiftCodeDTO.class);
    }

    @Override
    public SwiftCode mapToEntity(SwiftCodeDTO swiftCodeDTO) {
        return mapper.map(swiftCodeDTO, SwiftCode.class);

    }
}