package com.app.swiftcodesproject.config;

import com.app.swiftcodesproject.repositories.SwiftCodeRepository;
import com.app.swiftcodesproject.services.SwiftCodeLoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final SwiftCodeLoadService swiftCodeLoadService;
    private final SwiftCodeRepository swiftCodeRepository;

    public DataLoader(SwiftCodeLoadService swiftCodeLoadService, SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeLoadService = swiftCodeLoadService;
        this.swiftCodeRepository = swiftCodeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (swiftCodeRepository.count() == 0) {
            logger.info("Ładowanie danych SWIFT z CSV...");
            swiftCodeLoadService.loadSwiftCodesFromCSV();
            runTests();
        } else {
            logger.info("Dane SWIFT są już w bazie, pomijam ładowanie.");
        }
    }

    private void runTests() {
        long totalRows = swiftCodeRepository.count();
        long numberOfHeadquarters = swiftCodeRepository.countByIsHeadquarter(true);
        long numberOfBranches = swiftCodeRepository.countByIsHeadquarter(false);
        long numberOfAssociations = swiftCodeRepository.countByHeadquarterIsNotNull();

        assert totalRows == 1061 : "Total rows count mismatch";
        assert numberOfHeadquarters == 696 : "Number of headquarters count mismatch";
        assert numberOfBranches == 365 : "Number of branches count mismatch";
        assert numberOfAssociations == 328 : "Number of associations count mismatch";

        logger.info("All tests passed!");
    }
}