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

public class View {
    private Scanner scanner = new Scanner(System.in);
    private BufferedReader fileReader = null;

    public void display(String message) {
        System.out.println(message);
    }

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

    public void displayWelcomeMessage() {
        display(Messages.WELCOME_MESSAGE);
    }

    public void displayMatchQuitMessage() { display(Messages.MATCH_QUIT); }

    public void displayMatchWinMessage(Player winner) {
        String colorCode = getColorANSI(winner.color());
        String name = winner.name();
        display(String.format(Messages.MATCH_WIN, colorCode, name));
    }

    public void displaySingleWin() { display(Messages.SINGLE_WIN); }

    public void displayDoubleRefused() { display(Messages.DOUBLE_REFUSED); }

    public void displayGammonWin() { display(Messages.GAMMON_WIN); }

    public void displayBackgammonWin() { display(Messages.BACKGAMMON_WIN); }

    public void displayGameResult(Player winner, int pointsWon) {
        display(String.format(Messages.GAME_WINNER, getColorANSI(winner.color()), winner.name(), pointsWon));
        display(Messages.ENTER_TO_CONTINUE);
        getInput();
    }

    public Map<Color, String> retrievePlayerNames() {
        Map<Color, String> playerNames = new HashMap<>();
        playerNames.put(Color.BLUE, retrievePlayerName(String.format(Messages.NAME_PROMPT, 1)));
        playerNames.put(Color.RED, retrievePlayerName(String.format(Messages.NAME_PROMPT, 2)));
        return playerNames;
    }

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

    public void displayInitialRoll(Player player, int roll) {
        String name = player.name();
        String colorCode = getColorANSI(player.color());
        display(String.format(Messages.INITIAL_ROLL_MESSAGE, colorCode, name, roll));
    }

    public void displayRollAgain() {
        display(Messages.ROLL_AGAIN);
    }

    public void displayWhoPlaysFirst(Player firstToPlay) {
        String name = firstToPlay.name();
        String colorCode = getColorANSI(firstToPlay.color());
        display(String.format(Messages.FIRST_TO_PLAY, colorCode, name));
    }

    private String getColorANSI(Color color) {
        return switch(color) {
            case BLUE -> ColorANSICodes.BLUE;
            case RED -> ColorANSICodes.RED;
        };
    }

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

    private String cleanInput(String input) {
        return input.replaceAll("\\s", "");
    }

    private PlayerInput parsePlayerInput(String input) {
        String cleanedInput = cleanInput(input);

        PlayerInput playerInput = PlayerInputParser.parsePlayerInput(cleanedInput);
        if (playerInput == null) display(Messages.INVALID_COMMAND);

        return playerInput;
    }

    // Updated displayBoard method to accept match score and match length
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

    private void displayMatchInfo(Player player1, int player1Score, Player player2, int player2Score, int matchLength) {
        display(String.format(Messages.PLAYING_TO, matchLength));
        display(String.format(Messages.MATCH_SCORE, getColorANSI(player1.color()), player1.name(), player1Score,
                getColorANSI(player2.color()), player2.name(), player2Score));
    }

    public void displayPipCount(int redPipCount, int bluePipCount) {
        display("Red Pip Count: " + redPipCount);
        display("Blue Pip Count: " + bluePipCount + "\n");
    }

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

    public void cannotOfferDouble(Player owner) {
        display(String.format(Messages.DOUBLE_DICE_OWNER, getColorANSI(owner.color()), owner.name()));
    }

    public void displayHint(boolean doubleAvailable) {
        if (doubleAvailable) {
            display(Messages.HINT_WITH_DOUBLE);
            return;
        }
        display(Messages.HINT);
    }

    public void displayPossibleMoves(List<List<Move>> possibleMoveSequences) {
        display(Messages.POSSIBLE_MOVES_TITLE);
        for (int i = 0; i < possibleMoveSequences.size(); i++) {
            display(String.format(Messages.MOVE_OPTION_TITLE, 1 + i));
            for (Move move : possibleMoveSequences.get(i)) {
                display("  " + move);
            }
        }
    }

    public void displayNoMovesAvailable(Player player) {
        String name = player.name();
        String colorCode = getColorANSI(player.color());
        display(String.format(Messages.NO_POSSIBLE_MOVES, colorCode, name));
    }

    public void displayOnlyOnePossibleMove(List<Move> moves) {
        display(Messages.ONLY_POSSIBLE_MOVE);
        for (Move move : moves) {
            display("  " + move);
        }
    }

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
