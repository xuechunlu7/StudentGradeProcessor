package reader;

import exception.DataValidationException;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FileReaderTest {

    // Concrete implementation for testing
    class TestFileReader extends FileReader<String> {
        public TestFileReader(String filePath) {
            super(filePath);
        }

        @Override
        protected String parseLine(String line, int lineNumber) throws DataValidationException {
            if (line.contains("invalid")) {
                throw new DataValidationException("Invalid data in line " + lineNumber);
            }
            return line;
        }
    }

    @Test
    void testReadFileSuccessfully() throws IOException, DataValidationException {
        String filePath = "src/test/resources/test_file.txt";
        FileReader<String> reader = new TestFileReader(filePath);
        List<String> data = reader.readFile();
        assertEquals(3, data.size());
        assertEquals("line 1", data.get(0));
        assertEquals("line 2", data.get(1));
        assertEquals("line 3", data.get(2));
    }

    @Test
    void testReadFileWithInvalidData() throws IOException {
        String filePath = "src/test/resources/invalid_test_file.txt";
        // Create a file with invalid data for testing
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(filePath), "line 1\ninvalid line\nline 3".getBytes());
        } catch (IOException e) {
            fail("Failed to create test file");
        }

        FileReader<String> reader = new TestFileReader(filePath);
        assertThrows(DataValidationException.class, reader::readFile);
    }

    @Test
    void testFileNotFound() {
        FileReader<String> reader = new TestFileReader("non_existent_file.txt");
        assertThrows(IOException.class, reader::readFile);
    }
}