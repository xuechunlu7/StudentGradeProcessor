package reader;

import exception.DataValidationException;
import model.GradeRecord;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseFileReaderTest {

    @Test
    void testParseLineValid() throws IOException, DataValidationException {
        Path tempFile = Files.createTempFile("valid_grades", ".txt");
        Files.writeString(tempFile, "123456789,CS101,80,85,90,95");

        CourseFileReader reader = new CourseFileReader(tempFile.toString());
        List<GradeRecord> records = reader.readFile();

        assertEquals(1, records.size());
        GradeRecord record = records.get(0);
        assertEquals("123456789", record.getStudentId());
        assertEquals("CS101", record.getCourseCode());
        assertEquals(80, record.getTest1());
        assertEquals(85, record.getTest2());
        assertEquals(90, record.getTest3());
        assertEquals(95, record.getFinalExam());
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testParseLineInvalidFormat() throws IOException {
        Path tempFile = Files.createTempFile("invalid_format", ".txt");
        Files.writeString(tempFile, "123456789,CS101,80,85,90"); // Missing final exam

        CourseFileReader reader = null;
        try {
            reader = new CourseFileReader(tempFile.toString());
        } catch (DataValidationException e) {
            fail("Exception thrown in constructor unexpectedly: " + e.getMessage());
        }

        CourseFileReader finalReader = reader;
        DataValidationException exception = assertThrows(DataValidationException.class, finalReader::readFile);
        assertTrue(exception.getMessage().contains("format error: requires 6 columns"));
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testParseLineInvalidNumber() throws IOException {
        Path tempFile = Files.createTempFile("invalid_number", ".txt");
        Files.writeString(tempFile, "123456789,CS101,80,eighty-five,90,95");

        CourseFileReader reader = null;
        try {
            reader = new CourseFileReader(tempFile.toString());
        } catch (DataValidationException e) {
            fail("Exception thrown in constructor unexpectedly: " + e.getMessage());
        }

        CourseFileReader finalReader = reader;
        DataValidationException exception = assertThrows(DataValidationException.class, finalReader::readFile);
        assertTrue(exception.getMessage().contains("grade format error"));
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testParseLineGradeOutOfRange() throws IOException {
        Path tempFile = Files.createTempFile("out_of_range", ".txt");
        Files.writeString(tempFile, "123456789,CS101,80,105,90,95");

        CourseFileReader reader = null;
        try {
            reader = new CourseFileReader(tempFile.toString());
        } catch (DataValidationException e) {
            fail("Exception thrown in constructor unexpectedly: " + e.getMessage());
        }

        CourseFileReader finalReader = reader;
        DataValidationException exception = assertThrows(DataValidationException.class, finalReader::readFile);
        assertTrue(exception.getMessage().contains("grade value out of range"));
        
        Files.deleteIfExists(tempFile);
    }
}