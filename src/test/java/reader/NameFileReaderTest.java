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
        assertTrue(exception.getMessage().contains("format error: missing comma delimiter"));
        
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

    @Test
    void testParseLineMissingComma() throws IOException {
        Path tempFile = Files.createTempFile("missing_comma", ".txt");
        Files.writeString(tempFile, "123456789John Doe"); // No comma delimiter

        NameFileReader reader = null;
        try {
            reader = new NameFileReader(tempFile.toString());
        } catch (DataValidationException e) {
            fail("Exception thrown in constructor unexpectedly: " + e.getMessage());
        }

        NameFileReader finalReader = reader;
        DataValidationException exception = assertThrows(DataValidationException.class, finalReader::readFile);
        assertTrue(exception.getMessage().contains("missing comma delimiter"));

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testParseLineEmptyStudentName() throws IOException {
        Path tempFile = Files.createTempFile("empty_name", ".txt");
        Files.writeString(tempFile, "123456789,   "); // Commas exist, but name is only spaces

        NameFileReader reader = null;
        try {
            reader = new NameFileReader(tempFile.toString());
        } catch (DataValidationException e) {
            fail("Exception thrown in constructor unexpectedly: " + e.getMessage());
        }

        NameFileReader finalReader = reader;
        DataValidationException exception = assertThrows(DataValidationException.class, finalReader::readFile);
        assertTrue(exception.getMessage().contains("student name cannot be empty"));

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testGetStudentsReturnsData() throws IOException, DataValidationException {
        Path tempFile = Files.createTempFile("get_students", ".txt");
        Files.writeString(tempFile, "123456789,John Doe\n987654321,Jane Smith");

        NameFileReader reader = new NameFileReader(tempFile.toString());
        // Verify the list is initially empty
        assertTrue(reader.getStudents().isEmpty());

        reader.readFile();
        
        // Verify getStudents returns the correctly populated list after reading
        List<Student> students = reader.getStudents();
        assertEquals(2, students.size());
        assertEquals("123456789", students.get(0).getStudentId());

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testParseLineExtraColumnsIgnored() throws IOException, DataValidationException {
        Path tempFile = Files.createTempFile("extra_cols", ".txt");
        // Line has 3 columns, the code only requires at least 2 and uses the first 2
        Files.writeString(tempFile, "123456789,John Doe,ExtraData");

        NameFileReader reader = new NameFileReader(tempFile.toString());
        List<Student> students = reader.readFile();

        assertEquals(1, students.size());
        assertEquals("123456789", students.get(0).getStudentId());
        assertEquals("John Doe", students.get(0).getStudentName());

        Files.deleteIfExists(tempFile);
    }
}