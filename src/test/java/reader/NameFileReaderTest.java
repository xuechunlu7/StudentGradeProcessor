package reader;

import exception.DataValidationException;
import model.Student;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NameFileReaderTest {

    @Test
    void testParseLineValid() throws IOException, DataValidationException {
        Path tempFile = Files.createTempFile("valid_names", ".txt");
        Files.writeString(tempFile, "123456789,John Doe\n987654321,Jane Smith");

        NameFileReader reader = new NameFileReader(tempFile.toString());
        List<Student> students = reader.readFile();

        assertEquals(2, students.size());
        assertEquals("123456789", students.get(0).getStudentId());
        assertEquals("John Doe", students.get(0).getStudentName());
        assertEquals("987654321", students.get(1).getStudentId());
        assertEquals("Jane Smith", students.get(1).getStudentName());
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testParseLineInvalidFormat() throws IOException {
        Path tempFile = Files.createTempFile("invalid_format", ".txt");
        Files.writeString(tempFile, "123456789"); // Missing comma and name

        NameFileReader reader = null;
        try {
            reader = new NameFileReader(tempFile.toString());
        } catch (DataValidationException e) {
            fail("Exception thrown in constructor unexpectedly: " + e.getMessage());
        }

        NameFileReader finalReader = reader;
        DataValidationException exception = assertThrows(DataValidationException.class, finalReader::readFile);
        assertTrue(exception.getMessage().contains("format error: requires at least 2 columns"));
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testParseLineInvalidStudentId() throws IOException {
        Path tempFile = Files.createTempFile("invalid_id", ".txt");
        Files.writeString(tempFile, "12345678,John Doe"); // ID too short

        NameFileReader reader = null;
        try {
            reader = new NameFileReader(tempFile.toString());
        } catch (DataValidationException e) {
            fail("Exception thrown in constructor unexpectedly: " + e.getMessage());
        }

        NameFileReader finalReader = reader;
        DataValidationException exception = assertThrows(DataValidationException.class, finalReader::readFile);
        assertTrue(exception.getMessage().contains("student ID format error"));
        
        Files.deleteIfExists(tempFile);
    }
}