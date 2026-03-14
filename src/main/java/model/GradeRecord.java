package model;

public class GradeRecord extends Course {
    private String studentId;
    private double test1;
    private double test2;
    private double test3;
    private double finalExam;
    private double calculatedFinalGrade;
    
    public GradeRecord(String studentId, String courseCode, 
                       double test1, double test2, double test3, double finalExam) {
        super(courseCode);
        
        // Defensive programming: validate all inputs
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty");
        }
        
        // Validate grade ranges (0-100)
        validateGrade(test1, "Test 1");
        validateGrade(test2, "Test 2");
        validateGrade(test3, "Test 3");
        validateGrade(finalExam, "Final Exam");
        
        this.studentId = studentId.trim();
        this.test1 = test1;
        this.test2 = test2;
        this.test3 = test3;
        this.finalExam = finalExam;
        
        // Calculate final grade
        this.calculatedFinalGrade = calculateFinalGrade(test1, test2, test3, finalExam);
    }
    
    // Defensive programming: grade validation method
    private void validateGrade(double grade, String gradeName) {
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException(gradeName + " grade must be between 0-100: " + grade);
        }
    }
    
    @Override
    public String getCourseType() {
        return "Regular Course";
    }
    
    @Override
    public double calculateFinalGrade(double test1, double test2, double test3, double finalExam) {
        // Calculation: each test 20%, final exam 40%
        double weightedTests = (test1 + test2 + test3) * 0.2;
        double weightedFinal = finalExam * 0.4;
        double finalGrade = weightedTests + weightedFinal;
        
        // Keep one decimal place
        return Math.round(finalGrade * 10) / 10.0;
    }
    
    // Getter methods
    public String getStudentId() { return studentId; }
    public double getTest1() { return test1; }
    public double getTest2() { return test2; }
    public double getTest3() { return test3; }
    public double getFinalExam() { return finalExam; }
    public double getCalculatedFinalGrade() { return calculatedFinalGrade; }
    
    @Override
    public String toString() {
        return String.format("%s,%s,%.1f,%.1f,%.1f,%.1f,%.1f", 
            studentId, courseCode, test1, test2, test3, finalExam, calculatedFinalGrade);
    }
}