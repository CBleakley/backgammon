/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.playerInput;

/**
 * Represents a command for requesting the pip count in the Backgammon game.
 * Implements the {@code PlayerInput} interface.
 */
public class PipCommand implements PlayerInput {

    /**
     * Parses a string to determine if it represents a pip count command.
     *
     * @param string the input string to parse
     * @return a new {@code PipCommand} instance if the string represents a pip count command,
     *         or {@code null} if it does not
     */
    public static PipCommand parse(String string) {
        if(string == null) return null;
        return string.equalsIgnoreCase("pip") ? new PipCommand() : null;
    }
}
