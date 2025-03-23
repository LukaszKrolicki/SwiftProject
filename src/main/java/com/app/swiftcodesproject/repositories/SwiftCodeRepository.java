package com.app.swiftcodesproject.repositories;

import com.app.swiftcodesproject.domain.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SwiftCodeRepository extends JpaRepository<SwiftCode, String> {
    List<SwiftCode> findByCountryISO2(String countryISO2);
    long countByIsHeadquarter(boolean isHeadquarter);
    long countByHeadquarterIsNotNull();
    List<SwiftCode> findByHeadquarter(SwiftCode headquarter);
}
