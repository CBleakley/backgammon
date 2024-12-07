package playerInputTests;

import backgammon.playerInput.HintCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HintCommandTests {

    @Test
    void testParse_ValidHintInput() {
        // Test with valid "hint" input (case insensitive)
        HintCommand command = HintCommand.parse("hint");
        assertNotNull(command, "Expected HintCommand object for input 'hint'");

        command = HintCommand.parse("HINT");
        assertNotNull(command, "Expected HintCommand object for input 'HINT'");
    }

    @Test
    void testParse_InvalidInput() {
        // Test with invalid input that is not "hint"
        HintCommand command = HintCommand.parse("help");
        assertNull(command, "Expected null for input 'help'");

        command = HintCommand.parse("hint please");
        assertNull(command, "Expected null for input 'hint please'");
    }

    @Test
    void testParse_NullInput() {
        // Test with null input
        HintCommand command = HintCommand.parse(null);
        assertNull(command, "Expected null for null input");
    }

    @Test
    void testParse_EmptyStringInput() {
        // Test with an empty string input
        HintCommand command = HintCommand.parse("");
        assertNull(command, "Expected null for empty string input");
    }

    @Test
    void testParse_SpacesInInput() {
        // Test with strings containing spaces
        HintCommand command = HintCommand.parse(" hint ");
        assertNull(command, "Expected null for input ' hint '");

        command = HintCommand.parse("  ");
        assertNull(command, "Expected null for input '  '");
    }
}

