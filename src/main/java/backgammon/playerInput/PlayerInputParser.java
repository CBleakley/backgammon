/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.playerInput;

public class PlayerInputParser {

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
