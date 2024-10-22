package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.view.View;

public class Game {
    private View view;
    private Board board;

    public Game(View view) {
        this.view = view;
        this.board = new Board();
    }

    public void play() {
        view.displayBoard(board);
    }
}
