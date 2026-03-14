package writer;

import model.ProcessedResult;
import exception.DataValidationException;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class OutputFileWriter {
    private String outputFilePath;
    
    public OutputFileWriter(String outputFilePath) {
        if (outputFilePath == null || outputFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Output file path cannot be empty");
        }
        this.outputFilePath = outputFilePath;
    }
    
    public void writeOutput(List<ProcessedResult> results) 
            throws DataValidationException, IOException {
        
        // Defensive programming: check results
        if (results == null || results.isEmpty()) {
            throw new DataValidationException("No results to output");
        }
        
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFilePath), StandardCharsets.UTF_8))) {
            
            // Write header
            writer.write("Student ID,Student Name,Course Code,Final grade (test 1,2,3-3x20%, final exam 40%)");
            writer.newLine();
            
            // Write data rows
            for (ProcessedResult result : results) {
                String line = String.format("%s,%s,%s,%.1f",
                    result.getStudentId(),
                    result.getStudentName(),
                    result.getCourseCode(),
                    result.getFinalGrade()
                );
                
                writer.write(line);
                writer.newLine();
            }
            
            System.out.println("Output file written successfully: " + outputFilePath);
            
        } catch (IOException e) {
            throw new IOException("Cannot write output file: " + outputFilePath, e);
        }
    }
}