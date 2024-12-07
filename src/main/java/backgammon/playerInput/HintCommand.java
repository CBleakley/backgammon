/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.playerInput;

public class HintCommand implements PlayerInput{

    public static HintCommand parse(String string) {
        if(string == null) return null;
        return string.equalsIgnoreCase("hint") ? new HintCommand() : null;
    }
}
