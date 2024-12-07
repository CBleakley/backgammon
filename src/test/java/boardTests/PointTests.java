package boardTests;

import backgammon.board.Checker;
import backgammon.board.Color;
import backgammon.board.Point;
import org.junit.jupiter.api.Test;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class PointTests {

    @Test
    void testInitialState() {
        // Create a new Point
        Point point = new Point();

        // Test initial state
        assertFalse(point.hasCheckers(), "Point should initially have no checkers");
        assertEquals(0, point.getNumberOfCheckers(), "Point should initially have 0 checkers");
        assertNull(point.getTopCheckerColor(), "Point should have no top checker color initially");
        assertTrue(point.getCheckers().isEmpty(), "Point's checkers list should initially be empty");
    }

    @Test
    void testAddChecker() {
        // Create a new Point and Checker
        Point point = new Point();
        Checker checker = new Checker(Color.RED);

        // Add a checker and test
        point.addChecker(checker);
        assertTrue(point.hasCheckers(), "Point should have checkers after adding one");
        assertEquals(1, point.getNumberOfCheckers(), "Point should have 1 checker after adding one");
        assertEquals(Color.RED, point.getTopCheckerColor(), "Top checker color should be RED");
    }

    @Test
    void testRemoveTopChecker() {
        // Create a new Point and add a Checker
        Point point = new Point();
        Checker checker = new Checker(Color.BLUE);
        point.addChecker(checker);

        // Remove the checker and test
        Checker removedChecker = point.removeTopChecker();
        assertEquals(checker, removedChecker, "Removed checker should be the same as the added one");
        assertFalse(point.hasCheckers(), "Point should have no checkers after removing the only one");
        assertEquals(0, point.getNumberOfCheckers(), "Point should have 0 checkers after removing all");
        assertNull(point.getTopCheckerColor(), "Top checker color should be null after removing all");
    }

    @Test
    void testGetCheckerStackCopy() {
        // Create a new Point and add Checkers
        Point point = new Point();
        Checker checker1 = new Checker(Color.RED);
        Checker checker2 = new Checker(Color.BLUE);
        point.addChecker(checker1);
        point.addChecker(checker2);

        // Get a copy of the checker stack and test
        Stack<Checker> stackCopy = point.getCheckerStackCopy();
        assertEquals(2, stackCopy.size(), "Stack copy should have the same number of checkers");
        assertEquals(checker2, stackCopy.pop(), "Top of copied stack should match the original");
        assertEquals(checker1, stackCopy.pop(), "Remaining item in copied stack should match the original");
        assertTrue(stackCopy.isEmpty(), "Copied stack should be empty after popping all items");

        // Ensure the original stack is unaffected
        assertEquals(2, point.getNumberOfCheckers(), "Original stack should remain unchanged");
    }

    @Test
    void testClonePoint() {
        // Create a new Point and add Checkers
        Point point = new Point();
        Checker checker1 = new Checker(Color.RED);
        Checker checker2 = new Checker(Color.BLUE);
        point.addChecker(checker1);
        point.addChecker(checker2);

        // Clone the point and test
        Point clonedPoint = point.clonePoint();
        assertEquals(2, clonedPoint.getNumberOfCheckers(), "Cloned point should have the same number of checkers");
        assertEquals(Color.BLUE, clonedPoint.getTopCheckerColor(), "Top checker color should match in cloned point");

        // Modify the original point and ensure the clone is unaffected
        point.removeTopChecker();
        assertEquals(1, point.getNumberOfCheckers(), "Original point should reflect changes");
        assertEquals(2, clonedPoint.getNumberOfCheckers(), "Cloned point should remain unaffected by changes to original");
    }
}