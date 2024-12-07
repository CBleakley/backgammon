package backgammon.gameLogic;

import backgammon.board.Color;
import backgammon.player.Player;
import backgammon.view.View;

import java.util.HashMap;
import java.util.Map;

public class Match {
    final View view;

    int winThreshold;

    Player player1;
    Player player2;

    Map<Player, Integer> matchScore;

    Player matchWinner;
    boolean matchOver = false;

    public Match(View view) {
        this.view = view;
        setupMatch();
    }

    private void setupMatch() {
        Map<Color, String> playerNames = view.retrievePlayerNames();
        this.player1 = new Player(playerNames.get(Color.BLUE), Color.BLUE);
        this.player2 = new Player(playerNames.get(Color.RED), Color.RED);

        this.winThreshold = view.retrieveWinThreshold();

        this.matchScore = new HashMap<>();
        this.matchScore.put(player1, 0);
        this.matchScore.put(player2, 0);
    }

    public void start() {
        do {
            Game game = new Game(view, player1, player2, matchScore, winThreshold);
            GameWinner gameWinner = game.play();
            updateMatchScore(gameWinner);

            if (matchOver) {
                displayEndMatchMessage();
                if (view.promptStartNewMatch()) {
                    resetMatch();
                } else {
                    break;
                }
            } else {
                displayGameResult(gameWinner);
            }

        } while (!matchOver);
    }

    void updateMatchScore(GameWinner gameWinner) {
        if (gameWinner == null) {
            matchWinner = null;
            matchOver = true;
            return;
        }

        Player winner = gameWinner.getWinner();
        int currentScore = matchScore.get(winner);
        matchScore.put(winner, currentScore + gameWinner.getPointsWon());

        if (matchScore.get(winner) >= winThreshold) {
            matchWinner = winner;
            matchOver = true;
        }
    }

    void displayEndMatchMessage() {
        if (matchWinner == null) {
            view.displayMatchQuitMessage();
            return;
        }
        view.displayMatchWinMessage(matchWinner);
    }

    void resetMatch() {
        setupMatch();
        matchOver = false;
        matchWinner = null;
    }

    void displayGameResult(GameWinner gameWinner) {
        switch (gameWinner.getEndingType()) {
            case SINGLE -> view.displaySingleWin();
            case DOUBLE_REFUSED -> view.displayDoubleRefused();
            case GAMMON -> view.displayGammonWin();
            case BACKGAMMON -> view.displayBackgammonWin();
        }

        view.displayGameResult(gameWinner.getWinner(), gameWinner.getPointsWon());
    }
}
