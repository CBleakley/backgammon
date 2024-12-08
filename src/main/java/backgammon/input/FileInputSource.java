/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * An implementation of the {@code InputSource} interface that reads input from a file.
 * Provides methods to retrieve input line-by-line and close the file when done.
 */
public class FileInputSource implements InputSource {
    private final BufferedReader reader;

    /**
     * Constructs a new {@code FileInputSource} using the specified file.
     *
     * @param filename the name of the file to read input from
     * @throws IOException if an error occurs while opening the file
     */
    public FileInputSource(String filename) throws IOException {
        reader = new BufferedReader(new FileReader(filename));
    }

    /**
     * Retrieves the next line of input from the file.
     *
     * @return the next line as a {@code String}, or {@code null} if the end of the file is reached or an error occurs
     */
    @Override
    public String getInput() {
        try {
            return reader.readLine(); // Read the next line from the file
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null if there's an error
        }
    }

    /**
     * Closes the file when it is no longer needed.
     * Ensures that resources are released properly.
     */
    public void close() {
        try {
            reader.close(); // Close the file when done
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
