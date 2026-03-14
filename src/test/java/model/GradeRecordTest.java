package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GradeRecordTest {

    @Test
    void testConstructorAndGetters() {
        GradeRecord record = new GradeRecord("123456789", "CS101", 80, 85, 90, 95);
        assertEquals("123456789", record.getStudentId());
        assertEquals("CS101", record.getCourseCode());
        assertEquals(80, record.getTest1());
        assertEquals(85, record.getTest2());
        assertEquals(90, record.getTest3());
        assertEquals(95, record.getFinalExam());
    }

    @Test
    void testCalculateFinalGrade() {
        GradeRecord record = new GradeRecord("123456789", "CS101", 80, 85, 90, 95);
        assertEquals(89.0, record.getCalculatedFinalGrade(), 0.01);
    }

    @Test
    void testToString() {
        GradeRecord record = new GradeRecord("123456789", "CS101", 80, 85, 90, 95);
        assertEquals("123456789,CS101,80.0,85.0,90.0,95.0,89.0", record.toString());
    }

    @Test
    void testEmptyStudentId() {
        assertThrows(IllegalArgumentException.class, () -> new GradeRecord("", "CS101", 80, 85, 90, 95));
    }

    @Test
    void testNullStudentId() {
        assertThrows(IllegalArgumentException.class, () -> new GradeRecord(null, "CS101", 80, 85, 90, 95));
    }

    @Test
    void testInvalidGradeRange() {
        assertThrows(IllegalArgumentException.class, () -> new GradeRecord("123456789", "CS101", 110, 85, 90, 95));
        assertThrows(IllegalArgumentException.class, () -> new GradeRecord("123456789", "CS101", -10, 85, 90, 95));
    }
}