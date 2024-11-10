package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.board.Point;
import backgammon.board.Color;
import backgammon.board.Checker;

import java.util.List;

public class PipCounter {
    static public int calculatePipCount(Board board, Color color) {
        int pipCount = 0;
        List<Point> points = board.getPoints();

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

        return pipCount;
    }
}
