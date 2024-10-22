package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.player.Player;
import backgammon.view.View;

public class Match {
    private View view;

    private Board board;

    private int winThreshold;

    private Player player1;
    private Player player2;
    public Match (View view) {
        this.view = view;
        setupMatch();
    }

    private void setupMatch() {
        player1 = new Player(view.retrievePlayerName());
        player2 = new Player(view.retrievePlayerName());

        winThreshold = view.retrieveWinThreshold();
    }

    public void start() {
        Game game = new Game(view);
        game.play();
    }
}
