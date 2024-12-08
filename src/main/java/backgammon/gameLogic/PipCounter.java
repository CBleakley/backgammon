/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.board.Point;
import backgammon.board.Color;
import backgammon.board.Checker;

import java.util.List;

/**
 * Utility class for calculating the pip count in a backgammon game.
 * The pip count represents the total number of moves required to bear off all of a player's checkers.
 */
public class PipCounter {

    /**
     * Calculates the pip count for a given player color.
     * The pip count is the sum of the distances each checker needs to travel to bear off,
     * plus additional pips for checkers on the bar.
     *
     * @param board the current game board
     * @param color the color of the player whose pip count is being calculated
     * @return the total pip count for the specified player
     */
    static public int calculatePipCount(Board board, Color color) {
        int pipCount = 0;
        List<Point> points = board.getPoints();

        // Loop through points to find cumulative pip count off all the color's checkers
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            List<Checker> checkers = point.getCheckers();

            for (Checker checker : checkers) {
                if (checker.getColor() == color) {
                    if (color == Color.RED) {
                        pipCount += (25 - (i + 1)); // Red checkers use 25 - point location
                    } else if (color == Color.BLUE) {
                        pipCount += (i + 1); // Blue checkers use point location directly
                    }
                }
            }
        }

        int checkersOnBar = board.getBar().getBarOfColor(color).size();
        pipCount += checkersOnBar * 25;

        return pipCount;
    }
}
