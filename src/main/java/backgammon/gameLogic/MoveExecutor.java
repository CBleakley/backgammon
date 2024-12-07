/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.board.Checker;
import backgammon.board.Color;
import backgammon.board.Point;

import java.util.List;

/**
 * The {@code MoveExecutor} class is responsible for applying a move to the game board.
 * It handles various types of moves, including moving checkers from points on the board,
 * entering checkers from the bar, bearing off checkers, and handling hits on opponent's
 * checkers.
 */
public class MoveExecutor {

    /**
     * Executes a given move on the provided game board.
     * <p>
     * The method handles different move scenarios such as:
     * <ul>
     *     <li>Moving a checker from a point on the board to another point.</li>
     *     <li>Entering a checker from the bar.</li>
     *     <li>Bearing off a checker from the board.</li>
     *     <li>Hitting an opponent's single checker.</li>
     * </ul>
     * </p>
     *
     * @param board the current state of the game board where the move will be executed
     * @param move  the {@code Move} object representing the move to be executed
     */
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
