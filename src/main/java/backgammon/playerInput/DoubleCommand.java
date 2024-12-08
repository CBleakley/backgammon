/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.playerInput;

/**
 * Represents a command for offering a double in the Backgammon game.
 * Implements the {@code PlayerInput} interface.
 */
public class DoubleCommand implements PlayerInput {

    /**
     * Parses a string to determine if it represents a double command.
     *
     * @param string the input string to parse
     * @return a new {@code DoubleCommand} instance if the string represents a double command,
     *         or {@code null} if it does not
     */
    public static DoubleCommand parse(String string) {
        if (string == null) return null;
        return (string.equalsIgnoreCase("double")) ? new DoubleCommand() : null;
    }
}
