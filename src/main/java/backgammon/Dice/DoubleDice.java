/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.Dice;

import backgammon.player.Player;

public class DoubleDice {
    private Player owner;
    private int multiplier;

    public DoubleDice() {
        this.owner = null;
        this.multiplier = 1;
    }

    public Player getOwner() { return owner; }

    public int getMultiplier() { return multiplier; }

    public void setOwner(Player player) { owner = player; }

    public void updateMultiplier() { multiplier = multiplier * 2; }
}
