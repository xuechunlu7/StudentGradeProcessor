package writer;

import exception.DataValidationException;
import model.ProcessedResult;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OutputFileWriterTest {

    @Test
    void testWriteOutputValid() throws IOException, DataValidationException {
        Path tempFile = Files.createTempFile("output", ".txt");
        
        List<ProcessedResult> results = new ArrayList<>();
        results.add(new ProcessedResult("123456789", "John Doe", "CS101", 89.0));
        results.add(new ProcessedResult("987654321", "Jane Smith", "CS101", 92.0));

        OutputFileWriter writer = new OutputFileWriter(tempFile.toString());
        writer.writeOutput(results);

        List<String> lines = Files.readAllLines(tempFile);
        assertEquals(3, lines.size());
        assertEquals("Student ID,Student Name,Course Code,Final grade (test 1,2,3-3x20%, final exam 40%)", lines.get(0));
        assertEquals("123456789,John Doe,CS101,89.0", lines.get(1));
        assertEquals("987654321,Jane Smith,CS101,92.0", lines.get(2));
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testConstructorEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> new OutputFileWriter(""));
        assertThrows(IllegalArgumentException.class, () -> new OutputFileWriter(null));
    }

    @Test
    void testWriteOutputEmptyResults() {
        OutputFileWriter writer = new OutputFileWriter("dummy_path.txt");
        List<ProcessedResult> emptyResults = new ArrayList<>();
        assertThrows(DataValidationException.class, () -> writer.writeOutput(emptyResults));
        assertThrows(DataValidationException.class, () -> writer.writeOutput(null));
    }
}