package boardTests;

import backgammon.board.Board;
import backgammon.board.Checker;
import backgammon.board.Color;
import backgammon.board.Point;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BoardTests {

    @Test
    void testInitialisation() {
        // Create a new Board
        Board board = new Board();
        List<Point> points = board.getPoints();

        // Verify the number of points
        assertEquals(24, points.size(), "Board should have 24 points");

        // Verify initial RED checkers' positions
        assertEquals(2, points.get(0).getNumberOfCheckers(), "Point 1 should have 2 RED checkers");
        assertEquals(Color.RED, points.get(0).getTopCheckerColor(), "Point 1 top checker should be RED");

        assertEquals(5, points.get(11).getNumberOfCheckers(), "Point 12 should have 5 RED checkers");
        assertEquals(Color.RED, points.get(11).getTopCheckerColor(), "Point 12 top checker should be RED");

        assertEquals(3, points.get(16).getNumberOfCheckers(), "Point 17 should have 3 RED checkers");
        assertEquals(Color.RED, points.get(16).getTopCheckerColor(), "Point 17 top checker should be RED");

        assertEquals(5, points.get(18).getNumberOfCheckers(), "Point 19 should have 5 RED checkers");
        assertEquals(Color.RED, points.get(18).getTopCheckerColor(), "Point 19 top checker should be RED");

        // Verify initial BLUE checkers' positions
        assertEquals(2, points.get(23).getNumberOfCheckers(), "Point 24 should have 2 BLUE checkers");
        assertEquals(Color.BLUE, points.get(23).getTopCheckerColor(), "Point 24 top checker should be BLUE");

        assertEquals(5, points.get(12).getNumberOfCheckers(), "Point 13 should have 5 BLUE checkers");
        assertEquals(Color.BLUE, points.get(12).getTopCheckerColor(), "Point 13 top checker should be BLUE");

        assertEquals(3, points.get(7).getNumberOfCheckers(), "Point 8 should have 3 BLUE checkers");
        assertEquals(Color.BLUE, points.get(7).getTopCheckerColor(), "Point 8 top checker should be BLUE");

        assertEquals(5, points.get(5).getNumberOfCheckers(), "Point 6 should have 5 BLUE checkers");
        assertEquals(Color.BLUE, points.get(5).getTopCheckerColor(), "Point 6 top checker should be BLUE");
    }

    @Test
    void testBarAndOffInitialisation() {
        // Create a new Board
        Board board = new Board();

        // Verify bar and off initialisation
        assertNotNull(board.getBar(), "Bar should be initialised");
        assertNotNull(board.getOff(), "Off should be initialised");

        // Verify that bar and off are empty initially
        assertTrue(board.getBar().getBarOfColor(Color.RED).isEmpty(), "Bar for RED should be empty initially");
        assertTrue(board.getBar().getBarOfColor(Color.BLUE).isEmpty(), "Bar for BLUE should be empty initially");

        assertTrue(board.getOff().getOffOfColor(Color.RED).isEmpty(), "Off for RED should be empty initially");
        assertTrue(board.getOff().getOffOfColor(Color.BLUE).isEmpty(), "Off for BLUE should be empty initially");
    }

    @Test
    void testCloneBoard() {
        // Create a new Board and clone it
        Board board = new Board();
        Board clonedBoard = board.cloneBoard();

        // Verify points in the clone
        List<Point> originalPoints = board.getPoints();
        List<Point> clonedPoints = clonedBoard.getPoints();
        assertEquals(originalPoints.size(), clonedPoints.size(), "Cloned board should have the same number of points");

        for (int i = 0; i < originalPoints.size(); i++) {
            assertEquals(originalPoints.get(i).getNumberOfCheckers(), clonedPoints.get(i).getNumberOfCheckers(),
                    "Point " + (i + 1) + " in cloned board should have the same number of checkers");
        }

        // Verify bar and off in the clone
        assertEquals(board.getBar().getBarOfColor(Color.RED).size(), clonedBoard.getBar().getBarOfColor(Color.RED).size(),
                "Cloned board should have the same number of RED checkers in the bar");
        assertEquals(board.getOff().getOffOfColor(Color.RED).size(), clonedBoard.getOff().getOffOfColor(Color.RED).size(),
                "Cloned board should have the same number of RED checkers in the off");

        // Modify the original and ensure the clone is unaffected
        board.getPoints().get(0).addChecker(new Checker(Color.RED));
        assertNotEquals(board.getPoints().get(0).getNumberOfCheckers(), clonedBoard.getPoints().get(0).getNumberOfCheckers(),
                "Cloned board should remain unaffected by modifications to the original");
    }
}
