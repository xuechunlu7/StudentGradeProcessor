package model;

public class ProcessedResult implements Comparable<ProcessedResult> {
    private String studentId;
    private String studentName;
    private String courseCode;
    private double finalGrade;
    
    public ProcessedResult(String studentId, String studentName, String courseCode, double finalGrade) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseCode = courseCode;
        this.finalGrade = finalGrade;
    }
    
    // Getter methods
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getCourseCode() { return courseCode; }
    public double getFinalGrade() { return finalGrade; }
    
    // Implement Comparable interface for sorting by student ID
    @Override
    public int compareTo(ProcessedResult other) {
        return this.studentId.compareTo(other.studentId);
    }
    
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%.1f", studentId, studentName, courseCode, finalGrade);
    }
}