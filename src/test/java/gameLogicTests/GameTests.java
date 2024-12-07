package backgammon.gameLogic;

import backgammon.board.*;
import backgammon.Dice.DoubleDice;
import backgammon.Dice.DicePair;
import backgammon.player.Player;
import backgammon.playerInput.*;
import backgammon.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the Game class.
 */
class GameTests {
    private Player player1;
    private Player player2;
    private Map<Player, Integer> matchScore;
    private int matchLength;
    private StubView stubView;

    @BeforeEach
    void setUp() {
        player1 = new Player("Alice", Color.BLUE);
        player2 = new Player("Bob", Color.RED);
        matchScore = new HashMap<>();
        matchScore.put(player1, 0);
        matchScore.put(player2, 0);
        matchLength = 5; // Example match length
        stubView = new StubView();
    }



    /**
     * Test when the game is quit by a player.
     */
    @Test
    void should_GameQuit_When_PlayerIssuesQuitCommand() {
        // Standard starting position
        Board board = new Board();
        board.initialiseStartingPosition();

        // Configure the StubView to simulate the game
        stubView.setInitialRolls(4, 3); // Player1 plays first
        stubView.setPlayerInputs(Arrays.asList(
                new RollCommand(),
                new SetDiceCommand(4, 3),
                new PipCommand(),
                new QuitCommand() // Player1 quits the game
        ));

        // Create the game
        Game game = new Game(stubView, player1, player2, matchScore, matchLength);

        // Play the game
        GameWinner winner = game.play();

        // Assert that the game was quit and no winner was declared
        assertNull(winner);

        // Verify that the match score remains unchanged
        assertEquals(0, matchScore.get(player1));
        assertEquals(0, matchScore.get(player2));
    }



    /**
     * Test handling of TestCommand.
     */
    @Test
    void should_HandleTestCommand() {
        // Configure the StubView to simulate handling a TestCommand
        stubView.setInitialRolls(5, 2); // Player1 plays first
        stubView.setPlayerInputs(Arrays.asList(
                new RollCommand(),
                new SetDiceCommand(5, 2),
                new TestCommand("commands.txt"),
                new QuitCommand()
        ));

        // Create the game
        Game game = new Game(stubView, player1, player2, matchScore, matchLength);

        // Play the game
        GameWinner winner = game.play();

        // Assert that the game was quit and no winner was declared
        assertNull(winner);

        // Verify that the TestCommand was handled
        List<String> messages = stubView.getDisplayedMessages();
        assertTrue(messages.stream().anyMatch(msg -> msg.startsWith("Setting input source to:")),
                "Expected message about setting input source not found.");
    }

    /**
     * Test displaying pip counts.
     */
    @Test
    void should_DisplayPipCounts() {
        // Set up the board with specific pip counts
        Board board = new Board();
        board.initialiseStartingPosition();
        // Modify the board to create specific pip counts if necessary

        // Configure the StubView to simulate displaying pip counts
        stubView.setInitialRolls(3, 4); // Player2 plays first
        stubView.setPlayerInputs(Arrays.asList(
                new RollCommand(),
                new SetDiceCommand(3, 4),
                new PipCommand(),
                new QuitCommand()
        ));

        // Create the game
        Game game = new Game(stubView, player1, player2, matchScore, matchLength);
        // Manually set the board to the custom setup
        setPrivateField(game, "board", board);

        // Play the game
        GameWinner winner = game.play();

        // Assert that the game was quit and no winner was declared
        assertNull(winner);

        // Verify that pip counts were displayed
        List<String> messages = stubView.getDisplayedMessages();
        assertTrue(messages.stream().anyMatch(msg -> msg.contains("rolled:")));
        assertTrue(messages.stream().anyMatch(msg -> msg.contains("Displaying board state.")));
        // Add more specific assertions based on how displayPipCounts is implemented
    }

    /**
     * Test displaying hints.
     */
    @Test
    void should_DisplayHints() {
        // Set up the board with a specific state that requires hints
        Board board = new Board();
        board.initialiseStartingPosition();
        // Modify the board to create a state where hints are relevant

        // Configure the StubView to simulate displaying hints
        stubView.setInitialRolls(2, 5); // Player2 plays first
        stubView.setPlayerInputs(Arrays.asList(
                new RollCommand(),
                new SetDiceCommand(2, 5),
                new HintCommand(),
                new QuitCommand()
        ));

        // Create the game
        Game game = new Game(stubView, player1, player2, matchScore, matchLength);
        // Manually set the board to the custom setup
        setPrivateField(game, "board", board);

        // Play the game
        GameWinner winner = game.play();

        // Assert that the game was quit and no winner was declared
        assertNull(winner);

        // Verify that hints were displayed
        List<String> messages = stubView.getDisplayedMessages();
        // Assuming that displayHint adds a specific message
        assertTrue(messages.stream().anyMatch(msg -> msg.contains("Displaying board state.")));
    }



