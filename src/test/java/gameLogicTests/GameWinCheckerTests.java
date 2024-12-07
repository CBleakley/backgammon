package backgammon.gameLogic;

import backgammon.board.Color;
import backgammon.board.Off;
import backgammon.board.Bar;
import backgammon.board.Point;
import backgammon.board.Board;
import backgammon.board.Checker;
import backgammon.player.Player;
import backgammon.Dice.DoubleDice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import backgammon.view.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the GameWinChecker class.
 */
class GameWinCheckerTests {
    private Player player1;
    private Player player2;
    private DoubleDice doubleDice;
    private final int NUMBER_OF_CHECKERS = Board.NUMBER_OF_CHECKERS_PER_PLAYER;

    @BeforeEach
    void setUp() {
        player1 = new Player("Alice", Color.BLUE);
        player2 = new Player("Bob", Color.RED);
        doubleDice = new DoubleDice();
        // Assuming DoubleDice default multiplier is 1
    }

    /**
     * Helper method to create a Board with specified off and bar counts for both players,
     * and points configuration.
     *
     * @param offPlayer1    Number of Player1's checkers off the board.
     * @param offPlayer2    Number of Player2's checkers off the board.
     * @param barPlayer1    Number of Player1's checkers on the bar.
     * @param barPlayer2    Number of Player2's checkers on the bar.
     * @param pointCheckers Map of point number to list of Checkers.
     *                      Points are numbered from 1 to 24.
     * @return Configured Board instance.
     */
    private Board createBoard(int offPlayer1, int offPlayer2,
                              int barPlayer1, int barPlayer2,
                              Map<Integer, List<Checker>> pointCheckers) {
        Board board = new Board();

        // Set up Off
        Off off = board.getOff();
        for (int i = 0; i < offPlayer1; i++) {
            off.addChecker(new Checker(player1.color()));
        }
        for (int i = 0; i < offPlayer2; i++) {
            off.addChecker(new Checker(player2.color()));
        }

        // Set up Bar
        Bar bar = board.getBar();
        for (int i = 0; i < barPlayer1; i++) {
            bar.addChecker(new Checker(player1.color()));
        }
        for (int i = 0; i < barPlayer2; i++) {
            bar.addChecker(new Checker(player2.color()));
        }

        // Set up Points
        List<Point> points = board.getPoints();
        for (Map.Entry<Integer, List<Checker>> entry : pointCheckers.entrySet()) {
            int pointIndex = entry.getKey();
            List<Checker> checkers = entry.getValue();
            Point point = points.get(pointIndex - 1); // Assuming points are 1-indexed
            for (Checker checker : checkers) {
                point.addChecker(checker);
            }
        }

        return board;
    }


    /**
     * Test when Player 1 has all checkers off and Player 2 has checkers on the bar (Backgammon Win).
     */
    @Test
    void should_Player1_WinWithBackgammon() {
        // Player1 has all checkers off
        int offPlayer1 = NUMBER_OF_CHECKERS;
        int offPlayer2 = 0;

        // Player2 has checkers on the bar
        int barPlayer1 = 0;
        int barPlayer2 = 2;

        // No checkers in opponent's home board
        Map<Integer, List<Checker>> pointCheckers = new HashMap<>();

        Board board = createBoard(offPlayer1, offPlayer2, barPlayer1, barPlayer2, pointCheckers);

        GameWinner winner = GameWinChecker.checkGameWon(board, doubleDice, player1, player2);

        assertNotNull(winner);
        assertEquals(3, winner.getPointsWon());
    }

    /**
     * Test when Player 1 has all checkers off and Player 2 has no checkers on the bar
     * but has checkers in the opponent's home board (Backgammon Win).
     */
    @Test
    void should_Player1_WinWithBackgammon_OpponentsHomeBoard() {
        // Player1 has all checkers off
        int offPlayer1 = NUMBER_OF_CHECKERS;
        int offPlayer2 = 0;

        // No checkers on the bar
        int barPlayer1 = 0;
        int barPlayer2 = 0;

        // Player2 has checkers in Player1's home board (points 1-6)
        Map<Integer, List<Checker>> pointCheckers = new HashMap<>();
        pointCheckers.put(1, Collections.singletonList(new Checker(player2.color())));
        pointCheckers.put(2, Collections.singletonList(new Checker(player2.color())));

        Board board = createBoard(offPlayer1, offPlayer2, barPlayer1, barPlayer2, pointCheckers);

        GameWinner winner = GameWinChecker.checkGameWon(board, doubleDice, player1, player2);
        System.out.println(winner);

        assertNotNull(winner);
        assertEquals(player1, winner.getWinner());
        assertEquals(EndingType.BACKGAMMON, winner.getEndingType());
        assertEquals(3, winner.getPointsWon());
    }

