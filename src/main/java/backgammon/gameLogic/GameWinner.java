/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.gameLogic;

import backgammon.player.Player;

/**
 * Represents the result of a backgammon game, including the winning player,
 * the points won, and the type of ending.
 */
public class GameWinner {
    private final Player winner;
    private final int doubleValue;
    private final EndingType endingType;

    /**
     * Creates a new GameWinner object with the specified winner, double value, and ending type.
     *
     * @param winner     the player who won the game
     * @param pointsWon  the value of the doubling die at the end of the game
     * @param endingType the type of ending (e.g., single, gammon, backgammon)
     */
    public GameWinner(Player winner, int pointsWon, EndingType endingType) {
        this.winner = winner;
        this.doubleValue = pointsWon;
        this.endingType = endingType;
    }

    /**
     * Retrieves the player who won the game.
     *
     * @return the winning player
     */
    public Player getWinner() { return winner; }

    /**
     * Calculates and retrieves the total points won by the winning player.
     * The points are determined by multiplying the doubling die value by the ending type multiplier.
     *
     * @return the total points won
     */
    public int getPointsWon() {
        return doubleValue * endingType.getMultiplier();
    }

    /**
     * Retrieves the type of ending that determined the outcome of the game.
     *
     * @return the ending type
     */
    public EndingType getEndingType() {
        return endingType;
    }
}
