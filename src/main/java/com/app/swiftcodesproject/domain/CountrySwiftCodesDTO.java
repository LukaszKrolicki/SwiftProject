package com.app.swiftcodesproject.domain;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountrySwiftCodesDTO {
    private String countryISO2;
    private String countryName;
    private List<SwiftCodeDTO> swiftCodes;
}