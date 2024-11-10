package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.board.Checker;
import backgammon.board.Color;
import backgammon.board.Point;

import java.util.List;

public class MoveExecutor {
    public static void executeMove(Board board, Move move) {
        int fromPointIndex = move.getFromPoint();
        int toPointIndex = move.getToPoint();
        Color playerColor = move.getPlayerColor();

        List<Point> points = board.getPoints();
        Point fromPoint = points.get(fromPointIndex);

        Checker movingChecker = fromPoint.removeTopChecker();

        // Bearing off logic
        if (toPointIndex == -1 || toPointIndex == -2) {
            // Add checker to the Off stack for the player's color
            board.getOff().addChecker(movingChecker);
            return;
        }
        // Normal move within the board
        Point toPoint = points.get(toPointIndex);

        // Check if we are hitting an opponent's checker
        if(!toPoint.hasCheckers()) {
            toPoint.addChecker(movingChecker);
            return;
        }

        if(toPoint.getTopCheckerColor() == playerColor) {
            toPoint.addChecker(movingChecker);
            return;
        }

        if (toPoint.getNumberOfCheckers() == 1) {
            Checker opponentChecker = toPoint.removeTopChecker();
            board.getBar().addChecker(opponentChecker);
            toPoint.addChecker(movingChecker);
        }
    }
}
