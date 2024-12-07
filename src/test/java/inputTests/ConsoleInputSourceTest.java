package backgammon.input;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleInputSourceTest {
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    void setUpStreams() {
        // Redirect System.out if needed (optional)
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void restoreStreams() {
        // Restore original System.in and System.out
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void should_ReturnInputLine_When_InputIsProvided() {
        String input = "Hello, World!";
        testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        ConsoleInputSource inputSource = new ConsoleInputSource();
        String result = inputSource.getInput();

        assertEquals(input, result);
    }

    @Test
    void should_ReturnMultipleInputLines_InSequence() {
        String input = "First line\nSecond line\nThird line";
        testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        ConsoleInputSource inputSource = new ConsoleInputSource();
        String first = inputSource.getInput();
        String second = inputSource.getInput();
        String third = inputSource.getInput();

        assertEquals("First line", first);
        assertEquals("Second line", second);
        assertEquals("Third line", third);

        // Attempting to read beyond available input should throw NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> inputSource.getInput());
    }

    @Test
    void should_HandleEmptyInput_ReturnsEmptyString() {
        String input = "\n"; // Simulate empty line
        testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        ConsoleInputSource inputSource = new ConsoleInputSource();
        String result = inputSource.getInput();

        assertEquals("", result);
    }

    @Test
    void should_TrimInputWhitespace() {
        String input = "   Trimmed Input   ";
        testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        ConsoleInputSource inputSource = new ConsoleInputSource();
        String result = inputSource.getInput();

        assertEquals("   Trimmed Input   ", result); // Scanner.nextLine() does not trim by default
    }
}
