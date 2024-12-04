package playerInputTests;

import backgammon.playerInput.DoubleCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DoubleCommandTests {

    @Test
    void testParse_ValidDoubleInput() {
        // Test with a valid "double" input (case insensitive)
        DoubleCommand command = DoubleCommand.parse("double");
        assertNotNull(command, "Expected DoubleCommand object for input 'double'");

        command = DoubleCommand.parse("DOUBLE");
        assertNotNull(command, "Expected DoubleCommand object for input 'DOUBLE'");
    }

    @Test
    void testParse_InvalidInput() {
        // Test with an invalid input that is not "double"
        DoubleCommand command = DoubleCommand.parse("something else");
        assertNull(command, "Expected null for input 'something else'");

        command = DoubleCommand.parse("DoubleMe");
        assertNull(command, "Expected null for input 'DoubleMe'");
    }

    @Test
    void testParse_NullInput() {
        // Test with null input
        DoubleCommand command = DoubleCommand.parse(null);
        assertNull(command, "Expected null for null input");
    }

    @Test
    void testParse_EmptyStringInput() {
        // Test with an empty string input
        DoubleCommand command = DoubleCommand.parse("");
        assertNull(command, "Expected null for empty string input");
    }

    @Test
    void testParse_SpacesInInput() {
        // Test with strings containing spaces
        DoubleCommand command = DoubleCommand.parse(" double ");
        assertNull(command, "Expected null for input ' double '");

        command = DoubleCommand.parse("  ");
        assertNull(command, "Expected null for input '  '");
    }
}
