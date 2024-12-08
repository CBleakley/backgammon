/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.playerInput;

/**
 * Represents a command for quitting the Backgammon game.
 * Implements the {@code PlayerInput} interface.
 */
public class QuitCommand implements PlayerInput {

    /**
     * Parses a string to determine if it represents a quit command.
     *
     * @param string the input string to parse
     * @return a new {@code QuitCommand} instance if the string represents a quit command,
     *         or {@code null} if it does not
     */
    public static QuitCommand parse(String string) {
        if(string == null) return null;
        return string.equalsIgnoreCase("quit") ? new QuitCommand() : null;
    }
}
