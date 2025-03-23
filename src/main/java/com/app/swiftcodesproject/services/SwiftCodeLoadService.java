package com.app.swiftcodesproject.services;

import com.app.swiftcodesproject.domain.SwiftCode;
import com.app.swiftcodesproject.repositories.SwiftCodeRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SwiftCodeLoadService {

    private final SwiftCodeRepository swiftCodeRepository;

    @Value("${csv.file.path}")
    private String csvFilePath;

    public SwiftCodeLoadService(SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
    }

    @Transactional
    public void loadSwiftCodesFromCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            List<String[]> records = reader.readAll();
            if (!records.isEmpty()) {
                records.remove(0); // Remove the header row
            }

            Map<String, SwiftCode> headquartersMap = new HashMap<>();
            processHeadquarters(records, headquartersMap);
            processBranches(records, headquartersMap);

            System.out.println("Wczytano dane SWIFT z CSV do bazy!");
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private void processHeadquarters(List<String[]> records, Map<String, SwiftCode> headquartersMap) {
        for (String[] row : records) {
            if (row.length < 8) continue;

            String swiftCode = row[1].trim();
            if (isHeadquarter(swiftCode)) {
                SwiftCode swiftCodeEntity = createSwiftCodeEntity(row, true, null);
                if (!headquartersMap.containsKey(swiftCode) && !swiftCodeRepository.existsById(swiftCode)) {
                    headquartersMap.put(swiftCode, swiftCodeEntity);
                    swiftCodeRepository.save(swiftCodeEntity);
                }
            }
        }
    }

    private void processBranches(List<String[]> records, Map<String, SwiftCode> headquartersMap) {
        for (String[] row : records) {
            if (row.length < 8) continue;

            String swiftCode = row[1].trim();
            if (!isHeadquarter(swiftCode)) {
                String headquarterCode = swiftCode.substring(0, 8) + "XXX";
                SwiftCode headquarter = headquartersMap.get(headquarterCode);

                if (headquarter == null) {
                    headquarter = swiftCodeRepository.findById(headquarterCode).orElse(null);
                }

                SwiftCode swiftCodeEntity = createSwiftCodeEntity(row, false, headquarter);
                if (!swiftCodeRepository.existsById(swiftCode)) {
                    swiftCodeRepository.save(swiftCodeEntity);
                }
            }
        }
    }

    private boolean isHeadquarter(String swiftCode) {
        return swiftCode.endsWith("XXX");
    }

    private SwiftCode createSwiftCodeEntity(String[] row, boolean isHeadquarter, SwiftCode headquarter) {
        String countryISO2 = row[0].trim().toUpperCase();
        String swiftCode = row[1].trim();
        String bankName = row[3].trim();
        String address = row[4].trim();
        String countryName = row[6].trim().toUpperCase();

        return new SwiftCode(swiftCode, bankName, address, countryISO2, countryName, isHeadquarter, headquarter);
    }
}