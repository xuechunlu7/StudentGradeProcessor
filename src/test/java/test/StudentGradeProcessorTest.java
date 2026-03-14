package test;

import exception.DataValidationException;
import model.GradeRecord;
import model.ProcessedResult;
import model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import processor.DataProcessor;
import reader.CourseFileReader;
import reader.NameFileReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentGradeProcessorTest {

    @Test
    public void testStudentValidation() {
        // Test valid student creation
        assertDoesNotThrow(() -> new Student("123456789", "John Doe"));

        // Test invalid IDs (Length != 9)
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> new Student("123", "Short ID"));
        assertTrue(exception1.getMessage().contains("9 digits"));

        // Test invalid IDs (Non-numeric)
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> new Student("12345678a", "Alpha ID"));
        assertTrue(exception2.getMessage().contains("only digits"));

        // Test null/empty values
        assertThrows(IllegalArgumentException.class, () -> new Student(null, "Null ID"));
        assertThrows(IllegalArgumentException.class, () -> new Student("123456789", ""));
    }

    @Test
    public void testDataProcessorLogic() throws DataValidationException {
        // 1. Setup Mock Data
        List<Student> students = new ArrayList<>();
        students.add(new Student("100000001", "Alice"));
        students.add(new Student("100000002", "Bob"));

        List<GradeRecord> records = new ArrayList<>();
        // Constructor inferred from CourseFileReader: ID, Course, T1, T2, T3, Final
        records.add(new GradeRecord("100000001", "CSC101", 80, 80, 80, 80));
        records.add(new GradeRecord("100000002", "CSC101", 50, 60, 70, 90));

        // 2. Run Processor
        DataProcessor processor = new DataProcessor();
        processor.processData(students, records);
        List<ProcessedResult> results = processor.getProcessedResults();

        // 3. Verify Results
        assertEquals(2, results.size(), "Should process 2 valid records");

        // Verify Alice's Grade (80 across the board should result in 80)
        ProcessedResult aliceResult = results.stream()
                .filter(r -> r.getStudentId().equals("100000001"))
                .findFirst().orElseThrow(() -> new RuntimeException("Student not found"));
        assertEquals("Alice", aliceResult.getStudentName());
        assertEquals(80.0, aliceResult.getFinalGrade(), 0.01);
    }

    @Test
    public void testNameFileReader(@TempDir Path tempDir) throws IOException, DataValidationException {
        // 1. Create a temporary file
        File tempFile = tempDir.resolve("NameFile.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("100000001,Alice\n");
            writer.write("100000002,Bob\n");
        }

        // 2. Read file
        NameFileReader reader = new NameFileReader(tempFile.getAbsolutePath());
        reader.readFile();
        List<Student> students = reader.getStudents();

        // 3. Verify parsing
        assertEquals(2, students.size());
        assertEquals("Alice", students.get(0).getStudentName());
    }

    @Test
    public void testNameFileReaderInvalidFormat(@TempDir Path tempDir) throws IOException {
        // 1. Create a temporary file with invalid data
        File tempFile = tempDir.resolve("NameFile_Invalid.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("invalid_line_no_comma\n");
        }

        // 2. Expect DataValidationException
        assertThrows(DataValidationException.class, () -> {
            NameFileReader reader = new NameFileReader(tempFile.getAbsolutePath());
            reader.readFile();
        });
    }

    @Test
    public void testCourseFileReader(@TempDir Path tempDir) throws IOException, DataValidationException {
        // 1. Create a temporary file
        File tempFile = tempDir.resolve("CourseFile.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            // Format: ID, Course, T1, T2, T3, Final
            writer.write("100000001,CSC101,80,85,90,95\n");
        }

        // 2. Read file
        CourseFileReader reader = new CourseFileReader(tempFile.getAbsolutePath());
        reader.readFile();
        List<GradeRecord> records = reader.getGradeRecords();

        // 3. Verify parsing
        assertEquals(1, records.size());
        assertEquals("CSC101", records.get(0).getCourseCode());
    }

    @Test
    public void testCourseFileReaderNegativeGrades(@TempDir Path tempDir) throws IOException {
        // 1. Create a temporary file containing a negative grade (should be rejected)
        File tempFile = tempDir.resolve("CourseFile_Negative.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            // negative test1 value
            writer.write("100000001,CSC101,-5,85,90,95\n");
        }

        // 2. Expect DataValidationException during parsing/reading
        assertThrows(DataValidationException.class, () -> {
            CourseFileReader reader = new CourseFileReader(tempFile.getAbsolutePath());
            reader.readFile();
        });
    }
}

