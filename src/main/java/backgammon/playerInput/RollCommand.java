/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.playerInput;

/**
 * Represents a command for rolling the dice in the Backgammon game.
 * Implements the {@code PlayerInput} interface.
 */
public class RollCommand implements PlayerInput {

    /**
     * Parses a string to determine if it represents a roll command.
     * Accepts both "roll" and "r" as valid inputs for rolling the dice.
     *
     * @param string the input string to parse
     * @return a new {@code RollCommand} instance if the string represents a roll command,
     *         or {@code null} if it does not
     */
    public static RollCommand parse(String string) {
        if(string == null) return null;
        if (string.equalsIgnoreCase("roll")) return new RollCommand();
        if (string.equalsIgnoreCase("r")) return new RollCommand();
        return null;
    }
}
