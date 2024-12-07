package boardTests;

import backgammon.board.Checker;
import backgammon.board.Color;
import backgammon.board.Off;
import org.junit.jupiter.api.Test;
import java.util.Stack;
import static org.junit.jupiter.api.Assertions.*;

class OffTests {

    @Test
    void testInitialState() {
        // Create a new Off object
        Off off = new Off();

        // Ensure both stacks (RED and BLUE) are initialized and empty
        assertTrue(off.getOffOfColor(Color.RED).isEmpty(), "RED stack should be initially empty");
        assertTrue(off.getOffOfColor(Color.BLUE).isEmpty(), "BLUE stack should be initially empty");
    }

    @Test
    void testAddChecker() {
        // Create a new Off object and add a checker
        Off off = new Off();
        Checker redChecker = new Checker(Color.RED);
        Checker blueChecker = new Checker(Color.BLUE);

        // Add RED checker and verify
        off.addChecker(redChecker);
        Stack<Checker> redStack = off.getOffOfColor(Color.RED);
        assertEquals(1, redStack.size(), "RED stack should contain 1 checker");
        assertEquals(redChecker, redStack.peek(), "Top of RED stack should be the added checker");

        // Add BLUE checker and verify
        off.addChecker(blueChecker);
        Stack<Checker> blueStack = off.getOffOfColor(Color.BLUE);
        assertEquals(1, blueStack.size(), "BLUE stack should contain 1 checker");
        assertEquals(blueChecker, blueStack.peek(), "Top of BLUE stack should be the added checker");
    }

    @Test
    void testAddMultipleCheckers() {
        // Create a new Off object and add multiple checkers
        Off off = new Off();
        Checker redChecker1 = new Checker(Color.RED);
        Checker redChecker2 = new Checker(Color.RED);

        // Add two RED checkers and verify
        off.addChecker(redChecker1);
        off.addChecker(redChecker2);

        Stack<Checker> redStack = off.getOffOfColor(Color.RED);
        assertEquals(2, redStack.size(), "RED stack should contain 2 checkers");
        assertEquals(redChecker2, redStack.peek(), "Top of RED stack should be the last added checker");
    }

    @Test
    void testCloneOff() {
        // Create a new Off object and add checkers
        Off off = new Off();
        Checker redChecker = new Checker(Color.RED);
        Checker blueChecker = new Checker(Color.BLUE);
        off.addChecker(redChecker);
        off.addChecker(blueChecker);

        // Clone the Off object
        Off clonedOff = off.cloneOff();

        // Verify the clone contains the same data
        assertEquals(1, clonedOff.getOffOfColor(Color.RED).size(), "Cloned RED stack should have the same number of checkers");
        assertEquals(redChecker, clonedOff.getOffOfColor(Color.RED).peek(), "Cloned RED stack top checker should match");

        assertEquals(1, clonedOff.getOffOfColor(Color.BLUE).size(), "Cloned BLUE stack should have the same number of checkers");
        assertEquals(blueChecker, clonedOff.getOffOfColor(Color.BLUE).peek(), "Cloned BLUE stack top checker should match");

        // Modify the original and ensure the clone is unaffected
        off.addChecker(new Checker(Color.RED));
        assertEquals(1, clonedOff.getOffOfColor(Color.RED).size(), "Cloned RED stack should remain unaffected by modifications to original");
    }
}
