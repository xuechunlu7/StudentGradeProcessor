package model;

public abstract class Course {
    protected String courseCode;
    
    public Course(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be empty");
        }
        this.courseCode = courseCode.trim();
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    // Abstract methods: subclasses must implement
    public abstract String getCourseType();
    
    public abstract double calculateFinalGrade(double test1, double test2, double test3, double finalExam);
}