package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.player.Player;
import backgammon.view.View;

import java.util.List;

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
        List<String> playerNames = view.retrievePlayerNames();
        player1 = new Player(playerNames.getFirst());
        player2 = new Player(playerNames.getLast());

        winThreshold = view.retrieveWinThreshold();
    }

    public void start() {
        Game game = new Game(view);
        game.play();
    }
}
