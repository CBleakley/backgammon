package viewTests;

import backgammon.Dice.DoubleDice;
import backgammon.board.Board;
import backgammon.board.Color;
import backgammon.gameLogic.Move;
import backgammon.player.Player;
import backgammon.playerInput.PlayerInput;
import backgammon.playerInput.RollCommand;
import backgammon.view.BoardFormatting;
import backgammon.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ViewTests {
    private View view;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testDisplay() {
        View view = new View();

        // Test the display method
        String message = "Welcome to Backgammon!";
        view.display(message);

        assertTrue(outputStream.toString().contains(message), "Display should output the correct message");
    }

    @Test
    void testGetInputFromConsole() {
        // Simulate user input
        String simulatedInput = "test input";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        view = new View();

        // Verify getInput reads correctly from console
        String input = view.getInput();
        assertEquals(simulatedInput, input, "getInput should correctly read console input");
    }

    @TempDir
    Path tempDir; // JUnit will manage this directory

    @Test
    void testSetInputSourceToFileUsingTempDir() throws Exception {
        // Create a temporary file in the @TempDir directory
        Path tempFile = tempDir.resolve("testInput.txt");
        Files.writeString(tempFile, "line1\nline2\n");

        view = new View();

        // Set the input source to the temporary file
        view.setInputSource(tempFile.toString());

        // Read and assert file input
        String input = view.getInput();
        assertEquals("line1", input, "First line of the file should be read");

        input = view.getInput();
        assertEquals("line2", input, "Second line of the file should be read");

        // Reset input source to close the file reader
        view.setInputSource(null);

        // Clean up the file
        Files.delete(tempFile);
    }

    @Test
    void testSetInputSourceInvalidFile() {
        view = new View();
        // Test invalid file input
        view.setInputSource("nonexistent_file.txt");

        // Verify error message
        assertTrue(outputStream.toString().contains("Error opening file"), "Should display an error message for invalid file");
    }

    @Test
    void testRetrievePlayerNames() {
        // Simulate user input for player names
        System.setIn(new ByteArrayInputStream("Alice\nBob\n".getBytes()));

        view = new View();

        // Retrieve player names
        Map<Color, String> playerNames = view.retrievePlayerNames();

        // Verify names
        assertEquals("Alice", playerNames.get(Color.BLUE), "Player 1's name should be 'Alice'");
        assertEquals("Bob", playerNames.get(Color.RED), "Player 2's name should be 'Bob'");
    }

    @Test
    void testRetrieveWinThreshold() {
        // Simulate user input for the win threshold
        System.setIn(new ByteArrayInputStream("5\n".getBytes()));

        view = new View();

        // Retrieve win threshold
        int winThreshold = view.retrieveWinThreshold();

        // Verify the win threshold
        assertEquals(5, winThreshold, "Win threshold should be 5");
    }

    @Test
    void testRetrieveWinThresholdInvalidInput() {
        // Simulate invalid input followed by valid input
        System.setIn(new ByteArrayInputStream("invalid\n0\n10\n".getBytes()));

        view = new View();

        // Retrieve win threshold
        int winThreshold = view.retrieveWinThreshold();

        // Verify the win threshold
        assertEquals(10, winThreshold, "Win threshold should be 10 after invalid inputs");
    }

    @Test
    void testDisplayBoard() {
        // Arrange: Create real instances of required classes
        Board board = new Board(); // Default board setup
        DoubleDice doubleDice = new DoubleDice();
        doubleDice.setOwner(new Player( "Olivia", Color.BLUE)); // Set double dice owner
        doubleDice.updateMultiplier(); // Update multiplier to 2

        List<Integer> rollToPlay = List.of(3, 5); // Dice rolls for the current turn
        Player playerToPlay = new Player("Olivia", Color.BLUE);
        Player player1 = new Player("Olivia", Color.BLUE);
        Player player2 = new Player("Olivia", Color.RED);

        view = new View();

        // Act: Display the board
        view.displayBoard(board, rollToPlay, playerToPlay, 12, player1, 0, player2, 0, 15, doubleDice);

        // Assert: Verify key elements of the output
        String output = outputStream.toString();
        assertTrue(output.contains("Olivia"), "Board display should include the player's name");
        assertTrue(output.contains("pip: 12"), "Board display should include the pip count");
        assertTrue(output.contains("3-5"), "Board display should include the dice rolls");
        assertTrue(output.contains("15"), "Board display should include the match length");
        assertTrue(output.contains(BoardFormatting.BLUE_CHECKER), "Board display should include BLUE checkers");
    }

    @Test
    void testPromptStartNewMatch() {
        // Simulate user input
        System.setIn(new ByteArrayInputStream("yes\n".getBytes()));

        view = new View();

        // Call promptStartNewMatch
        boolean startNewMatch = view.promptStartNewMatch();

        // Verify the response
        assertTrue(startNewMatch, "Should return true for 'yes' input");
    }

    @Test
    void testPromptStartNewMatchInvalidInput() {
        // Simulate invalid inputs followed by valid input
        System.setIn(new ByteArrayInputStream("maybe\nno\n".getBytes()));

        view = new View();

        // Call promptStartNewMatch
        boolean startNewMatch = view.promptStartNewMatch();

        // Verify the response
        assertFalse(startNewMatch, "Should return false for 'no' input after invalid input");
    }

    @Test
    void testDisplayWelcomeMessage() {
        view = new View();
        view.displayWelcomeMessage();
        String output = outputStream.toString();
        assertTrue(output.contains("Welcome to Backgammon"), "Output should contain the welcome message");
    }

    @Test
    void testDisplayMatchQuitMessage() {
        view = new View();
        view.displayMatchQuitMessage();
        String output = outputStream.toString();
        assertTrue(output.contains("Match was quit"), "Output should contain the match quit message");
    }

    @Test
    void testDisplayMatchWinMessage() {
        view = new View();
        Player winner = new Player("Olivia", Color.RED);
        view.displayMatchWinMessage(winner);
        String output = outputStream.toString();
        assertTrue(output.contains("\033[31mOlivia\033[0m won the match!"), "Output should contain the winner's name");
    }

    @Test
    void testDisplaySingleWin() {
        view = new View();
        view.displaySingleWin();
        String output = outputStream.toString();
        assertTrue(output.contains("Game ended in a Single"), "Output should contain the single win message");
    }

    @Test
    void testDisplayDoubleRefused() {
        view = new View();
        view.displayDoubleRefused();
        String output = outputStream.toString();
        assertTrue(output.contains("Game ended because the double was refused"), "Output should contain the double refused message");
    }

    @Test
    void testDisplayGammonWin() {
        view = new View();
        view.displayGammonWin();
        String output = outputStream.toString();
        assertTrue(output.contains("Game ended in a Gammon"), "Output should contain the gammon win message");
    }

    @Test
    void testDisplayBackgammonWin() {
        view = new View();
        view.displayBackgammonWin();
        String output = outputStream.toString();
        assertTrue(output.contains("Game ended in a Backgammon"), "Output should contain the backgammon win message");
    }

    @Test
    void testDisplayGameResult() {
        // Simulate user pressing Enter after viewing the game result
        System.setIn(new ByteArrayInputStream("\n".getBytes()));

        View view = new View();

        Player winner = new Player("Olivia", Color.BLUE);
        view.displayGameResult(winner, 5);

        String output = outputStream.toString();
        assertTrue(output.contains("Olivia"), "Output should contain the winner's name");
        assertTrue(output.contains("5"), "Output should contain the points won");
        assertTrue(output.contains("Press ENTER to continue to the next game"), "Output should prompt to press Enter to continue");
    }

    @Test
    void testDisplayRollAgain() {
        View view = new View();
        // Call displayRollAgain
        view.displayRollAgain();

        // Verify the output contains the expected message
        String output = outputStream.toString();
        assertTrue(output.contains("Both players rolled the same, roll again..."), "Output should contain the roll again message");
    }

    @Test
    void testDisplayWhoPlaysFirst() {
        View view = new View();
        // Create a Player instance
        Player player = new Player("Olivia", Color.BLUE);

        // Call displayWhoPlaysFirst
        view.displayWhoPlaysFirst(player);

        // Verify the output contains the correct message
        String output = outputStream.toString();
        assertTrue(output.contains("Olivia"), "Output should contain the player's name");
        assertTrue(output.contains("\033[34mOlivia\033[0m rolled the higher number, and therefore plays first"), "Output should contain the first-to-play message prefix");
    }

    @Test
    void testDisplayPipCount() {
        View view = new View();
        view.displayPipCount(10, 15);

        String output = outputStream.toString();
        assertTrue(output.contains("Red Pip Count: 10"), "Output should display the red pip count");
        assertTrue(output.contains("Blue Pip Count: 15"), "Output should display the blue pip count");
    }

    @Test
    void testGetDoubleDecisionAccept() {
        System.setIn(new ByteArrayInputStream("accept\n".getBytes()));
        view = new View();

        Player player = new Player("Olivia", Color.RED);
        boolean decision = view.getDoubleDecision(player);

        assertTrue(decision, "The decision should be true when the input is 'accept'");
        String output = outputStream.toString();
        assertTrue(output.contains("Olivia"), "Output should include the player's name");
        assertTrue(decision, "Decision should be true indicating accepted");
    }

    @Test
    void testGetDoubleDecisionRefuse() {
        System.setIn(new ByteArrayInputStream("refuse\n".getBytes()));
        view = new View();

        Player player = new Player("Olivia", Color.BLUE);
        boolean decision = view.getDoubleDecision(player);

        assertFalse(decision, "The decision should be false when the input is 'refuse'");
    }

    @Test
    void testCannotOfferDouble() {
        View view = new View();
        Player owner = new Player("Conor", Color.BLUE);
        view.cannotOfferDouble(owner);

        String output = outputStream.toString();
        assertTrue(output.contains("\033[34mConor\033[0m owns the double dice, you cannot offer it"), "Output should say who owns the dice");
    }

    @Test
    void testDisplayHintWithDoubleAvailable() {
        View view = new View();
        view.displayHint(true);

        String output = outputStream.toString();
        assertTrue(output.contains(            """
             The following commands are available:
                "quit"
                "roll"
                "pip"
                "hint"
                """), "Output should contain the hint with double available message");
    }

    @Test
    void testDisplayHintWithoutDoubleAvailable() {
        View view = new View();
        view.displayHint(false);

        String output = outputStream.toString();
        assertTrue(output.contains(            """
             The following commands are available:
                "quit"
                "roll"
                "pip"
                """), "Output should contain the basic hint message");
    }

    @Test
    void testDisplayPossibleMoves() {
        View view = new View();

        List<List<Move>> possibleMoves = List.of(
                List.of(new Move(1, 2, Color.BLUE), new Move(5, 6, Color.BLUE)),
                List.of(new Move( 7, 9, Color.BLUE))
        );
        view.displayPossibleMoves(possibleMoves);

        String output = outputStream.toString();
        assertTrue(output.contains("Possible moves: "), "Output should contain the possible moves title");
        assertTrue(output.contains("Option 1:"), "Output should display the first move option");
        assertTrue(output.contains("2-3"), "Output should display the first move in Option 1");
        assertTrue(output.contains("6-7"), "Output should display the move in Option 1");
        assertTrue(output.contains("Option 2:"), "Output should display the first move option");
        assertTrue(output.contains("8-10"), "Output should display the move in Option 2");

    }

    @Test
    void testDisplayNoMovesAvailable() {
        View view = new View();
        Player player = new Player("Olivia", Color.BLUE);
        view.displayNoMovesAvailable(player);

        String output = outputStream.toString();
        assertTrue(output.contains("Olivia"), "Output should include the player's name");
        assertTrue(output.contains("has no available moves"), "Output should contain the no moves available message prefix");
    }

    @Test
    void testDisplayOnlyOnePossibleMove() {
        View view = new View();
        List<Move> moves = List.of(new Move(1, 2, Color.BLUE));
        view.displayOnlyOnePossibleMove(moves);

        String output = outputStream.toString();
        assertTrue(output.contains("Only one possible move, forced to play: "), "Output should contain the only possible move message");
        assertTrue(output.contains("2-3"), "Output should include the single move available");
    }

    @Test
    void testPromptMoveSelection() {
        System.setIn(new ByteArrayInputStream("2\n".getBytes()));
        view = new View();

        int choice = view.promptMoveSelection(3);

        assertEquals(1, choice, "The selection should be 1-based and converted to 0-based (2 maps to index 1)");
        String output = outputStream.toString();
        assertTrue(output.contains("Chose a move sequence (1 to 3): "), "Output should prompt for move selection");
    }

    @Test
    void testGetPlayerInputValidCommand() {
        // Simulate user input for a valid command
        System.setIn(new ByteArrayInputStream("roll\n".getBytes()));
        view = new View();

        Player player = new Player("Olivia", Color.RED);

        // Call getPlayerInput and verify the result
        PlayerInput input = view.getPlayerInput(player);

        assertTrue(input instanceof RollCommand, "Input should be parsed as RollCommand");
    }

    @Test
    void testGetPlayerInputInvalidCommand() {
        // Simulate invalid command followed by a valid command
        System.setIn(new ByteArrayInputStream("invalid\nroll\n".getBytes()));
        view = new View();

        Player player = new Player("Bob", Color.BLUE);

        // Call getPlayerInput and verify the result
        PlayerInput input = view.getPlayerInput(player);

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid Command: enter \"hint\" to see valid commands\n"), "Output should display an invalid command message");

        assertNotNull(input, "Player input should not be null after entering a valid command");
        assertTrue(input instanceof RollCommand, "Input should be parsed as RollCommand");
    }

    @Test
    void testDisplayInitialRoll() {
        View view = new View();
        // Create a player and roll value
        Player player = new Player("Olivia", Color.BLUE);
        int roll = 5;

        // Call the method to display the initial roll
        view.displayInitialRoll(player, roll);

        // Capture and verify the output
        String output = outputStream.toString();
        assertTrue(output.contains("Olivia"), "Output should contain the player's name");
        assertTrue(output.contains("5"), "Output should contain the roll value");
    }
}
