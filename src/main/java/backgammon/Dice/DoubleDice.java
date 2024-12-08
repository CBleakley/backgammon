/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.Dice;

import backgammon.player.Player;

/**
 * Represents the doubling die used in a backgammon game.
 * The doubling die is used to track ownership and the multiplier of the stakes in the game.
 */
public class DoubleDice {
    private Player owner;
    private int multiplier;

    /**
     * Initializes a new DoubleDice with no owner and a multiplier of 1.
     */
    public DoubleDice() {
        this.owner = null;
        this.multiplier = 1;
    }

    /**
     * Retrieves the current owner of the doubling die.
     *
     * @return the owner of the doubling die, or {@code null} if it has no owner
     */
    public Player getOwner() { return owner; }

    /**
     * Retrieves the current multiplier of the doubling die.
     *
     * @return the current multiplier
     */
    public int getMultiplier() { return multiplier; }

    /**
     * Sets the owner of the doubling die to the specified player.
     *
     * @param player the player to set as the owner
     */
    public void setOwner(Player player) { owner = player; }

    /**
     * Doubles the multiplier of the doubling die.
     */
    public void updateMultiplier() { multiplier = multiplier * 2; }
}
