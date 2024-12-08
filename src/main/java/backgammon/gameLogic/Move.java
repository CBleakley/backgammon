/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.board.Color;

/**
 * Represents a single move in a backgammon game.
 * A move is defined by the starting point, the ending point, the player's color, and the dice value used.
 */
public class Move {
    private final int fromPoint;
    private final int toPoint;
    private final Color playerColor;
    private final int diceUsed;

    /**
     * Creates a new Move with the specified parameters.
     *
     * @param fromPoint   the point from which the move starts (-3 represents the bar)
     * @param toPoint     the point to which the move ends (-1 or -2 represents the off area)
     * @param playerColor the color of the player making the move
     * @param diceUsed    the dice value used for the move
     */
    public Move(int fromPoint, int toPoint, Color playerColor, int diceUsed) {
        this.fromPoint = fromPoint;
        this.toPoint = toPoint;
        this.playerColor = playerColor;
        this.diceUsed = diceUsed;
    }

    /**
     * Retrieves the starting point of the move.
     *
     * @return the starting point of the move
     */
    public int getFromPoint() {
        return fromPoint;
    }

    /**
     * Retrieves the ending point of the move.
     *
     * @return the ending point of the move
     */
    public int getToPoint() {
        return toPoint;
    }

    /**
     * Retrieves the color of the player making the move.
     *
     * @return the player's color
     */
    public Color getPlayerColor() {
        return playerColor;
    }

    /**
     * Retrieves the dice value used for the move.
     *
     * @return the dice value used
     */
    public int getDiceUsed() { return diceUsed; }

    /**
     * Returns a string representation of the move, indicating the points involved.
     * Converts bar and off points to user-friendly labels.
     *
     * @return a string representation of the move
     */
    @Override
    public String toString() {
        String from;
        if (fromPoint == Board.BAR_FLAG) {
            from = "Bar";
        } else {
            from = (playerColor == Color.BLUE) ? String.valueOf(fromPoint + 1) : String.valueOf(24 - fromPoint);
        }

        String to;
        if (toPoint == Board.BLUE_OFF_FLAG || toPoint == Board.RED_OFF_FLAG) {
            to = "Off";
        } else {
            to = (playerColor == Color.BLUE) ? String.valueOf(toPoint + 1) : String.valueOf(24 - toPoint);
        }

        return from + "-" + to;
    }

    /**
     * Checks whether this move is equal to another object.
     * Two moves are considered equal if their starting point, ending point, and player color are the same.
     *
     * @param o the object to compare with
     * @return {@code true} if the moves are equal, {@code false} otherwise
     */
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