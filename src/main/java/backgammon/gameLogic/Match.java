package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.board.Color;
import backgammon.player.Player;
import backgammon.view.View;

import java.util.Map;

public class Match {
    private final View view;

    private int winThreshold;

    private Player player1;
    private Player player2;

    public Match (View view) {
        this.view = view;
        setupMatch();
    }

    private void setupMatch() {
        Map<Color, String> playerNames = view.retrievePlayerNames();
        this.player1 = new Player(playerNames.get(Color.BLUE), Color.BLUE);
        this.player2 = new Player(playerNames.get(Color.RED), Color.RED);

        this.winThreshold = view.retrieveWinThreshold();
    }

    public void start() {
        Game game = new Game(view, player1, player2);
        game.play();
    }
}
