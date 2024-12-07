package playerInputTests;

import backgammon.playerInput.PipCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PipCommandTests {

    @Test
    void testParse_ValidPipInput() {
        // Test with valid "pip" input (case insensitive)
        PipCommand command = PipCommand.parse("pip");
        assertNotNull(command, "Expected PipCommand object for input 'pip'");

        command = PipCommand.parse("PIP");
        assertNotNull(command, "Expected PipCommand object for input 'PIP'");

        command = PipCommand.parse("PiP");
        assertNotNull(command, "Expected PipCommand object for input 'PiP'");
    }

    @Test
    void testParse_InvalidInput() {
        // Test with invalid input that is not "pip"
        PipCommand command = PipCommand.parse("pop");
        assertNull(command, "Expected null for input 'pop'");

        command = PipCommand.parse("pip123");
        assertNull(command, "Expected null for input 'pip123'");

        command = PipCommand.parse("pips");
        assertNull(command, "Expected null for input 'pips'");
    }

    @Test
    void testParse_NullInput() {
        // Test with null input
        PipCommand command = PipCommand.parse(null);
        assertNull(command, "Expected null for null input");
    }

    @Test
    void testParse_EmptyStringInput() {
        // Test with an empty string input
        PipCommand command = PipCommand.parse("");
        assertNull(command, "Expected null for empty string input");
    }

    @Test
    void testParse_SpacesInInput() {
        // Test with strings containing spaces
        PipCommand command = PipCommand.parse(" pip ");
        assertNull(command, "Expected null for input ' pip '");

        command = PipCommand.parse("  ");
        assertNull(command, "Expected null for input '  '");
    }
}
