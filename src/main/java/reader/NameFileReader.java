package reader;

import model.Student;
import exception.DataValidationException;
import java.util.HashSet;
import java.util.Set;

public class NameFileReader extends FileReader<Student> {
    
    private Set<String> processedStudentIds;

    public NameFileReader(String filePath) throws DataValidationException, java.io.IOException {
        super(filePath);
        this.processedStudentIds = new HashSet<>();
        // readFile(); // Call parent's template method
    }
    
    @Override
    public java.util.List<Student> readFile() throws DataValidationException, java.io.IOException {
        // Clear any existing tracking data before reading to avoid false duplicates on subsequent reads
        if (processedStudentIds != null) {
            processedStudentIds.clear();
        }
        return super.readFile();
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
        
        // Defensive programming: validate ID length
        if (studentId.length() != 9) {
            throw new DataValidationException("Line " + lineNumber + " student ID format error: ID must be exactly 9 digits");
        }
        
        // Defensive programming: validate ID format (only digits)
        if (!studentId.matches("\\d{9}")) {
            throw new DataValidationException("Line " + lineNumber + " student ID format error: " + studentId);
        }
        
        // Defensive programming: validate student name
        if (studentName.isEmpty()) {
            throw new DataValidationException("Line " + lineNumber + " student name cannot be empty");
        }
        
        // Defensive programming: Check for duplicate student IDs
        if (processedStudentIds.contains(studentId)) {
             throw new DataValidationException("Line " + lineNumber + " duplicate student ID error: " + studentId + " already exists in the file.");
        }
        processedStudentIds.add(studentId);
        
        return new Student(studentId, studentName);
    }
    
    public java.util.List<Student> getStudents() {
        return getData();
    }
}
