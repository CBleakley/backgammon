package backgammon.main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    void setUpStreams() {
        // Redirect System.out to capture outputs
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
    void should_DisplayWelcomeMessage_OnStart() {
        // Simulate user inputs: player1 name, player2 name, win threshold
        // Additionally, simulate a "quit" command if the game expects it to terminate gracefully
        // Adjust the number of inputs based on your application's flow
        String simulatedInput = "Alice\nBob\n1\n"; // player1, player2, win threshold
        testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(testIn);

        // Run the main method and expect NoSuchElementException due to lack of further inputs
        Exception exception = assertThrows(NoSuchElementException.class, () -> App.main(new String[] {}));

        // Capture the output
        String output = testOut.toString();

        // Verify that the welcome message was displayed before the exception
        assertTrue(output.contains("Welcome to Backgammon"), "Welcome message should be displayed");
    }
}
