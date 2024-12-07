package playerInputTests;

import backgammon.playerInput.SetDiceCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SetDiceCommandTests {

    @Test
    void testParse_ValidDiceInput() {
        // Test with valid "dice" inputs
        SetDiceCommand command = SetDiceCommand.parse("dice12");
        assertNotNull(command, "Expected SetDiceCommand object for input 'dice12'");
        assertEquals(1, command.getDice1(), "Dice1 should be 1");
        assertEquals(2, command.getDice2(), "Dice2 should be 2");

        command = SetDiceCommand.parse("dice34");
        assertNotNull(command, "Expected SetDiceCommand object for input 'dice34'");
        assertEquals(3, command.getDice1(), "Dice1 should be 3");
        assertEquals(4, command.getDice2(), "Dice2 should be 4");
    }

    @Test
    void testParse_InvalidPrefix() {
        // Test with an invalid prefix
        SetDiceCommand command = SetDiceCommand.parse("roll12");
        assertNull(command, "Expected null for input 'roll12'");
    }

    @Test
    void testParse_NullInput() {
        // Test with null input
        SetDiceCommand command = SetDiceCommand.parse(null);
        assertNull(command, "Expected null for null input");
    }

    @Test
    void testParse_InvalidLengthInput() {
        // Test with an input that has more or less than two dice values
        SetDiceCommand command = SetDiceCommand.parse("dice1");
        assertNull(command, "Expected null for input 'dice1'");

        command = SetDiceCommand.parse("dice123");
        assertNull(command, "Expected null for input 'dice123'");
    }

    @Test
    void testParse_NonDigitDiceValues() {
        // Test with non-digit characters in dice values
        SetDiceCommand command = SetDiceCommand.parse("dice1a");
        assertNull(command, "Expected null for input 'dice1a'");

        command = SetDiceCommand.parse("diceab");
        assertNull(command, "Expected null for input 'diceab'");
    }

    @Test
    void testParse_SpacesInInput() {
        // Test with inputs containing spaces
        SetDiceCommand command = SetDiceCommand.parse(" dice12");
        assertNull(command, "Expected null for input ' dice12'");

        command = SetDiceCommand.parse("dice 12");
        assertNull(command, "Expected null for input 'dice 12'");
    }

    @Test
    void testParse_DiceValuesOutOfRange() {
        // Test with dice values that are logically incorrect
        SetDiceCommand command = SetDiceCommand.parse("dice07");
        assertNotNull(command, "Expected SetDiceCommand object for input 'dice07'");
        assertEquals(0, command.getDice1(), "Dice1 should be 0");
        assertEquals(7, command.getDice2(), "Dice2 should be 7");
    }
}
