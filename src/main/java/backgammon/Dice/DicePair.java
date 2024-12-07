/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.Dice;

import java.util.ArrayList;
import java.util.List;

public class DicePair {
    private final Die die1;
    private final Die die2;

    public DicePair() {
        this.die1 = new Die();
        this.die2 = new Die();
    }

    public List<Integer> roll() {
        int die1Result = die1.roll();
        int die2Result = die2.roll();

        List<Integer> result = new ArrayList<>();

        result.add(die1Result);
        result.add(die2Result);
        return result;
    }

}
