package backgammon.gameLogic;

import backgammon.player.Player;

public class GameWinner {
    private final Player winner;
    private final int doubleValue;
    private final EndingType endingType;

    public GameWinner(Player winner, int pointsWon, EndingType endingType) {
        this.winner = winner;
        this.doubleValue = pointsWon;
        this.endingType = endingType;
    }

    public Player getWinner() { return winner; }

    public int getPointsWon() {
        return doubleValue * endingType.getMultiplier();
    }

    public EndingType getEndingType() {
        return endingType;
    }
}
