/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.playerInput;

/**
 * Utility class for parsing player input commands in the Backgammon game.
 * Determines the appropriate {@code PlayerInput} implementation based on the input string.
 */
public class PlayerInputParser {

    /**
     * Parses a player's input string and returns the corresponding {@code PlayerInput} object.
     * Checks for valid commands in the following order:
     * <ul>
     *     <li>QuitCommand</li>
     *     <li>RollCommand</li>
     *     <li>SetDiceCommand</li>
     *     <li>PipCommand</li>
     *     <li>HintCommand</li>
     *     <li>DoubleCommand</li>
     *     <li>TestCommand</li>
     * </ul>
     *
     * @param input the player's input string
     * @return the corresponding {@code PlayerInput} object if the input is valid, or {@code null} if it is not recognized
     */
    public static PlayerInput parsePlayerInput(String input) {
        QuitCommand quitCommand = QuitCommand.parse(input);
        if (quitCommand != null) return quitCommand;

        RollCommand rollCommand = RollCommand.parse(input);
        if (rollCommand != null) return rollCommand;

        SetDiceCommand setDiceCommand = SetDiceCommand.parse(input);
        if (setDiceCommand != null) return setDiceCommand;

        PipCommand pipCommand = PipCommand.parse(input);
        if (pipCommand != null) return pipCommand;

        HintCommand hintCommand = HintCommand.parse(input);
        if (hintCommand != null) return hintCommand;

        DoubleCommand doubleCommand = DoubleCommand.parse(input);
        if (doubleCommand != null) return doubleCommand;

        TestCommand testCommand = TestCommand.parse(input);
        if (testCommand != null) return testCommand;

        return null;
    }
}
