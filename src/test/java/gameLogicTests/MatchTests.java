package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.board.Color;
import backgammon.player.Player;
import backgammon.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MatchTests {
    private TestableMatch match;
    private MockView view;
    Player player1;
    Player player2;

    @BeforeEach
    void setUp() {
        view = new MockView();
        match = new TestableMatch(view);

        // Using players from match initialization
        player1 = match.player1;
        player2 = match.player2;

        // Set the default win threshold
        match.winThreshold = 5;
    }


    @Test
    void should_UpdateScoreAndDeclareWinner_When_PlayerReachesWinThreshold() {
        GameWinner gameWinner = new GameWinner(player1, 5, EndingType.SINGLE);
        match.updateMatchScore(gameWinner);

        assertEquals(5, match.matchScore.get(player1));
        assertTrue(match.matchOver);
        assertEquals(player1, match.matchWinner);
    }

    @Test
    void should_UpdateScoreAndContinue_When_NoPlayerReachesWinThreshold() {
        GameWinner gameWinner = new GameWinner(player2, 3, EndingType.SINGLE);
        match.updateMatchScore(gameWinner);

        assertEquals(3, match.matchScore.get(player2));
        assertFalse(match.matchOver);
        assertNull(match.matchWinner);
    }

    @Test
    void should_EndMatchWithNoWinner_When_GameWinnerIsNull() {
        match.updateMatchScore(null);

        assertTrue(match.matchOver);
        assertNull(match.matchWinner);
    }

    @Test
    void should_DisplayEndMatchMessage_WithNoWinner() {
        match.matchWinner = null;
        match.matchOver = true;

        match.displayEndMatchMessage();

        assertTrue(view.getDisplayedMessages().contains("Match was quit"));
    }

    @Test
    void should_DisplayEndMatchMessage_WithWinner() {
        match.matchWinner = player1;
        match.matchOver = true;

        match.displayEndMatchMessage();

        boolean hasWinMessage = view.getDisplayedMessages().stream()
                .anyMatch(msg -> msg.contains("won the match!") && msg.contains("Alice"));
        assertTrue(hasWinMessage);
    }


    @Test
    void should_DisplayGameResult_ForDoubleRefused() {
        GameWinner gameWinner = new GameWinner(player1, 2, EndingType.DOUBLE_REFUSED);
        match.displayGameResult(gameWinner);

        assertTrue(view.getDisplayedMessages().contains("Game ended because the double was refused"));
    }

    @Test
    void should_DisplayGameResult_ForGammonWin() {
        GameWinner gameWinner = new GameWinner(player1, 2, EndingType.GAMMON);
        match.displayGameResult(gameWinner);

        assertTrue(view.getDisplayedMessages().contains("Game ended in a Gammon"));
        assertTrue(view.getDisplayedMessages().contains("Press ENTER to continue to the next game"));
    }



    @Test
    void should_StartMatchAndEnd_When_WinnerIsFoundImmediately() {
        view.setPromptStartNewMatch(true, 1); // Start one match
        view.setWinThreshold(1);
        match.winThreshold = 1;
        match.setGameFactory((v, p1, p2, score, threshold) ->
                new StubGame(new GameWinner(match.player1, 1, EndingType.SINGLE)));

        match.start();

        assertTrue(match.matchOver);
        assertEquals(match.player1, match.matchWinner);
        boolean hasWinnerMessage = view.getDisplayedMessages().stream()
                .anyMatch(msg -> msg.contains("won the match!") && msg.contains(match.player1.name()));
        assertTrue(hasWinnerMessage);
    }

    @Test
    void should_StartMatchAndEndWithoutWinner_When_GamePlayReturnsNull() {
        match.setGameFactory((v, p1, p2, score, threshold) -> new StubGame(null));

        match.start();

        assertTrue(match.matchOver);
        assertNull(match.matchWinner);
        assertTrue(view.getDisplayedMessages().contains("Match was quit"));
    }


    /**
     * Test to verify multiple sequential game updates leading to a match win.
     */
    @Test
    void should_UpdateScoreOverMultipleGames_ToReachWinThreshold() {
        // Simulate five games where player1 wins 1 point each time
        for (int i = 1; i <= 5; i++) { // 5 games to reach threshold of 5
            GameWinner gameWinner = new GameWinner(player1, 1, EndingType.SINGLE);
            match.updateMatchScore(gameWinner);
            if (i < 5) {
                assertFalse(match.matchOver, "Match should not be over after game " + i);
                assertNull(match.matchWinner, "No winner should be declared after game " + i);
            } else {
                assertTrue(match.matchOver, "Match should be over after game " + i);
                assertEquals(player1, match.matchWinner, "Player1 should be the winner after game " + i);
            }
        }
    }

    /**
     * Test to verify that resetMatch correctly reinitializes the match,
     * allowing for a new sequence of games.
     */
    @Test
    void should_ResetMatch_And_StartNewSequence() {
        // Player1 wins 5 points to end the match
        GameWinner gameWinner = new GameWinner(player1, 5, EndingType.SINGLE);
        match.updateMatchScore(gameWinner);
        assertTrue(match.matchOver);
        assertEquals(player1, match.matchWinner);

        // Reset the match
        match.resetMatch();
        assertFalse(match.matchOver);
        assertNull(match.matchWinner);
        assertEquals(0, match.matchScore.get(match.player1));
        assertEquals(0, match.matchScore.get(match.player2));

        // Player2 wins 5 points to end the new match
        GameWinner gameWinner2 = new GameWinner(match.player2, 5, EndingType.GAMMON);
        match.updateMatchScore(gameWinner2);
        assertTrue(match.matchOver);
        assertEquals(match.player2, match.matchWinner);
    }

    /**
     * New Test: Verify that updateMatchScore continues correctly when pointsWon is zero.
     */
    @Test
    void should_UpdateScoreAndContinue_When_PointsWonIsZero() {
        GameWinner gameWinner = new GameWinner(player1, 0, EndingType.SINGLE);
        match.updateMatchScore(gameWinner);

        assertEquals(0, match.matchScore.get(player1));
        assertFalse(match.matchOver);
        assertNull(match.matchWinner);
    }

    /**
     * New Test: Verify that Match initializes correctly upon construction.
     */
    @Test
    void should_InitializeMatchCorrectly_When_Constructed() {
        // Verify players are initialized with correct names and colors
        assertEquals("Alice", match.player1.name());
        assertEquals(Color.BLUE, match.player1.color());

        assertEquals("Bob", match.player2.name());
        assertEquals(Color.RED, match.player2.color());

        // Verify match scores are initialized to 0
        assertEquals(0, match.matchScore.get(match.player1));
        assertEquals(0, match.matchScore.get(match.player2));

        // Verify win threshold is set correctly
        assertEquals(5, match.winThreshold);

        // Verify match is not over initially
        assertFalse(match.matchOver);
        assertNull(match.matchWinner);
    }

    /**
     * New Test: Verify that updateMatchScore handles negative points correctly.
     * Assuming the game allows negative points.
     * If not, adjust the test accordingly.
     */
    @Test
    void should_HandleNegativePointsWon_Correctly() {
        GameWinner gameWinner = new GameWinner(player1, -2, EndingType.SINGLE);
        match.updateMatchScore(gameWinner);

        // Verify that the match is not over
        assertFalse(match.matchOver);
        assertNull(match.matchWinner);

        // Verify that match score is updated correctly (decremented)
        assertEquals(-2, match.matchScore.get(player1));
    }


    /**
     * New Test: Verify that setupMatch retrieves player names correctly from View.
     */
    @Test
    void should_SetupMatch_RetrievePlayerNamesCorrectly() {
        // Modify the MockView to return different player names
        view.setPlayerNames(Map.of(Color.BLUE, "Charlie", Color.RED, "Dana"));
        match.resetMatch();

        // Verify that players are updated
        assertEquals("Charlie", match.player1.name());
        assertEquals("Dana", match.player2.name());
    }

    /**
     * New Test: Verify that setupMatch handles duplicate player names correctly.
     */
    @Test
    void should_HandleDuplicatePlayerNames_InSetupMatch() {
        // Modify the MockView to return duplicate names
        view.setPlayerNames(Map.of(Color.BLUE, "Eve", Color.RED, "Eve"));
        match.resetMatch();

        // For example, ensure that both players have the same name
        assertEquals("Eve", match.player1.name());
        assertEquals("Eve", match.player2.name());
    }

    /**
     * New Test: Verify that setupMatch retrieves win threshold correctly.
     */
    @Test
    void should_SetupMatch_RetrieveWinThresholdCorrectly() {
        // Modify the MockView to return a different win threshold
        view.setWinThreshold(10);
        match.resetMatch();

        assertEquals(10, match.winThreshold);
    }

    // **Helper Classes**

    /**
     * MockView that simulates user input and stores displayed messages.
     * Overrides getInput() to avoid blocking (e.g., when pressing ENTER).
     */
    private static class MockView extends View {
        private final List<String> displayedMessages = new ArrayList<>();
        private Map<Color, String> playerNames = Map.of(Color.BLUE, "Alice", Color.RED, "Bob");
        private int winThreshold = 5;
        private boolean promptStartNewMatch = false;
        private int restartCount = 0;

        @Override
        public void display(String message) {
            displayedMessages.add(message);
        }

        @Override
        public Map<Color, String> retrievePlayerNames() {
            return playerNames;
        }

        @Override
        public int retrieveWinThreshold() {
            return winThreshold;
        }

        public void setWinThreshold(int threshold) {
            this.winThreshold = threshold;
        }

        public void setPlayerNames(Map<Color, String> names) {
            this.playerNames = names;
        }

        /**
         * Sets whether to prompt for a new match and how many times to allow restarts.
         *
         * @param val          whether to prompt for a new match
         * @param restartCount number of times to allow restarting
         */
        public void setPromptStartNewMatch(boolean val, int restartCount) {
            this.promptStartNewMatch = val;
            this.restartCount = restartCount;
        }

        @Override
        public boolean promptStartNewMatch() {
            if (restartCount > 0) {
                restartCount--;
                return true;
            }
            return false;
        }

        public List<String> getDisplayedMessages() {
            return displayedMessages;
        }

        @Override
        public String getInput() {
            return "";  // Simulate pressing ENTER to avoid blocking
        }

        @Override
        public void displayMatchQuitMessage() {
            display("Match was quit");
        }

        @Override
        public void displayMatchWinMessage(Player winner) {
            display(winner.name() + " won the match!");
        }

        @Override
        public void displaySingleWin() {
            display("Game ended in a Single");
        }

        @Override
        public void displayDoubleRefused() {
            display("Game ended because the double was refused");
        }

        @Override
        public void displayGammonWin() {
            display("Game ended in a Gammon");
            display("Press ENTER to continue to the next game");
        }

        @Override
        public void displayBackgammonWin() {
            display("Game ended in a Backgammon");
        }

        @Override
        public void displayGameResult(Player winner, int pointsWon) {
            display("Player " + winner.name() + " won " + pointsWon + " points.");
        }
    }

    /**
     * StubGame returns a predefined GameWinner or null.
     */
    private static class StubGame extends Game {
        private final GameWinner gameWinner;

        public StubGame(GameWinner gameWinner) {
            super(null, null, null, null, 0); // Pass nulls for dependencies
            this.gameWinner = gameWinner;
        }

        @Override
        public GameWinner play() {
            return gameWinner;
        }
    }

    /**
     * TestableMatch allows injecting a GameFactory.
     */
    private static class TestableMatch extends Match {
        private GameFactory gameFactory;

        public TestableMatch(View view) {
            super(view);
        }

        @Override
        public void start() {
            do {
                Game game = (gameFactory != null) ?
                        gameFactory.create(view, player1, player2, matchScore, winThreshold)
                        : new Game(view, player1, player2, matchScore, winThreshold);

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

        public void setGameFactory(GameFactory factory) {
            this.gameFactory = factory;
        }
    }

    @FunctionalInterface
    interface GameFactory {
        Game create(View v, Player p1, Player p2, Map<Player, Integer> score, int threshold);
    }
}
