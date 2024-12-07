/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.playerInput;

public class DoubleCommand implements PlayerInput {
    public static DoubleCommand parse(String string) {
        if (string == null) return null;
        return (string.equalsIgnoreCase("double")) ? new DoubleCommand() : null;
    }
}
