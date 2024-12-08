/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.view;

import backgammon.Dice.DoubleDice;
import backgammon.playerInput.*;
import backgammon.board.Board;
import backgammon.board.Color;
import backgammon.gameLogic.Move;
import backgammon.player.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Handles user interaction in the Backgammon game.
 * Provides methods to display messages, retrieve input, and manage input sources.
 */
public class View {
    private Scanner scanner = new Scanner(System.in);
    private BufferedReader fileReader = null;

    /**
     * Displays a message to the console.
     *
     * @param message the message to display
     */
    public void display(String message) {
        System.out.println(message);
    }

    /**
     * Retrieves user input from the current input source (console or file).
     * If the input source is a file and EOF is reached, switches back to console input.
     *
     * @return the user input as a string
     */
    public String getInput() {
        try {
            if (fileReader != null) {
                String line = fileReader.readLine();
                if (line != null) {
                    display("> " + line); // Show the command being executed from the file
                    return line;
                } else {
                    closeFileReader(); // EOF reached, switch back to console input
                }
            }
        } catch (IOException e) {
            display("Error reading from file: " + e.getMessage());
            closeFileReader(); // Ensure fallback to console input
        }
        return scanner.nextLine();
    }

    /**
     * Sets the input source for user commands to either console or a file.
     *
     * @param filename the name of the file to use as input, or {@code null} to reset to console input
     */
    public void setInputSource(String filename) {
        if (filename == null) {
            // Reset to console input
            closeFileReader();
            display("Input source reset to console.");
        } else {
            // Switch to file input
            try {
                fileReader = new BufferedReader(new FileReader(filename));
                display("Switched input source to file: " + filename);
            } catch (IOException e) {
                display("Error opening file: " + e.getMessage());
                System.out.println("Current directory: " + System.getProperty("user.dir"));
                closeFileReader(); // Reset to console input in case of error
            }
        }
    }

    /**
     * Closes the file reader if it is open and resets input to the console.
     */
    private void closeFileReader() {
        if (fileReader != null) {
            try {
                fileReader.close();
            } catch (IOException e) {
                display("Error closing file: " + e.getMessage());
            }
            fileReader = null;
        }
    }

    /**
     * Displays the welcome message for the game.
     */
    public void displayWelcomeMessage() {
        display(Messages.WELCOME_MESSAGE);
    }

    /**
     * Displays the message for quitting a match.
     */
    public void displayMatchQuitMessage() { display(Messages.MATCH_QUIT); }

    /**
     * Displays the message for a player winning the match.
     *
     * @param winner the player who won the match
     */
    public void displayMatchWinMessage(Player winner) {
        String colorCode = getColorANSI(winner.color());
        String name = winner.name();
        display(String.format(Messages.MATCH_WIN, colorCode, name));
    }

    /**
     * Displays a message indicating that the game ended in a Single.
     */
    public void displaySingleWin() { display(Messages.SINGLE_WIN); }

    /**
     * Displays a message indicating that the game ended because the double was refused.
     */
    public void displayDoubleRefused() { display(Messages.DOUBLE_REFUSED); }

    /**
     * Displays a message indicating that the game ended in a Gammon.
     */
    public void displayGammonWin() { display(Messages.GAMMON_WIN); }

    /**
     * Displays a message indicating that the game ended in a Backgammon.
     */
    public void displayBackgammonWin() { display(Messages.BACKGAMMON_WIN); }

    /**
     * Displays the result of a game, including the winner and points won.
     *
     * @param winner    the player who won the game
     * @param pointsWon the number of points won
     */
    public void displayGameResult(Player winner, int pointsWon) {
        display(String.format(Messages.GAME_WINNER, getColorANSI(winner.color()), winner.name(), pointsWon));
        display(Messages.ENTER_TO_CONTINUE);
        getInput();
    }

    /**
     * Retrieves both player names from the user.
     *
     * @return a map containing the names of the players, keyed by their colors
     */
    public Map<Color, String> retrievePlayerNames() {
        Map<Color, String> playerNames = new HashMap<>();
        playerNames.put(Color.BLUE, retrievePlayerName(String.format(Messages.NAME_PROMPT, 1)));
        playerNames.put(Color.RED, retrievePlayerName(String.format(Messages.NAME_PROMPT, 2)));
        return playerNames;
    }

    /**
     * Retrieves a player names from the user.
     *
     * @return a map containing the names of the players, keyed by their colors
     */
    private String retrievePlayerName(String prompt) {
        display(prompt);

        String playerName;
        while (true) {
            playerName = getInput();

            if (!playerName.isEmpty()) {
                return playerName;
            }
            display(Messages.INVALID_NAME);
            display(Messages.PLEASE_TRY_AGAIN);
        }
    }

