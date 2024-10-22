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
