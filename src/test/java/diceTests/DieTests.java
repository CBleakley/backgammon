package diceTests;

import backgammon.Dice.Die;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DieTests {

    @Test
    void testInitialFaceValueIsNull() {
        // Create a new Die object and check that faceValue is null initially
        Die die = new Die();
        assertNull(die.getFaceValue(), "Initial faceValue should be null");
    }

    @Test
    void testRollReturnsValidFaceValue() {
        // Create a new Die object and roll it
        Die die = new Die();
        int result = die.roll();

        // Ensure the result is between 1 and 6
        assertTrue(result >= 1 && result <= 6, "Roll result should be between 1 and 6");
    }

    @Test
    void testRollUpdatesFaceValue() {
        // Create a new Die object, roll it, and check that faceValue is updated
        Die die = new Die();
        int result = die.roll();

        // Ensure faceValue matches the roll result
        assertEquals(result, die.getFaceValue(), "faceValue should match the result of roll()");
    }

    @Test
    void testMultipleRollsReturnValidFaceValues() {
        // Create a new Die object and roll it multiple times
        Die die = new Die();

        for (int i = 0; i < 100; i++) {
            int result = die.roll();
            // Ensure every roll result is between 1 and 6
            assertTrue(result >= 1 && result <= 6, "Roll result should be between 1 and 6");
        }
    }
}
