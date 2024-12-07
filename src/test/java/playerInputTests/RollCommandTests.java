package playerInputTests;

import backgammon.playerInput.RollCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RollCommandTests {

    @Test
    void testParse_ValidRollInput() {
        // Test with valid "roll" input (case insensitive)
        RollCommand command = RollCommand.parse("roll");
        assertNotNull(command, "Expected RollCommand object for input 'roll'");

        command = RollCommand.parse("ROLL");
        assertNotNull(command, "Expected RollCommand object for input 'ROLL'");

        command = RollCommand.parse("RoLl");
        assertNotNull(command, "Expected RollCommand object for input 'RoLl'");
    }

    @Test
    void testParse_InvalidInput() {
        // Test with invalid input that is not "roll"
        RollCommand command = RollCommand.parse("dice");
        assertNull(command, "Expected null for input 'dice'");

        command = RollCommand.parse("rolling");
        assertNull(command, "Expected null for input 'rolling'");

        command = RollCommand.parse("roll123");
        assertNull(command, "Expected null for input 'roll123'");
    }

    @Test
    void testParse_NullInput() {
        // Test with null input
        RollCommand command = RollCommand.parse(null);
        assertNull(command, "Expected null for null input");
    }

    @Test
    void testParse_EmptyStringInput() {
        // Test with an empty string input
        RollCommand command = RollCommand.parse("");
        assertNull(command, "Expected null for empty string input");
    }

    @Test
    void testParse_SpacesInInput() {
        // Test with strings containing spaces
        RollCommand command = RollCommand.parse(" roll ");
        assertNull(command, "Expected null for input ' roll '");

        command = RollCommand.parse("  ");
        assertNull(command, "Expected null for input '  '");
    }
}
