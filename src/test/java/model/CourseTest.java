package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    // Concrete implementation of Course for testing
    class ConcreteCourse extends Course {
        public ConcreteCourse(String courseCode) {
            super(courseCode);
        }

        @Override
        public String getCourseType() {
            return "Test";
        }

        @Override
        public double calculateFinalGrade(double test1, double test2, double test3, double finalExam) {
            return (test1 + test2 + test3 + finalExam) / 4;
        }
    }

    @Test
    void testConstructorAndGetCourseCode() {
        Course course = new ConcreteCourse("CS101");
        assertEquals("CS101", course.getCourseCode());
    }

    @Test
    void testEmptyCourseCode() {
        assertThrows(IllegalArgumentException.class, () -> new ConcreteCourse(""));
    }

    @Test
    void testNullCourseCode() {
        assertThrows(IllegalArgumentException.class, () -> new ConcreteCourse(null));
    }
}