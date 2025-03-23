package com.app.swiftcodesproject.controllers;

import com.app.swiftcodesproject.domain.CountrySwiftCodesDTO;
import com.app.swiftcodesproject.domain.MessageResponse;
import com.app.swiftcodesproject.domain.SwiftCodeDTO;
import com.app.swiftcodesproject.services.SwiftCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftCodesController {

    private final SwiftCodeService swiftCodeService;

    public SwiftCodesController(SwiftCodeService swiftCodeService) {
        this.swiftCodeService = swiftCodeService;
    }

    @GetMapping("/{swiftCode}" )
    public ResponseEntity<?> getSwiftCodeDetails(@PathVariable("swiftCode") String swiftCode) {
        System.out.println("swiftCode = " + swiftCode);
        return ResponseEntity.ok(swiftCodeService.getSwiftCodeDetails(swiftCode));
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<CountrySwiftCodesDTO> getSwiftCodesByCountry(@PathVariable("countryISO2code") String countryISO2code) {
        CountrySwiftCodesDTO countrySwiftCodes = swiftCodeService.getSwiftCodesByCountry(countryISO2code);
        return ResponseEntity.ok(countrySwiftCodes);
    }

    @PostMapping
    public ResponseEntity<?> addSwiftCode(@RequestBody SwiftCodeDTO swiftCodeDTO) {
        swiftCodeService.addSwiftCode(swiftCodeDTO);
        return ResponseEntity.ok(new MessageResponse("SWIFT code added successfully"));
    }

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<MessageResponse> deleteSwiftCode(@PathVariable("swiftCode") String swiftCode) {
        swiftCodeService.deleteSwiftCode(swiftCode);
        return ResponseEntity.ok(new MessageResponse("SWIFT code deleted successfully"));
    }
}
