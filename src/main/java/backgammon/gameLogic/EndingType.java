/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.gameLogic;

/**
 * Represents the different types of game endings in backgammon and their associated multipliers.
 */
public enum EndingType {
    /** A single win, where the losing player has borne off at least one checker. */
    SINGLE,

    /** A gammon, where the losing player has not borne off any checkers. */
    GAMMON,

    /** A backgammon, where the losing player has not borne off any checkers
     * and still has checkers on the bar or in the winner's home board. */
    BACKGAMMON,
    DOUBLE_REFUSED;

    /**
     * Retrieves the multiplier associated with this ending type.
     *
     * @return the multiplier for the ending type
     */
    public int getMultiplier() {
        return switch (this) {
            case SINGLE -> 1;
            case DOUBLE_REFUSED -> 1;
            case GAMMON -> 2;
            case BACKGAMMON -> 3;
        };
    }
}
