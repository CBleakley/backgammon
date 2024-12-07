/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.main;

import backgammon.gameLogic.Match;
import backgammon.view.View;

public class App {
    public static void main(String[] args) {
        View view = new View();

        view.displayWelcomeMessage();

        Match match = new Match(view);
        match.start();
    }
}
