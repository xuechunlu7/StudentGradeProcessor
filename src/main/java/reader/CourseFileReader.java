package reader;

import model.GradeRecord;
import exception.DataValidationException;

public class CourseFileReader extends FileReader<GradeRecord> {
    
    public CourseFileReader(String filePath) throws DataValidationException, java.io.IOException {
        super(filePath);
        // removed dataList.clear() and readFile() to avoid double-reading
    }

    @Override
    public java.util.List<GradeRecord> readFile() throws DataValidationException, java.io.IOException {
        // Clear any existing data so calling readFile() multiple times
        // does not result in duplicate entries.
        java.util.List<GradeRecord> dataList = getData();
        if (dataList != null) {
            dataList.clear();
        }
        return super.readFile();
    }
    
    @Override
    protected GradeRecord parseLine(String line, int lineNumber) throws DataValidationException {
        // Explicitly check for a comma
        if (!line.contains(",")) {
            throw new DataValidationException("Line " + lineNumber + " format error: missing comma delimiter");
        }

        String[] parts = line.split(",");
        
        // Defensive programming: check column count
        if (parts.length < 6) {
            throw new DataValidationException("Line " + lineNumber + " format error: requires 6 columns (Student ID,Course Code,Test1,Test2,Test3,Final)");
        }
        
        String studentId = parts[0].trim();
        String courseCode = parts[1].trim();
        
        try {
            double test1 = Double.parseDouble(parts[2].trim());
            double test2 = Double.parseDouble(parts[3].trim());
            double test3 = Double.parseDouble(parts[4].trim());
            double finalExam = Double.parseDouble(parts[5].trim());
            
            // Defensive Programming: Validate grade ranges (reject negative and out-of-range values)
            for (double g : new double[]{test1, test2, test3, finalExam}) {
                if (g < 0) {
                    throw new DataValidationException("Line " + lineNumber + " grade value negative: " + g);
                }
                if (g > 100) {
                    throw new DataValidationException("Line " + lineNumber + " grade value out of range (>100): " + g);
                }
            }
            
            return new GradeRecord(studentId, courseCode, test1, test2, test3, finalExam);
            
        } catch (NumberFormatException e) {
            throw new DataValidationException("Line " + lineNumber + " grade format error: " + e.getMessage());
        }
    }
    
    public java.util.List<GradeRecord> getGradeRecords() {
        return getData();
    }
}
