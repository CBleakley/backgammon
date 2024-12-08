/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.gameLogic;

import backgammon.board.Color;
import backgammon.player.Player;
import backgammon.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a backgammon match consisting of multiple games.
 * Manages players, match scores, and the overall match state.
 */
public class Match {
    final View view;

    int winThreshold;

    Player player1;
    Player player2;

    Map<Player, Integer> matchScore;

    Player matchWinner;
    boolean matchOver = false;

    /**
     * Initializes a new match and sets up the initial configuration.
     *
     * @param view the {@code View} object used for user interaction
     */
    public Match(View view) {
        this.view = view;
        setupMatch();
    }

    /**
     * Sets up the match by retrieving player names, assigning colors, and setting the win threshold.
     */
    private void setupMatch() {
        Map<Color, String> playerNames = view.retrievePlayerNames();
        this.player1 = new Player(playerNames.get(Color.BLUE), Color.BLUE);
        this.player2 = new Player(playerNames.get(Color.RED), Color.RED);

        this.winThreshold = view.retrieveWinThreshold();

        this.matchScore = new HashMap<>();
        this.matchScore.put(player1, 0);
        this.matchScore.put(player2, 0);
    }

    /**
     * Starts the match, consisting of multiple games.
     * Continues until a player reaches the win threshold or the match is quit.
     */
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

    /**
     * Updates the match score based on the outcome of a game.
     * Checks if a player has reached the win threshold to end the match.
     *
     * @param gameWinner the winner of the game and their points, or {@code null} if the game was quit
     */
    void updateMatchScore(GameWinner gameWinner) {
        // Game winner being null indicated that the match has been quit
        if (gameWinner == null) {
            matchWinner = null;
            matchOver = true;
            return;
        }

        Player winner = gameWinner.getWinner();

        int currentScore = matchScore.get(winner);
        matchScore.put(winner, currentScore + gameWinner.getPointsWon());

        // Check if the game winner has won the overall match
        if (matchScore.get(winner) >= winThreshold) {
            matchWinner = winner;
            matchOver = true;
        }
    }

    /**
     * Displays a message indicating the end of the match.
     * Quit message or the match winner depending on match outcome.
     */
    void displayEndMatchMessage() {
        if (matchWinner == null) {
            view.displayMatchQuitMessage();
            return;
        }
        view.displayMatchWinMessage(matchWinner);
    }

    /**
     * Resets the match to its initial state, allowing a new match to start.
     */
    void resetMatch() {
        setupMatch();
        matchOver = false;
        matchWinner = null;
    }

    /**
     * Displays the result of a single game, including the type of win and the points won.
     *
     * @param gameWinner the winner of the game and their ending type
     */
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
