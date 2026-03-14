package processor;

import exception.DataValidationException;
import model.GradeRecord;
import model.ProcessedResult;
import model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DataProcessorTest {

    private DataProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new DataProcessor();
    }

    @Test
    void testProcessDataValid() throws DataValidationException {
        List<Student> students = new ArrayList<>();
        students.add(new Student("123456789", "John Doe"));
        students.add(new Student("987654321", "Jane Smith"));

        List<GradeRecord> records = new ArrayList<>();
        records.add(new GradeRecord("123456789", "CS101", 80, 85, 90, 95));
        records.add(new GradeRecord("987654321", "CS102", 70, 75, 80, 85));

        processor.processData(students, records);

        List<ProcessedResult> results = processor.getProcessedResults();
        assertEquals(2, results.size());

        // Verify sorting (123456789 should come before 987654321)
        assertEquals("123456789", results.get(0).getStudentId());
        assertEquals("John Doe", results.get(0).getStudentName());
        assertEquals("CS101", results.get(0).getCourseCode());
        assertEquals(89.0, results.get(0).getFinalGrade(), 0.01);

        assertEquals("987654321", results.get(1).getStudentId());
        assertEquals("Jane Smith", results.get(1).getStudentName());
        assertEquals("CS102", results.get(1).getCourseCode());
        assertEquals(79.0, results.get(1).getFinalGrade(), 0.01);

        Map<String, Student> studentMap = processor.getStudentMap();
        assertEquals(2, studentMap.size());
        assertTrue(studentMap.containsKey("123456789"));
        assertTrue(studentMap.containsKey("987654321"));
    }

    @Test
    void testProcessDataNullInputs() {
        List<Student> students = new ArrayList<>();
        List<GradeRecord> records = new ArrayList<>();

        assertThrows(DataValidationException.class, () -> processor.processData(null, records));
        assertThrows(DataValidationException.class, () -> processor.processData(students, null));
        assertThrows(DataValidationException.class, () -> processor.processData(null, null));
    }

    @Test
    void testProcessDataUnmatchedStudent() throws DataValidationException {
        List<Student> students = new ArrayList<>();
        students.add(new Student("123456789", "John Doe"));

        List<GradeRecord> records = new ArrayList<>();
        records.add(new GradeRecord("123456789", "CS101", 80, 85, 90, 95));
        // Record with no matching student
        records.add(new GradeRecord("111222333", "CS102", 70, 75, 80, 85)); 

        processor.processData(students, records);

        List<ProcessedResult> results = processor.getProcessedResults();
        // Only 1 result should be added because the second one didn't match a student
        assertEquals(1, results.size());
        assertEquals("123456789", results.get(0).getStudentId());
    }

    @Test
    void testProcessDataDuplicateStudentIdAndCourse() throws DataValidationException {
        List<Student> students = new ArrayList<>();
        // Duplicate student IDs in the input list
        students.add(new Student("123456789", "John Doe"));
        students.add(new Student("123456789", "John Doe Duplicate"));

        List<GradeRecord> records = new ArrayList<>();
        // Multiple records for the same student and course
        records.add(new GradeRecord("123456789", "CS101", 80, 85, 90, 95));
        records.add(new GradeRecord("123456789", "CS101", 100, 100, 100, 100));

        processor.processData(students, records);

        List<ProcessedResult> results = processor.getProcessedResults();
        
        // The records list should now deduplicate identical student/course combinations
        // It should only have processed the very first "123456789 - CS101" record it saw.
        assertEquals(1, results.size());
        assertEquals("123456789", results.get(0).getStudentId());
        assertEquals("CS101", results.get(0).getCourseCode());
        // Original final grade logic (80,85,90)*0.2 + 95*0.4 = 89.0. It should NOT be the 100.0 from the skipped dup.
        assertEquals(89.0, results.get(0).getFinalGrade(), 0.01); 
    }

    @Test
    void testCalculateGradeWithDifferentWeightValid() {
        GradeRecord record = new GradeRecord("123456789", "CS101", 80, 80, 80, 100);
        
        // Tests weight: 10% each (30% total), Final weight: 70%
        double finalGrade = processor.calculateGradeWithDifferentWeight(record, 0.1, 0.7);
        
        // (80 + 80 + 80) * 0.1 = 24
        // 100 * 0.7 = 70
        // Total = 94.0
        assertEquals(94.0, finalGrade, 0.01);
    }

    @Test
    void testCalculateGradeWithDifferentWeightInvalidWeights() {
        GradeRecord record = new GradeRecord("123456789", "CS101", 80, 80, 80, 100);

        // Negative weight
        assertThrows(IllegalArgumentException.class, () -> 
            processor.calculateGradeWithDifferentWeight(record, -0.1, 0.7));
            
        // Weight > 1
        assertThrows(IllegalArgumentException.class, () -> 
            processor.calculateGradeWithDifferentWeight(record, 0.2, 1.5));

        // Weights don't add up to 1 (e.g. 3*0.2 + 0.5 = 1.1)
        assertThrows(IllegalArgumentException.class, () -> 
            processor.calculateGradeWithDifferentWeight(record, 0.2, 0.5));
    }
}
