/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.playerInput;

/**
 * Represents a command for requesting a hint in the Backgammon game.
 * Implements the {@code PlayerInput} interface.
 */
public class HintCommand implements PlayerInput{

    /**
     * Parses a string to determine if it represents a hint command.
     *
     * @param string the input string to parse
     * @return a new {@code HintCommand} instance if the string represents a hint command,
     *         or {@code null} if it does not
     */
    public static HintCommand parse(String string) {
        if(string == null) return null;
        return string.equalsIgnoreCase("hint") ? new HintCommand() : null;
    }
}
