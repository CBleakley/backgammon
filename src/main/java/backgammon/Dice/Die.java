/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.Dice;

/**
 * Represents a single die used in a game.
 * The die can be rolled to produce a random face value between 1 and 6.
 */
public class Die {
    private Integer faceValue;

    /**
     * Initializes a new Die with no face value set.
     */
    public Die() {
        this.faceValue = null;
    }

    public Integer getFaceValue() {
        if (faceValue == null) return null;
        return faceValue;
    }

    /**
     * Rolls the die to generate a random face value between 1 and 6.
     *
     * @return the new face value after the roll
     */
    public int roll() {
        faceValue = (int)(Math.random() * 6) + 1;
        return faceValue;
    }
}
