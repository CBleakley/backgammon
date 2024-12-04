package diceTests;

import backgammon.Dice.DicePair;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DicePairTests {

    @Test
    void testRollReturnsTwoResults() {
        // Create a new DicePair object and roll the dice
        DicePair dicePair = new DicePair();
        List<Integer> results = dicePair.roll();

        // Ensure the results contain exactly two values
        assertNotNull(results, "Results should not be null");
        assertEquals(2, results.size(), "Results should contain exactly two values");
    }

    @Test
    void testRollResultsAreInRange() {
        // Create a new DicePair object and roll the dice
        DicePair dicePair = new DicePair();
        List<Integer> results = dicePair.roll();

        // Ensure both dice results are between 1 and 6
        assertTrue(results.get(0) >= 1 && results.get(0) <= 6, "Die 1 result should be between 1 and 6");
        assertTrue(results.get(1) >= 1 && results.get(1) <= 6, "Die 2 result should be between 1 and 6");
    }

    @Test
    void testMultipleRollsReturnValidResults() {
        // Create a new DicePair object
        DicePair dicePair = new DicePair();

        // Roll the dice multiple times and check the results
        for (int i = 0; i < 100; i++) {
            List<Integer> results = dicePair.roll();
            assertEquals(2, results.size(), "Each roll should contain exactly two values");
            assertTrue(results.get(0) >= 1 && results.get(0) <= 6, "Die 1 result should be between 1 and 6");
            assertTrue(results.get(1) >= 1 && results.get(1) <= 6, "Die 2 result should be between 1 and 6");
        }
    }
}
