/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.main;

import backgammon.gameLogic.Match;
import backgammon.view.View;

/**
 * The entry point for the backgammon application.
 * Initializes the view, displays the welcome message, and starts a new match.
 */
public class App {

    /**
     * The main method that launches the application.
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        View view = new View();

        // Display a welcome massage to the user
        view.displayWelcomeMessage();

        // Initialize and start a new match
        Match match = new Match(view);
        match.start();
    }
}
