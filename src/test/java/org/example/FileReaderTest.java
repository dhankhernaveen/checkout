package org.example;

import org.example.util.FileReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class FileReaderTest {


    @Test
    void readFileAsString() {
        String expectedFileContent = "{\"applicationName\": \"checkout-system\"}";

        String actualFileContent = FileReader.readFile("test.json");

        assertEquals(expectedFileContent, actualFileContent);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Read file will throw exception when fileName is not valid")
    void readFilesFails(String fileName) {
        assertThrows(RuntimeException.class, () -> FileReader.readFile(fileName));
    }

    @Test
    void readFileFailsWithFileNotFound() {
        assertThrows(RuntimeException.class,
                () -> FileReader.readFile("missingFile.json"));

    }

    @ParameterizedTest
    @NullAndEmptySource
    void readFileFailsWhenFileNameIsInvalid(String fileName) {
        assertThrows(RuntimeException.class,
                () -> FileReader.readFile(fileName));
    }
}