package boardTests;

import backgammon.board.Bar;
import backgammon.board.Checker;
import backgammon.board.Color;
import org.junit.jupiter.api.Test;
import java.util.Stack;
import static org.junit.jupiter.api.Assertions.*;

class BarTests {

    @Test
    void testInitialState() {
        // Create a new Bar object
        Bar bar = new Bar();

        // Verify that both stacks (RED and BLUE) are initialized and empty
        assertTrue(bar.getBarOfColor(Color.RED).isEmpty(), "RED stack should be initially empty");
        assertTrue(bar.getBarOfColor(Color.BLUE).isEmpty(), "BLUE stack should be initially empty");
    }

    @Test
    void testAddChecker() {
        // Create a new Bar object and Checkers
        Bar bar = new Bar();
        Checker redChecker = new Checker(Color.RED);
        Checker blueChecker = new Checker(Color.BLUE);

        // Add RED checker and verify
        bar.addChecker(redChecker);
        Stack<Checker> redStack = bar.getBarOfColor(Color.RED);
        assertEquals(1, redStack.size(), "RED stack should contain 1 checker");
        assertEquals(redChecker, redStack.peek(), "Top of RED stack should be the added checker");

        // Add BLUE checker and verify
        bar.addChecker(blueChecker);
        Stack<Checker> blueStack = bar.getBarOfColor(Color.BLUE);
        assertEquals(1, blueStack.size(), "BLUE stack should contain 1 checker");
        assertEquals(blueChecker, blueStack.peek(), "Top of BLUE stack should be the added checker");
    }

    @Test
    void testAddMultipleCheckers() {
        // Create a new Bar object and add multiple checkers
        Bar bar = new Bar();
        Checker redChecker1 = new Checker(Color.RED);
        Checker redChecker2 = new Checker(Color.RED);

        // Add two RED checkers and verify
        bar.addChecker(redChecker1);
        bar.addChecker(redChecker2);

        Stack<Checker> redStack = bar.getBarOfColor(Color.RED);
        assertEquals(2, redStack.size(), "RED stack should contain 2 checkers");
        assertEquals(redChecker2, redStack.peek(), "Top of RED stack should be the last added checker");
    }

    @Test
    void testCloneBar() {
        // Create a new Bar object and add checkers
        Bar bar = new Bar();
        Checker redChecker = new Checker(Color.RED);
        Checker blueChecker = new Checker(Color.BLUE);
        bar.addChecker(redChecker);
        bar.addChecker(blueChecker);

        // Clone the Bar object
        Bar clonedBar = bar.cloneBar();

        // Verify the clone contains the same data
        assertEquals(1, clonedBar.getBarOfColor(Color.RED).size(), "Cloned RED stack should have the same number of checkers");
        assertEquals(redChecker, clonedBar.getBarOfColor(Color.RED).peek(), "Cloned RED stack top checker should match");

        assertEquals(1, clonedBar.getBarOfColor(Color.BLUE).size(), "Cloned BLUE stack should have the same number of checkers");
        assertEquals(blueChecker, clonedBar.getBarOfColor(Color.BLUE).peek(), "Cloned BLUE stack top checker should match");

        // Modify the original and ensure the clone is unaffected
        bar.addChecker(new Checker(Color.RED));
        assertEquals(1, clonedBar.getBarOfColor(Color.RED).size(), "Cloned RED stack should remain unaffected by modifications to original");
    }
}
