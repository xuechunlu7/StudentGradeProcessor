package reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import exception.DataValidationException;

public abstract class FileReader<T> {
    protected final String filePath;
    protected final List<T> data = new ArrayList<>();

    public FileReader(String filePath) {
        this.filePath = filePath;
    }

    public List<T> getData() {
        return data;
    }

    // Replace existing readFile implementation with this one
    public List<T> readFile() throws DataValidationException, IOException {
        data.clear();
        List<DataValidationException> errors = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                try {
                    T item = parseLine(line, lineNumber++);
                    if (item != null) {
                        data.add(item);
                    }
                } catch (DataValidationException dve) {
                    // Collect validation errors so we can rethrow after reading (or fail fast)
                    errors.add(dve);
                }
            }
        }

        if (!errors.isEmpty()) {
            // Rethrow the first validation error so callers/tests observe failures.
            throw errors.get(0);
        }

        return data;
    }

    protected abstract T parseLine(String line, int lineNumber) throws DataValidationException;
}