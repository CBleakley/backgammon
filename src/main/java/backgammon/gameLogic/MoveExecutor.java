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
        Checker movingChecker;

        // If the move is from the bar, take the checker from the bar
        if (fromPointIndex == -3) {  // Using -3 as a flag for moves from the bar
            if (board.getBar().getBarOfColor(playerColor).isEmpty()) {
                System.out.println("Error: No checker available in bar for player color " + playerColor);
                return;
            }
            movingChecker = board.getBar().getBarOfColor(playerColor).pop();
        } else {
            Point fromPoint = points.get(fromPointIndex);

            // Check if fromPoint has any checkers to move
            if (!fromPoint.hasCheckers() || fromPoint.getTopCheckerColor() != playerColor) {
                System.out.println("Error: No checker of color " + playerColor + " at point " + fromPointIndex);
                return;
            }

            movingChecker = fromPoint.removeTopChecker();
        }

        // Bearing off logic
        if (toPointIndex == -1 || toPointIndex == -2) {
            board.getOff().addChecker(movingChecker);
            return;
        }

        // Check if toPointIndex is within board bounds
        if (toPointIndex < 0 || toPointIndex >= points.size()) {
            System.out.println("Error: toPointIndex " + toPointIndex + " is out of bounds.");
            return;
        }

        // Normal move within the board
        Point toPoint = points.get(toPointIndex);

        // Check if we are hitting an opponent's checker
        if (!toPoint.hasCheckers()) {
            toPoint.addChecker(movingChecker);
            return;
        }

        if (toPoint.getTopCheckerColor() == playerColor) {
            toPoint.addChecker(movingChecker);
        } else if (toPoint.getNumberOfCheckers() == 1) {
            // Hit opponent's single checker
            Checker opponentChecker = toPoint.removeTopChecker();
            board.getBar().addChecker(opponentChecker);
            toPoint.addChecker(movingChecker);
        } else {
            System.out.println("Error: Move to point " + toPointIndex + " blocked by opponent.");
        }
    }
}
