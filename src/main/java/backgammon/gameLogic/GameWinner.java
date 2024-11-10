package backgammon.gameLogic;

import backgammon.player.Player;

public class GameWinner {
    private final Player winner;
    private final int pointsWon;

    public GameWinner(Player winner, int pointsWon) {
        this.winner = winner;
        this.pointsWon = pointsWon;
    }

    public Player getWinner() { return winner; }

    public int getPointsWon() { return pointsWon; }
}