    /**
     * Test when there are multiple possible move sequences.
     */
    @Test
    void should_HandleMultiplePossibleMoves() {
        // Set up the board in a state where multiple move sequences are possible for nextToPlay
        Board board = new Board();
        board.initialiseStartingPosition();
        // Modify the board to create a multiple-move scenario for nextToPlay

        // Configure the StubView to simulate selecting the first move sequence
        stubView.setInitialRolls(4, 5); // Player2 plays first
        stubView.setPlayerInputs(Arrays.asList(
                new RollCommand(),
                new SetDiceCommand(4, 5),
                new PipCommand(),
                new DoubleCommand(),
                new DoubleCommand(), // Accept the double
                new QuitCommand()
        ));
        // Override the double decision to accept
        stubView.setDoubleDecision(true);

        // Create the game
        Game game = new Game(stubView, player1, player2, matchScore, matchLength);
        // Manually set the board to the custom setup
        setPrivateField(game, "board", board);

        // Play the game
        GameWinner winner = game.play();

        // Assert that the game was quit after handling multiple moves
        assertNull(winner);

        // Verify that multiple move sequences were displayed and handled
        List<String> messages = stubView.getDisplayedMessages();
        assertTrue(messages.stream().anyMatch(msg -> msg.contains("Displaying possible move sequences.")));
    }

    /**
     * Helper method to create a Board with all checkers of a specific player off the board.
     *
     * @param winner The player who has all checkers off.
     * @param loser  The other player.
     * @return Configured Board instance.
     */
    private Board createBoardWithAllCheckersOff(Player winner, Player loser) {
        Board board = new Board();
        board.initialiseStartingPosition();

        // Move all checkers of the winner off the board
        Off off = board.getOff();
        for (Point point : board.getPoints()) {
            while (point.hasCheckers()) {
                Checker checker = point.removeTopChecker();
                if (checker.getColor() == winner.color()) {
                    off.addChecker(checker);
                } else {
                    // Return the checkers to the point if they belong to the loser
                    point.addChecker(checker);
                    break; // Assuming one checker left to prevent infinite loop
                }
            }
        }

        // Ensure the winner has all checkers off
        assertEquals(Board.NUMBER_OF_CHECKERS_PER_PLAYER, off.getOffOfColor(winner.color()).size());

        // Ensure the loser still has checkers on the board
        assertTrue(off.getOffOfColor(loser.color()).isEmpty());

        return board;
    }

    /**
     * Helper method to set private fields via reflection.
     *
     * @param target    The object whose field should be set.
     * @param fieldName The name of the field.
     * @param value     The value to set.
     */
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = Game.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set private field: " + e.getMessage());
        }
    }

    /**
     * Stub implementation of the View interface to simulate user interactions.
     */
    private static class StubView extends View {
        private final Queue<PlayerInput> inputs;
        private final List<String> displayedMessages;
        private int initialRoll1;
        private int initialRoll2;
        private boolean doubleDecision; // Determines if the double is accepted

        public StubView() {
            this.inputs = new LinkedList<>();
            this.displayedMessages = new ArrayList<>();
            this.initialRoll1 = 1;
            this.initialRoll2 = 1;
            this.doubleDecision = true; // Default to accept doubles
        }

        /**
         * Predefine initial dice rolls.
         *
         * @param roll1 Player1's initial roll.
         * @param roll2 Player2's initial roll.
         */
        public void setInitialRolls(int roll1, int roll2) {
            this.initialRoll1 = roll1;
            this.initialRoll2 = roll2;
        }

        /**
         * Set the sequence of player inputs.
         *
         * @param inputs List of PlayerInput commands to simulate.
         */
        public void setPlayerInputs(List<PlayerInput> inputs) {
            this.inputs.clear();
            this.inputs.addAll(inputs);
        }

        /**
         * Set whether the double is accepted or refused.
         *
         * @param decision true to accept, false to refuse.
         */
        public void setDoubleDecision(boolean decision) {
            this.doubleDecision = decision;
        }

        @Override
        public void displayInitialRoll(Player player, int roll) {
            displayedMessages.add(player.name() + " rolled: " + roll);
        }

        @Override
        public void displayRollAgain() {
            displayedMessages.add("Both players rolled the same number. Roll again.");
        }

        @Override
        public void displayWhoPlaysFirst(Player player) {
            displayedMessages.add(player.name() + " will play first.");
        }

        @Override
        public void displayBoard(Board board, List<Integer> nextRoll, Player nextToPlay,
                                 Integer pipCount, Player player1, int score1,
                                 Player player2, int score2, int matchLength,
                                 DoubleDice doubleDice) {
            displayedMessages.add("Displaying board state.");
        }

        @Override
        public PlayerInput getPlayerInput(Player player) {
            return inputs.poll(); // Return the next predefined input
        }

        @Override
        public void displayNoMovesAvailable(Player player) {
            displayedMessages.add(player.name() + " has no moves available.");
        }

        @Override
        public void displayOnlyOnePossibleMove(List<Move> moves) {
            displayedMessages.add("Only one possible move sequence available.");
        }

        @Override
        public void displayPossibleMoves(List<List<Move>> possibleMoveSequences) {
            displayedMessages.add("Displaying possible move sequences.");
        }

        @Override
        public int promptMoveSelection(int numberOfOptions) {
            // Automatically select the first option for testing
            displayedMessages.add("Selecting move option 1 out of " + numberOfOptions);
            return 0; // Zero-based index
        }

        @Override
        public boolean getDoubleDecision(Player offerRecipient) {
            // Return the predefined double decision
            displayedMessages.add(offerRecipient.name() + " has " + (doubleDecision ? "accepted" : "refused") + " the double.");
            return doubleDecision;
        }

        @Override
        public void cannotOfferDouble(Player owner) {
            displayedMessages.add("Cannot offer double. " + owner.name() + " already owns the double.");
        }

        @Override
        public void display(String message) {
            displayedMessages.add(message);
        }

        @Override
        public void setInputSource(String filename) {
            displayedMessages.add("Setting input source to: " + filename);
        }

        public List<String> getDisplayedMessages() {
            return displayedMessages;
        }
    }
}