    /**
     * Retrieves the win threshold for the match from the user.
     *
     * @return the win threshold
     */
    public int retrieveWinThreshold() {
        display(Messages.POINTS_TO_PLAY_TO_PROMPT);

        int winThreshold;
        while (true) {
            try {
                winThreshold = Integer.parseInt(getInput());
                if (winThreshold > 0) {
                    return winThreshold;
                }
                display(Messages.INVALID_POSITIVE);
            } catch (NumberFormatException e) {
                display(Messages.INVALID_INTEGER);
            }
            display(Messages.PLEASE_TRY_AGAIN);
        }
    }

    /**
     * Displays the result of a player's initial roll in the game.
     *
     * @param player the player who rolled
     * @param roll   the result of the player's roll
     */
    public void displayInitialRoll(Player player, int roll) {
        String name = player.name();
        String colorCode = getColorANSI(player.color());
        display(String.format(Messages.INITIAL_ROLL_MESSAGE, colorCode, name, roll));
    }

    /**
     * Displays a message indicating that both players rolled the same number
     * and need to roll again.
     */
    public void displayRollAgain() {
        display(Messages.ROLL_AGAIN);
    }

    /**
     * Displays a message indicating which player rolled the higher number
     * and will play first.
     *
     * @param firstToPlay the player who will play first
     */
    public void displayWhoPlaysFirst(Player firstToPlay) {
        String name = firstToPlay.name();
        String colorCode = getColorANSI(firstToPlay.color());
        display(String.format(Messages.FIRST_TO_PLAY, colorCode, name));
    }

    /**
     * Retrieves the ANSI color code for the specified player color.
     *
     * @param color the color of the player
     * @return the ANSI color code as a string
     */
    private String getColorANSI(Color color) {
        return switch(color) {
            case BLUE -> ColorANSICodes.BLUE;
            case RED -> ColorANSICodes.RED;
        };
    }

    /**
     * Prompts the specified player to enter a command and parses it into a {@code PlayerInput} object.
     * Repeats the prompt until a valid input is provided.
     *
     * @param player the player entering the command
     * @return a {@code PlayerInput} object representing the player's command
     */
    public PlayerInput getPlayerInput(Player player) {
        String name = player.name();
        String colorCode = getColorANSI(player.color());

        PlayerInput playerInput;
        do {
            display(String.format(Messages.PLAYER_INPUT_PROMPT, colorCode, name));
            String commandLineInput = getInput();

            playerInput = parsePlayerInput(commandLineInput);
        } while (playerInput == null);

        return playerInput;
    }

    /**
     * Removes all whitespace characters from the input string.
     *
     * @param input the raw input string
     * @return the cleaned input string with all whitespace removed
     */
    private String cleanInput(String input) {
        return input.replaceAll("\\s", "");
    }

    private PlayerInput parsePlayerInput(String input) {
        String cleanedInput = cleanInput(input);

        PlayerInput playerInput = PlayerInputParser.parsePlayerInput(cleanedInput);
        if (playerInput == null) display(Messages.INVALID_COMMAND);

        return playerInput;
    }

    /**
     * Displays the backgammon board along with additional match information.
     *
     * @param board         the current game board
     * @param rollToPlay    the dice rolls available
     * @param playerToPlay  the player currently taking their turn
     * @param pip           the pip count for the current player
     * @param player1       the first player
     * @param player1Score  the first player's score
     * @param player2       the second player
     * @param player2Score  the second player's score
     * @param matchLength   the match length
     * @param doubleDice    the doubling die
     */
    public void displayBoard(Board board, List<Integer> rollToPlay, Player playerToPlay, Integer pip,
                             Player player1, int player1Score, Player player2, int player2Score, int matchLength, DoubleDice doubleDice) {

        display("\n");
        if (playerToPlay != null) {
            displayMatchInfo(player1, player1Score, player2, player2Score, matchLength);

            // Display board with player-specific details
            String colorCode = getColorANSI(playerToPlay.color());
            String name = playerToPlay.name();
            display("\n" + String.format(Messages.BOARD_TITLE, colorCode, name, pip));

            String boardToDisplay = BoardDisplayBuilder.buildBoard(board, doubleDice, rollToPlay, playerToPlay.color());
            display(boardToDisplay);
        } else {
            displayMatchInfo(player1, player1Score, player2, player2Score, matchLength);

            // Display board without player-specific details
            String boardToDisplay = BoardDisplayBuilder.buildBoard(board, doubleDice, rollToPlay, null);
            display("\n" + boardToDisplay);
        }
    }

    /**
     * Displays match information, including the match length and the current scores of both players.
     *
     * @param player1       the first player
     * @param player1Score  the score of the first player
     * @param player2       the second player
     * @param player2Score  the score of the second player
     * @param matchLength   the total match length (points required to win the match)
     */
    private void displayMatchInfo(Player player1, int player1Score, Player player2, int player2Score, int matchLength) {
        display(String.format(Messages.PLAYING_TO, matchLength));
        display(String.format(Messages.MATCH_SCORE, getColorANSI(player1.color()), player1.name(), player1Score,
                getColorANSI(player2.color()), player2.name(), player2Score));
    }

