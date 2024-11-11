package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.board.Point;
import backgammon.board.Color;

import java.util.Arrays;
import java.util.Objects;

public class BoardState {
    private final int[][] checkerPositions;

    public BoardState(Board board) {
        checkerPositions = new int[2][24]; // 2 colors (0 for RED, 1 for BLUE) and 24 points

        for (int i = 0; i < board.getPoints().size(); i++) {
            Point point = board.getPoints().get(i);
            if (point.hasCheckers()) {
                if (point.getTopCheckerColor() == Color.RED) {
                    checkerPositions[0][i] = point.getNumberOfCheckers();
                } else if (point.getTopCheckerColor() == Color.BLUE) {
                    checkerPositions[1][i] = point.getNumberOfCheckers();
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardState)) return false;
        BoardState that = (BoardState) o;
        return Arrays.deepEquals(checkerPositions, that.checkerPositions);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(checkerPositions);
    }
}
