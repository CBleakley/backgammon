/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.playerInput;

public class PipCommand implements PlayerInput {

    public static PipCommand parse(String string) {
        if(string == null) return null;
        return string.equalsIgnoreCase("pip") ? new PipCommand() : null;
    }
}