    /**
     * Displays the pip counts for both players.
     * The pip count represents the total number of moves needed to bear off all checkers for each player.
     *
     * @param redPipCount  the pip count for the red player
     * @param bluePipCount the pip count for the blue player
     */
    public void displayPipCount(int redPipCount, int bluePipCount) {
        display("Red Pip Count: " + redPipCount);
        display("Blue Pip Count: " + bluePipCount + "\n");
    }

    /**
     * Prompts the specified player to make a decision on a double offer.
     * The player can either "accept" or "refuse" the offer.
     *
     * @param player the player who needs to make the double decision
     * @return {@code true} if the player accepts the double, {@code false} if the player refuses
     */
    public boolean getDoubleDecision(Player player) {
        String name = player.name();
        String colorCode = getColorANSI(player.color());
        while (true) {
            display(String.format(Messages.OFFER_DOUBLE, colorCode, name));
            String commandLineInput = cleanInput(getInput());

            if (commandLineInput.equalsIgnoreCase("accept")) return true;
            if (commandLineInput.equalsIgnoreCase("refuse")) return false;

            display(Messages.INVALID_DECISION);
        }
    }

    /**
     * Displays a message indicating that the current player cannot offer a double
     * because the doubling die is owned by another player.
     *
     * @param owner the player who currently owns the doubling die
     */
    public void cannotOfferDouble(Player owner) {
        display(String.format(Messages.DOUBLE_DICE_OWNER, getColorANSI(owner.color()), owner.name()));
    }

    /**
     * Displays a hint message listing the available commands for the player.
     * If the doubling option is available, the hint includes the "double" command.
     *
     * @param doubleAvailable {@code true} if the "double" command is available, {@code false} otherwise
     */
    public void displayHint(boolean doubleAvailable) {
        if (doubleAvailable) {
            display(Messages.HINT_WITH_DOUBLE);
            return;
        }
        display(Messages.HINT);
    }

    /**
     * Displays a list of possible move sequences for the current player.
     * Each sequence is displayed as a numbered option, with its moves listed.
     *
     * @param possibleMoveSequences a list of possible move sequences, where each sequence is a list of {@code Move} objects
     */
    public void displayPossibleMoves(List<List<Move>> possibleMoveSequences) {
        display(Messages.POSSIBLE_MOVES_TITLE);
        for (int i = 0; i < possibleMoveSequences.size(); i++) {
            String movesToDisplay = " ";
            for (Move move : possibleMoveSequences.get(i)) {
                movesToDisplay = movesToDisplay + move + ", ";
            }
            display(String.format(Messages.MOVE_OPTION_TITLE, 1 + i) + movesToDisplay.substring(0, movesToDisplay.length() - 2));
        }
    }

    /**
     * Displays a message indicating that the specified player has no available moves.
     *
     * @param player the player who has no available moves
     */
    public void displayNoMovesAvailable(Player player) {
        String name = player.name();
        String colorCode = getColorANSI(player.color());
        display(String.format(Messages.NO_POSSIBLE_MOVES, colorCode, name));
    }

    /**
     * Displays a message indicating that only one move sequence is possible
     * and lists the moves in that sequence.
     *
     * @param moves the list of moves in the only possible move sequence
     */
    public void displayOnlyOnePossibleMove(List<Move> moves) {
        display(Messages.ONLY_POSSIBLE_MOVE);
        for (Move move : moves) {
            display("  " + move);
        }
    }

    /**
     * Prompts the user to select a move sequence from the available options.
     * Validates the input to ensure it is within the range of available options.
     *
     * @param numberOfOptions the number of available move options
     * @return the index of the selected move sequence (zero-based)
     */
    public int promptMoveSelection(int numberOfOptions) {
        display(String.format(Messages.CHOSE_MOVE_PROMPT, numberOfOptions));

        while (true) {
            try {
                int choice = Integer.parseInt(getInput()) - 1;
                if (choice >= 0 && choice < numberOfOptions) {
                    return choice;
                }
                display(String.format(Messages.OUT_OF_VALID_RANGE, numberOfOptions));
            } catch (NumberFormatException e) {
                display(Messages.INVALID_INTEGER);
            }
            display(Messages.PLEASE_TRY_AGAIN);
        }
    }

    /**
     * Prompts the user to start a new match and validates the input.
     *
     * @return {@code true} if the user wants to start a new match, {@code false} otherwise
     */
    public boolean promptStartNewMatch() {
        display("Would you like to start a new match? (yes/no)");
        while (true) {
            String input = cleanInput(getInput());
            if (input.equalsIgnoreCase("yes")) {
                return true;
            } else if (input.equalsIgnoreCase("no")) {
                return false;
            } else {
                display("Invalid input. Please type 'yes' or 'no'.");
            }
        }
    }
}