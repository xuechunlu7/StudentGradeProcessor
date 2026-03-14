import processor.DataProcessor;
import reader.NameFileReader;
import reader.CourseFileReader;
import writer.OutputFileWriter;
import exception.DataValidationException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        System.out.println("====== Student Grade Processing System Started ======");
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        try {
            // Try multiple possible file locations
            String[] possiblePaths = {
                "src/input",
                "input"
            };
            
            String nameFilePath = findFile(possiblePaths, "NameFile.txt");
            String courseFilePath = findFile(possiblePaths, "CourseFile.txt");
            String outputFilePath = "src/output/OutputFile.txt";
            
            // Create output directory if it doesn't exist
            new File("src/output").mkdirs();

            System.out.println("Using name file: " + nameFilePath);
            System.out.println("Using course file: " + courseFilePath);

            // Step 1: Read files
            System.out.println("Reading student information file...");
            NameFileReader nameReader = new NameFileReader(nameFilePath);
            nameReader.readFile();

            System.out.println("Reading course grade file...");
            CourseFileReader courseReader = new CourseFileReader(courseFilePath);
            courseReader.readFile();

            // Step 2: Process data
            System.out.println("Processing data...");
            DataProcessor processor = new DataProcessor();
            processor.processData(nameReader.getStudents(), courseReader.getGradeRecords());

            // Step 3: Output results
            System.out.println("Generating output file: " + outputFilePath);
            OutputFileWriter writer = new OutputFileWriter(outputFilePath);
            writer.writeOutput(processor.getProcessedResults());

            System.out.println("====== Processing Complete! ======");
            System.out.println("Total " + processor.getProcessedResults().size() + " records processed");

        } catch (DataValidationException e) {
            System.err.println("Data validation error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("System error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String findFile(String[] possiblePaths, String filename) throws Exception {
        for (String path : possiblePaths) {
            File file = new File(path, filename);
            if (file.exists()) {
                return file.getAbsolutePath();
            }
        }
        
        // If file not found, show where we looked
        System.err.println("Could not find " + filename + " in any of these locations:");
        for (String path : possiblePaths) {
            System.err.println("  - " + new File(path, filename).getAbsolutePath());
        }
        System.err.println("\nPlease create the file or update the path.");
        
        throw new Exception("File not found: " + filename);
    }
}