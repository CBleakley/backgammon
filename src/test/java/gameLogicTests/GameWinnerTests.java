package gameLogicTests;

import backgammon.board.Color;
import backgammon.gameLogic.EndingType;
import backgammon.gameLogic.GameWinner;
import backgammon.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameWinnerTests {
    @Test
    void testGetWinner() {
        Player player = new Player("Olivia", Color.RED);
        GameWinner gameWinner = new GameWinner(player, 2, EndingType.SINGLE);

        assertEquals(player, gameWinner.getWinner(), "getWinner should return the correct player");
    }

    @Test
    void testGetPointsWonSingle() {
        Player player = new Player("Olivia", Color.RED);
        GameWinner gameWinner = new GameWinner(player, 2, EndingType.SINGLE);

        assertEquals(2, gameWinner.getPointsWon(), "Points won for SINGLE should be doubleValue * 1");
    }

    @Test
    void testGetPointsWonGammon() {
        Player player = new Player("Olivia", Color.RED);
        GameWinner gameWinner = new GameWinner(player, 3, EndingType.GAMMON);

        assertEquals(6, gameWinner.getPointsWon(), "Points won for GAMMON should be doubleValue * 2");
    }

    @Test
    void testGetPointsWonBackgammon() {
        Player player = new Player("Olivia", Color.RED);
        GameWinner gameWinner = new GameWinner(player, 4, EndingType.BACKGAMMON);

        assertEquals(12, gameWinner.getPointsWon(), "Points won for BACKGAMMON should be doubleValue * 3");
    }

    @Test
    void testGetPointsWonDoubleRefused() {
        Player player = new Player("Olivia", Color.RED);
        GameWinner gameWinner = new GameWinner(player, 1, EndingType.DOUBLE_REFUSED);

        assertEquals(1, gameWinner.getPointsWon(), "Points won for DOUBLE_REFUSED should be doubleValue * 1");
    }

    @Test
    void testGetEndingType() {
        Player player = new Player("Olivia", Color.RED);
        GameWinner gameWinner = new GameWinner(player, 2, EndingType.GAMMON);

        assertEquals(EndingType.GAMMON, gameWinner.getEndingType(), "getEndingType should return the correct EndingType");
    }
}
