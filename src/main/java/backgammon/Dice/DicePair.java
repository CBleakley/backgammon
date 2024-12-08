/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.Dice;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pair of dice used in a backgammon game.
 * Each die is rolled independently, and the results are returned as a list.
 */
public class DicePair {
    private final Die die1;
    private final Die die2;

    /**
     * Initializes a new DicePair with two independent dice.
     */
    public DicePair() {
        this.die1 = new Die();
        this.die2 = new Die();
    }

    /**
     * Rolls both dice and returns their results as a list.
     *
     * @return a list containing the results of the two dice rolls
     */
    public List<Integer> roll() {
        int die1Result = die1.roll();
        int die2Result = die2.roll();

        List<Integer> result = new ArrayList<>();

        result.add(die1Result);
        result.add(die2Result);
        return result;
    }

}
