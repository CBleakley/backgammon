package backgammon.gameLogic;

import backgammon.board.Color;

public class Move {
    private final int fromPoint;
    private final int toPoint;
    private final Color playerColor;
    private final int diceUsed;

    public Move(int fromPoint, int toPoint, Color playerColor, int diceUsed) {
        this.fromPoint = fromPoint;
        this.toPoint = toPoint;
        this.playerColor = playerColor;
        this.diceUsed = diceUsed;
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

    public int getDiceUsed() { return diceUsed; }

    @Override
    public String toString() {
        String from;
        if (fromPoint == -3) {
            from = "Bar";
        } else {
            from = (playerColor == Color.BLUE) ? String.valueOf(fromPoint + 1) : String.valueOf(24 - fromPoint);
        }

        String to;
        if (toPoint == -1 || toPoint == -2) {
            to = "Off";
        } else {
            to = (playerColor == Color.BLUE) ? String.valueOf(toPoint + 1) : String.valueOf(24 - toPoint);
        }

        return from + "-" + to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        if (fromPoint != move.fromPoint) return false;
        if (toPoint != move.toPoint) return false;
        return playerColor == move.playerColor;
    }

    @Override
    public int hashCode() {
        int result = fromPoint;
        result = 31 * result + toPoint;
        result = 31 * result + (playerColor != null ? playerColor.hashCode() : 0);
        return result;
    }
}