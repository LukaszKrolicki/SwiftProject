package com.app.swiftcodesproject.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwiftCodeDTO {
    @NotBlank(message = "Address is mandatory")
    private String address;
    @NotBlank(message = "Bank name is mandatory")
    private String bankName;
    @NotBlank(message = "Country ISO2 is mandatory")
    private String countryISO2;
    @NotBlank(message = "Country name is mandatory")
    private String countryName;

    @NotBlank(message = "isHeadquarter is mandatory")
    @JsonProperty("IsHeadquarter")
    private boolean isHeadquarter;

    @NotBlank(message = "Swift code is mandatory")
    private String swiftCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SwiftCodeDTO> branches;

}