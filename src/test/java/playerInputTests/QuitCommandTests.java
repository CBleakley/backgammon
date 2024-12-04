package playerInputTests;

import backgammon.playerInput.QuitCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuitCommandTests {

    @Test
    void testParse_ValidQuitInput() {
        // Test with valid "quit" input (case insensitive)
        QuitCommand command = QuitCommand.parse("quit");
        assertNotNull(command, "Expected QuitCommand object for input 'quit'");

        command = QuitCommand.parse("QUIT");
        assertNotNull(command, "Expected QuitCommand object for input 'QUIT'");

        command = QuitCommand.parse("QuIt");
        assertNotNull(command, "Expected QuitCommand object for input 'QuIt'");
    }

    @Test
    void testParse_InvalidInput() {
        // Test with invalid input that is not "quit"
        QuitCommand command = QuitCommand.parse("exit");
        assertNull(command, "Expected null for input 'exit'");

        command = QuitCommand.parse("quitting");
        assertNull(command, "Expected null for input 'quitting'");

        command = QuitCommand.parse("quit123");
        assertNull(command, "Expected null for input 'quit123'");
    }

    @Test
    void testParse_NullInput() {
        // Test with null input
        QuitCommand command = QuitCommand.parse(null);
        assertNull(command, "Expected null for null input");
    }

    @Test
    void testParse_EmptyStringInput() {
        // Test with an empty string input
        QuitCommand command = QuitCommand.parse("");
        assertNull(command, "Expected null for empty string input");
    }

    @Test
    void testParse_SpacesInInput() {
        // Test with strings containing spaces
        QuitCommand command = QuitCommand.parse(" quit ");
        assertNull(command, "Expected null for input ' quit '");

        command = QuitCommand.parse("  ");
        assertNull(command, "Expected null for input '  '");
    }
}