package diceTests;

import backgammon.Dice.DoubleDice;
import backgammon.board.Color;
import backgammon.player.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DoubleDiceTests {

    @Test
    void testInitialValues() {
        // Create a new DoubleDice object
        DoubleDice doubleDice = new DoubleDice();

        // Check initial values
        assertNull(doubleDice.getOwner(), "Initial owner should be null");
        assertEquals(1, doubleDice.getMultiplier(), "Initial multiplier should be 1");
    }

    @Test
    void testSetOwner() {
        // Create a new DoubleDice object and a mock Player object
        Player player = new Player("Olivia", Color.BLUE); // Assuming a simple Player class exists
        DoubleDice doubleDice = new DoubleDice();

        // Set the owner and verify
        doubleDice.setOwner(player);
        assertEquals(player, doubleDice.getOwner(), "Owner should be set correctly");
    }

    @Test
    void testUpdateMultiplier() {
        // Create a new DoubleDice object
        DoubleDice doubleDice = new DoubleDice();

        // Update multiplier and verify
        doubleDice.updateMultiplier();
        assertEquals(2, doubleDice.getMultiplier(), "Multiplier should be updated to 2 after first update");

        doubleDice.updateMultiplier();
        assertEquals(4, doubleDice.getMultiplier(), "Multiplier should be updated to 4 after second update");

        doubleDice.updateMultiplier();
        assertEquals(8, doubleDice.getMultiplier(), "Multiplier should be updated to 8 after third update");
    }
}
