/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.playerInput;

public class QuitCommand implements PlayerInput {

    public static QuitCommand parse(String string) {
        if(string == null) return null;
        return string.equalsIgnoreCase("quit") ? new QuitCommand() : null;
    }
}
