package processor;

import model.Student;
import model.GradeRecord;
import model.ProcessedResult;
import exception.DataValidationException;

import java.util.*;

public class DataProcessor {
    private Map<String, Student> studentMap;
    private List<GradeRecord> gradeRecords;
    private List<ProcessedResult> processedResults;
    
    public DataProcessor() {
        this.studentMap = new HashMap<>();
        this.gradeRecords = new ArrayList<>();
        this.processedResults = new ArrayList<>();
    }
    
    public void processData(List<Student> students, List<GradeRecord> records) 
            throws DataValidationException {
        
        // Defensive programming: check inputs
        if (students == null || records == null) {
            throw new DataValidationException("Student list and grade records cannot be null");
        }
        
        // Build student Map
        for (Student student : students) {
            studentMap.put(student.getStudentId(), student);
        }
        
        this.gradeRecords = records;
        
        // Associate students with grades
        for (GradeRecord record : records) {
            String studentId = record.getStudentId();
            Student student = studentMap.get(studentId);
            
            if (student == null) {
                // Defensive programming: log warning but continue processing
                System.err.println("Warning: Cannot find student information for ID " + studentId);
                continue;
            }
            
            // Create processed result
            ProcessedResult result = new ProcessedResult(
                studentId,
                student.getStudentName(),
                record.getCourseCode(),
                record.getCalculatedFinalGrade()
            );
            
            processedResults.add(result);
        }
        
        // Sort by student ID using Collections.sort
        Collections.sort(processedResults);
        
        System.out.println("Data processing complete, " + processedResults.size() + " valid records");
    }
    
    // Polymorphism example: different calculation methods
    public double calculateGradeWithDifferentWeight(GradeRecord record, 
                                                    double testWeight, 
                                                    double finalWeight) {
        // Defensive programming: check weights
        if (testWeight < 0 || testWeight > 1 || finalWeight < 0 || finalWeight > 1) {
            throw new IllegalArgumentException("Weights must be between 0 and 1");
        }
        if (Math.abs(testWeight * 3 + finalWeight - 1.0) > 0.001) {
            throw new IllegalArgumentException("Total weight must equal 1");
        }
        
        double weightedTests = (record.getTest1() + record.getTest2() + record.getTest3()) * testWeight;
        double weightedFinal = record.getFinalExam() * finalWeight;
        
        return Math.round((weightedTests + weightedFinal) * 10) / 10.0;
    }
    
    public List<ProcessedResult> getProcessedResults() {
        return new ArrayList<>(processedResults);
    }
    
    public Map<String, Student> getStudentMap() {
        return new HashMap<>(studentMap);
    }
}