package com.example.rentalSystem.global.initializer;

import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvReader {

    public static List<String> readCSV(String filePath) {
        try {
            return Files.readAllLines(Path.of(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CustomException(ErrorType.FAIL_READ_CSV);
        }
    }
}
