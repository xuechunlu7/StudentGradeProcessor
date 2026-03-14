package reader;

import model.Student;
import exception.DataValidationException;

public class NameFileReader extends FileReader<Student> {
    
    public NameFileReader(String filePath) throws DataValidationException, java.io.IOException {
        super(filePath);
        // readFile(); // Call parent's template method
    }
    
    @Override
    protected Student parseLine(String line, int lineNumber) throws DataValidationException {
        // Explicitly check for a comma
        if (!line.contains(",")) {
            throw new DataValidationException("Line " + lineNumber + " format error: missing comma delimiter");
        }

        // Split by comma
        String[] parts = line.split(",");
        
        // Defensive programming: check column count
        if (parts.length < 2) {
            throw new DataValidationException("Line " + lineNumber + " format error: requires at least 2 columns");
        }
        
        String studentId = parts[0].trim();
        String studentName = parts[1].trim();
        
        // Defensive programming: validate ID format
        if (!studentId.matches("\\d{9}")) {
            throw new DataValidationException("Line " + lineNumber + " student ID format error: " + studentId);
        }
        
        // Defensive programming: validate student name
        if (studentName.isEmpty()) {
            throw new DataValidationException("Line " + lineNumber + " student name cannot be empty");
        }
        
        return new Student(studentId, studentName);
    }
    
    public java.util.List<Student> getStudents() {
        return getData();
    }
}