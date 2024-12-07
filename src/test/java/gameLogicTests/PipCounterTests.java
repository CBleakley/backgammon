package gameLogicTests;

import backgammon.board.Board;
import backgammon.board.Checker;
import backgammon.board.Color;
import backgammon.board.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static backgammon.gameLogic.PipCounter.calculatePipCount;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PipCounterTests {
    private Board board;

    @BeforeEach
    void setUp() {
        // Initialize a new board for each test
        board = new Board();
    }

    @Test
    void testCalculatePipCountEmptyBoard() {
        // On an empty board, pip count should be 0 for both colors
        assertEquals(0, calculatePipCount(board, Color.RED), "Pip count for RED on an empty board should be 0");
        assertEquals(0, calculatePipCount(board, Color.BLUE), "Pip count for BLUE on an empty board should be 0");
    }

    @Test
    void testCalculatePipCountWithCheckers() {
        // Place checkers on specific points
        Point point1 = board.getPoints().get(0); // Point 1 (index 0)
        point1.addChecker(new Checker(Color.RED)); // Add one RED checker

        Point point24 = board.getPoints().get(23); // Point 24 (index 23)
        point24.addChecker(new Checker(Color.BLUE)); // Add one BLUE checker

        // Calculate expected pip counts
        int redExpectedPipCount = 25 - 1; // RED checker on point 1 contributes 24
        int blueExpectedPipCount = 24; // BLUE checker on point 24 contributes 24

        assertEquals(redExpectedPipCount, calculatePipCount(board, Color.RED), "Pip count for RED should match the expected value");
        assertEquals(blueExpectedPipCount, calculatePipCount(board, Color.BLUE), "Pip count for BLUE should match the expected value");
    }

    @Test
    void testCalculatePipCountWithBarCheckers() {
        // Add checkers to the bar
        board.getBar().addChecker(new Checker(Color.RED));
        board.getBar().addChecker(new Checker(Color.RED));
        board.getBar().addChecker(new Checker(Color.BLUE));

        // Pip count from checkers on the bar
        int redBarPipCount = 2 * 25; // Two RED checkers on the bar
        int blueBarPipCount = 1 * 25; // One BLUE checker on the bar

        assertEquals(redBarPipCount, calculatePipCount(board, Color.RED), "Pip count for RED on the bar should match the expected value");
        assertEquals(blueBarPipCount, calculatePipCount(board, Color.BLUE), "Pip count for BLUE on the bar should match the expected value");
    }

    @Test
    void testCalculatePipCountMixedBoard() {
        // Place multiple checkers on the board
        board.getPoints().get(0).addChecker(new Checker(Color.RED));  // Point 1
        board.getPoints().get(5).addChecker(new Checker(Color.RED));  // Point 6
        board.getPoints().get(23).addChecker(new Checker(Color.BLUE)); // Point 24
        board.getPoints().get(12).addChecker(new Checker(Color.BLUE)); // Point 13

        // Add checkers to the bar
        board.getBar().addChecker(new Checker(Color.RED)); // RED checker on the bar

        // Calculate expected pip counts
        int redExpectedPipCount = (25 - 1) + (25 - 6) + (1 * 25); // RED: Point 1, Point 6, and bar
        int blueExpectedPipCount = 24 + 13; // BLUE: Point 24 and Point 13

        assertEquals(redExpectedPipCount, calculatePipCount(board, Color.RED), "Pip count for RED should match the expected value");
        assertEquals(blueExpectedPipCount, calculatePipCount(board, Color.BLUE), "Pip count for BLUE should match the expected value");
    }
}

