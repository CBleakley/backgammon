package viewTests;

import backgammon.view.BoardFormatting;
import backgammon.Dice.DoubleDice;
import backgammon.board.Board;
import backgammon.board.Point;
import backgammon.board.Color;
import backgammon.player.Player;
import backgammon.view.BoardDisplayBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardDisplayBuilderTests {

    @Test
    void testBuildBoardInitialBoard() {
        // Arrange: Create a new board with default setup
        Board board = new Board();
        board.initialiseStartingPosition();
        DoubleDice doubleDice = new DoubleDice();
        List<Integer> rollToPlay = null; // No dice rolled yet
        Color colorToPlay = null; // No specific player's turn

        // Act: Build the display board
        String boardDisplay = BoardDisplayBuilder.buildBoard(board, doubleDice, rollToPlay, colorToPlay);

        // Assert: Verify some key elements of the display
        assertNotNull(boardDisplay, "Board display should not be null");
        assertTrue(boardDisplay.contains("OFF: "), "Display should include 'OFF' section");
        assertTrue(boardDisplay.contains("|BAR|"), "Display should include 'BAR' section");
        assertTrue(boardDisplay.contains(BoardFormatting.RED_CHECKER) || boardDisplay.contains(BoardFormatting.BLUE_CHECKER),
                "Display should include checkers");
    }

    @Test
    void testBuildBoardWithDoubleDice() {
        // Arrange: Set up the board and double dice
        Board board = new Board();
        DoubleDice doubleDice = new DoubleDice();
        doubleDice.setOwner(new Player("Olivia", Color.RED)); // Set owner to RED
        doubleDice.updateMultiplier(); // Double multiplier to 2
        List<Integer> rollToPlay = null;
        Color colorToPlay = Color.RED;

        // Act: Build the display board
        String boardDisplay = BoardDisplayBuilder.buildBoard(board, doubleDice, rollToPlay, colorToPlay);

        // Assert: Verify the double dice display
        assertTrue(boardDisplay.contains("[2]"), "Display should show double dice value 4");
    }

    @Test
    void testBuildBoardWithDiceRolls() {
        // Arrange: Set up the board with dice rolls
        Board board = new Board();
        DoubleDice doubleDice = new DoubleDice();
        List<Integer> rollToPlay = List.of(3, 5); // Example dice roll
        Color colorToPlay = Color.BLUE;

        // Act: Build the display board
        String boardDisplay = BoardDisplayBuilder.buildBoard(board, doubleDice, rollToPlay, colorToPlay);

        // Assert: Verify the dice rolls are displayed
        assertTrue(boardDisplay.contains("|          3-5          |   |                       |"), "Display should include rolled dice values 3 and 5");
    }

    @Test
    void testBuildBoardEmptyBoard() {
        // Arrange: Create an empty board (clear checkers manually)
        Board board = new Board();
        for (Point point : board.getPoints()) {
            while (point.hasCheckers()) {
                point.removeTopChecker();
            }
        }
        DoubleDice doubleDice = new DoubleDice();
        List<Integer> rollToPlay = List.of(4, 4, 6, 6); // Example roll for doubles
        Color colorToPlay = null;

        // Act: Build the display board
        String boardDisplay = BoardDisplayBuilder.buildBoard(board, doubleDice, rollToPlay, colorToPlay);

        assertTrue(boardDisplay.contains("4-4-6-6"), "Display should show rolled dice 4-4-6-6");
    }
}
