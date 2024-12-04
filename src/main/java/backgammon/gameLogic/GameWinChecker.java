package backgammon.gameLogic;

import backgammon.Dice.DoubleDice;
import backgammon.board.*;
import backgammon.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameWinChecker {

    public static GameWinner checkGameWon(Board board, DoubleDice doubleDice, Player player1, Player player2) {
        int numberOfCheckersOffToWin = Board.NUMBER_OF_CHECKERS_PER_PLAYER;

        Off off = board.getOff();
        if (off.getOffOfColor(player1.color()).size() == numberOfCheckersOffToWin) {
            if (isBackgammon(board, player2)) {
                return new GameWinner(player1, doubleDice.getMultiplier(), EndingType.BACKGAMMON);
            }

            if (isGammon(board, player2)) {
                return new GameWinner(player1, doubleDice.getMultiplier(), EndingType.GAMMON);
            }

            return new GameWinner(player1, doubleDice.getMultiplier(), EndingType.SINGLE);
        }

        if (off.getOffOfColor((player2.color())).size() == numberOfCheckersOffToWin) {
            if (isBackgammon(board, player1)) {
                return new GameWinner(player2, doubleDice.getMultiplier(), EndingType.BACKGAMMON);
            }

            if (isGammon(board, player1)) {
                return new GameWinner(player2, doubleDice.getMultiplier(), EndingType.GAMMON);
            }

            return new GameWinner(player2, doubleDice.getMultiplier(), EndingType.SINGLE);
        }

        return null;
    }

    private static boolean isBackgammon(Board board, Player loser) {
        if (!board.getOff().getOffOfColor(loser.color()).isEmpty()) return false;

        if (!board.getBar().getBarOfColor(loser.color()).isEmpty()) return true;

        List<Point> points = new ArrayList<>(board.getPoints());
        if (loser.color() == Color.BLUE) {
            Collections.reverse(points);
        }

        List<Point> opponentsHome = points.subList(0, 6);
        for (Point point : opponentsHome) {
            if (point.hasCheckers() && point.getTopCheckerColor() == loser.color()) {
                return true;
            }
        }

        return false;
    }

    private static boolean isGammon(Board board, Player loser) {
        return board.getOff().getOffOfColor(loser.color()).isEmpty();
    }
}
