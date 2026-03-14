package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void testConstructorAndGetters() {
        Student student = new Student("123456789", "John Doe");
        assertEquals("123456789", student.getStudentId());
        assertEquals("John Doe", student.getStudentName());
    }

    @Test
    void testToString() {
        Student student = new Student("123456789", "John Doe");
        assertEquals("123456789,John Doe", student.toString());
    }

    @Test
    void testEmptyStudentId() {
        assertThrows(IllegalArgumentException.class, () -> new Student("", "John Doe"));
    }

    @Test
    void testNullStudentId() {
        assertThrows(IllegalArgumentException.class, () -> new Student(null, "John Doe"));
    }

    @Test
    void testEmptyStudentName() {
        assertThrows(IllegalArgumentException.class, () -> new Student("123456789", ""));
    }

    @Test
    void testNullStudentName() {
        assertThrows(IllegalArgumentException.class, () -> new Student("123456789", null));
    }

    @Test
    void testInvalidStudentIdLength() {
        assertThrows(IllegalArgumentException.class, () -> new Student("123", "John Doe"));
    }

    @Test
    void testNonDigitStudentId() {
        assertThrows(IllegalArgumentException.class, () -> new Student("12345678a", "John Doe"));
    }
}