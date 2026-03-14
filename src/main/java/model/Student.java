package model;

public class Student {
    // Encapsulation: private fields
    private String studentId;
    private String studentName;
    
    // Constructor
    public Student(String studentId, String studentName) {
        // Defensive programming: input validation
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty");
        }
        if (studentName == null || studentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        if (studentId.length() != 9) {
            throw new IllegalArgumentException("Student ID must be 9 digits");
        }
        if (!studentId.matches("\\d{9}")) {
            throw new IllegalArgumentException("Student ID must contain only digits");
        }
        
        this.studentId = studentId.trim();
        this.studentName = studentName.trim();
    }
    
    // Public access methods
    public String getStudentId() {
        return studentId;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    @Override
    public String toString() {
        return studentId + "," + studentName;
    }
}