package backgammon.input;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileInputSourceTest {
    private Path tempFile;
    private FileInputSource inputSource;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary file
        tempFile = Files.createTempFile("testInput", ".txt");
        // Write some lines to it
        BufferedWriter writer = Files.newBufferedWriter(tempFile);
        writer.write("Line 1");
        writer.newLine();
        writer.write("Line 2");
        writer.newLine();
        writer.write("Line 3");
        writer.newLine();
        writer.close();

        // Initialize FileInputSource with the temp file
        inputSource = new FileInputSource(tempFile.toString());
    }

    @AfterEach
    void tearDown() {
        // Close the input source
        inputSource.close();
        // Delete the temporary file
        try {
            Files.deleteIfExists(tempFile);
        } catch (IOException e) {
            // Ignore
        }
    }

    @Test
    void should_ReturnLinesInOrder_When_GetInputCalledMultipleTimes() {
        assertEquals("Line 1", inputSource.getInput());
        assertEquals("Line 2", inputSource.getInput());
        assertEquals("Line 3", inputSource.getInput());
        assertNull(inputSource.getInput()); // After all lines, should return null
    }

    @Test
    void should_ReturnNull_When_FileIsEmpty() throws IOException {
        // Create an empty temporary file
        Files.write(tempFile, new byte[0]);

        // Re-initialize FileInputSource
        inputSource.close();
        inputSource = new FileInputSource(tempFile.toString());

        assertNull(inputSource.getInput());
    }

    @Test
    void should_HandleIOException_Correctly() throws IOException {
        // Close the reader to cause an IOException
        inputSource.close();

        // Calling getInput() after close should return null (as per implementation)
        String input = inputSource.getInput();
        assertNull(input);
    }

    @Test
    void should_ReadInputCorrectly_AfterSomeLines() {
        // Read first two lines
        assertEquals("Line 1", inputSource.getInput());
        assertEquals("Line 2", inputSource.getInput());

        // Close the input source
        inputSource.close();

        // Attempt to read third line should return null
        assertNull(inputSource.getInput());
    }
}
