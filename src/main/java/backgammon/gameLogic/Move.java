package backgammon.gameLogic;

import backgammon.board.Color;

public class Move {
    private final int fromPoint;
    private final int toPoint;
    private final Color playerColor;

    public Move(int fromPoint, int toPoint, Color playerColor) {
        this.fromPoint = fromPoint;
        this.toPoint = toPoint;
        this.playerColor = playerColor;
    }

    public int getFromPoint() {
        return fromPoint;
    }

    public int getToPoint() {
        return toPoint;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    // TODO: move this to view
    @Override
    public String toString() {
        return "Move from " + (fromPoint + 1) + " to " + (toPoint + 1);
    }
}