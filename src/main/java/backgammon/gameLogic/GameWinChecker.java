/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.gameLogic;

import backgammon.Dice.DoubleDice;
import backgammon.board.*;
import backgammon.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class to check if a game of backgammon has been won and determine the type of win.
 */
public class GameWinChecker {

    /**
     * Checks if the game has been won by any player and determines the winner, the multiplier, and the type of win.
     *
     * @param board      the current game board
     * @param doubleDice the doubling die used in the game
     * @param player1    the first player
     * @param player2    the second player
     * @return a {@link GameWinner} object containing the winner, multiplier, and ending type,
     *         or {@code null} if no player has won yet
     */
    public static GameWinner checkGameWon(Board board, DoubleDice doubleDice, Player player1, Player player2) {
        int numberOfCheckersOffToWin = Board.NUMBER_OF_CHECKERS_PER_PLAYER;

        Player winner;
        Player loser;
        Off off = board.getOff();
        // Check if a player has won the game by bearing off all checkers and set winner and loser
        if (off.getOffOfColor(player1.color()).size() == numberOfCheckersOffToWin) {
            winner = player1;
            loser = player2;
        } else if (off.getOffOfColor((player2.color())).size() == numberOfCheckersOffToWin) {
            winner = player2;
            loser = player1;
        } else {
            return null;    // Neither player has won
        }

        if (isBackgammon(board, loser)) {
            return new GameWinner(winner, doubleDice.getMultiplier(), EndingType.BACKGAMMON);
        }

        if (isGammon(board, loser)) {
            return new GameWinner(winner, doubleDice.getMultiplier(), EndingType.GAMMON);
        }

        return new GameWinner(winner, doubleDice.getMultiplier(), EndingType.SINGLE);
    }

    /**
     * Checks if the losing player has been backgammoned.
     * A backgammon occurs if the losing player has no checkers borne off and
     * still has checkers on the bar or in the opponent's home board.
     *
     * @param board the current game board
     * @param loser the losing player
     * @return {@code true} if the player has been backgammoned, {@code false} otherwise
     */
    private static boolean isBackgammon(Board board, Player loser) {
        // If losing player has borne off any checkers it is not a backgammon
        if (!board.getOff().getOffOfColor(loser.color()).isEmpty()) return false;

        // If losing player has checkers on the bar it is a backgammon
        if (!board.getBar().getBarOfColor(loser.color()).isEmpty()) return true;

        // Reverse points order if the loser is Blue
        List<Point> points = new ArrayList<>(board.getPoints());
        if (loser.color() == Color.BLUE) {
            Collections.reverse(points);
        }

        // If losing player has checkers in the opponents home board it is a backgammon
        List<Point> opponentsHome = points.subList(0, 6);
        for (Point point : opponentsHome) {
            if (point.hasCheckers() && point.getTopCheckerColor() == loser.color()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the losing player has been gammoned.
     * A gammon occurs if the losing player has no checkers borne off.
     *
     * @param board the current game board
     * @param loser the losing player
     * @return {@code true} if the player has been gammoned, {@code false} otherwise
     */
    private static boolean isGammon(Board board, Player loser) {
        return board.getOff().getOffOfColor(loser.color()).isEmpty();
    }
}