    /**
     * Test when Player 1 has all checkers off and Player 2 has no checkers on the bar
     * and no checkers in the opponent's home board (Gammon Win).
     */
    @Test
    void should_Player1_WinWithGammon() {
        // Player1 has all checkers off
        int offPlayer1 = NUMBER_OF_CHECKERS;
        int offPlayer2 = 0;

        // No checkers on the bar
        int barPlayer1 = 0;
        int barPlayer2 = 0;

        // No checkers in opponent's home board
        Map<Integer, List<Checker>> pointCheckers = new HashMap<>();

        Board board = createBoard(offPlayer1, offPlayer2, barPlayer1, barPlayer2, pointCheckers);

        GameWinner winner = GameWinChecker.checkGameWon(board, doubleDice, player1, player2);

        assertNotNull(winner);
        assertEquals(player1, winner.getWinner());
        assertEquals(EndingType.GAMMON, winner.getEndingType());
        assertEquals(2, winner.getPointsWon());
    }

    /**
     * Test when Player 2 has all checkers off and Player 1 has checkers on the bar (Backgammon Win).
     */
    @Test
    void should_Player2_WinWithBackgammon() {
        // Player2 has all checkers off
        int offPlayer1 = 0;
        int offPlayer2 = NUMBER_OF_CHECKERS;

        // Player1 has checkers on the bar
        int barPlayer1 = 3;
        int barPlayer2 = 0;

        // No checkers in opponent's home board
        Map<Integer, List<Checker>> pointCheckers = new HashMap<>();

        Board board = createBoard(offPlayer1, offPlayer2, barPlayer1, barPlayer2, pointCheckers);

        GameWinner winner = GameWinChecker.checkGameWon(board, doubleDice, player1, player2);

        assertNotNull(winner);
        assertEquals(player2, winner.getWinner());
        assertEquals(EndingType.BACKGAMMON, winner.getEndingType());
        assertEquals(3, winner.getPointsWon());
    }


    /**
     * Test when Player 2 has all checkers off and Player 1 has no checkers on the bar
     * and no checkers in the opponent's home board (Gammon Win).
     */
    @Test
    void should_Player2_WinWithGammon() {
        // Player2 has all checkers off
        int offPlayer1 = 0;
        int offPlayer2 = NUMBER_OF_CHECKERS;

        // No checkers on the bar
        int barPlayer1 = 0;
        int barPlayer2 = 0;

        // No checkers in opponent's home board
        Map<Integer, List<Checker>> pointCheckers = new HashMap<>();

        Board board = createBoard(offPlayer1, offPlayer2, barPlayer1, barPlayer2, pointCheckers);

        GameWinner winner = GameWinChecker.checkGameWon(board, doubleDice, player1, player2);

        assertNotNull(winner);
        assertEquals(player2, winner.getWinner());
        assertEquals(EndingType.GAMMON, winner.getEndingType());
        assertEquals(2, winner.getPointsWon());
    }

    /**
     * Test when neither player has all checkers off (No Win).
     */
    @Test
    void should_NoPlayerWin_When_NeitherHasAllCheckersOff() {
        // Neither player has all checkers off
        int offPlayer1 = 5;
        int offPlayer2 = 7;

        // No checkers on the bar
        int barPlayer1 = 0;
        int barPlayer2 = 0;

        // No checkers in opponent's home board
        Map<Integer, List<Checker>> pointCheckers = new HashMap<>();

        Board board = createBoard(offPlayer1, offPlayer2, barPlayer1, barPlayer2, pointCheckers);

        GameWinner winner = GameWinChecker.checkGameWon(board, doubleDice, player1, player2);

        assertNull(winner);
    }


    /**
     * Test when both players have all checkers off (Invalid State - Should return first player as winner).
     * Assuming that in such a case, Player1 is considered the winner.
     */
    @Test
    void should_Player1_Win_When_BothPlayersHaveAllCheckersOff() {
        // Both players have all checkers off
        int offPlayer1 = NUMBER_OF_CHECKERS;
        int offPlayer2 = NUMBER_OF_CHECKERS;

        // No checkers on the bar
        int barPlayer1 = 0;
        int barPlayer2 = 0;

        // No checkers in opponent's home board
        Map<Integer, List<Checker>> pointCheckers = new HashMap<>();

        Board board = createBoard(offPlayer1, offPlayer2, barPlayer1, barPlayer2, pointCheckers);

        // Depending on implementation, this could be invalid. Here, we assume Player1 is declared first.
        GameWinner winner = GameWinChecker.checkGameWon(board, doubleDice, player1, player2);

        assertNotNull(winner);
        // Depending on business rules, this could be handled differently
        // For this test, we'll assume Player1 is declared the winner if both have all checkers off
        assertEquals(player1, winner.getWinner());
        assertEquals(EndingType.SINGLE, winner.getEndingType());
        assertEquals(1, winner.getPointsWon());
    }
}
