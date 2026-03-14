package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProcessedResultTest {

    @Test
    void testConstructorAndGetters() {
        ProcessedResult result = new ProcessedResult("123456789", "John Doe", "CS101", 89.0);
        assertEquals("123456789", result.getStudentId());
        assertEquals("John Doe", result.getStudentName());
        assertEquals("CS101", result.getCourseCode());
        assertEquals(89.0, result.getFinalGrade(), 0.01);
    }

    @Test
    void testToString() {
        ProcessedResult result = new ProcessedResult("123456789", "John Doe", "CS101", 89.0);
        assertEquals("123456789,John Doe,CS101,89.0", result.toString());
    }

    @Test
    void testCompareTo() {
        ProcessedResult result1 = new ProcessedResult("123456789", "John Doe", "CS101", 89.0);
        ProcessedResult result2 = new ProcessedResult("987654321", "Jane Smith", "CS101", 92.0);
        ProcessedResult result3 = new ProcessedResult("123456789", "John Doe", "CS101", 89.0);

        assertTrue(result1.compareTo(result2) < 0);
        assertTrue(result2.compareTo(result1) > 0);
        assertEquals(0, result1.compareTo(result3));
    }
}