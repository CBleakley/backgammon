package backgammon.input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileInputSource implements InputSource {
    private final BufferedReader reader;

    // Constructor accepts a filename and initializes the reader
    public FileInputSource(String filename) throws IOException {
        reader = new BufferedReader(new FileReader(filename));
    }

    @Override
    public String getInput() {
        try {
            return reader.readLine(); // Read the next line from the file
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null if there's an error
        }
    }

    public void close() {
        try {
            reader.close(); // Close the file when done
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
