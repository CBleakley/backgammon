package backgammon.view;

import backgammon.Dice.DoubleDice;
import backgammon.PlayerInput.*;
import backgammon.board.Board;
import backgammon.board.Color;
import backgammon.gameLogic.Move;
import backgammon.player.Player;

import java.util.*;

public class View {
    final private Scanner scanner = new Scanner(System.in);

    public void display(String message) {
        System.out.println(message);
    }

    public String getInput() {
        return scanner.nextLine();
    }

    public void displayWelcomeMessage() {
        display(Messages.WELCOME_MESSAGE);
    }

    public void displayMatchQuitMessage() { display(Messages.MATCH_QUIT); }

    public void displayMatchWinMessage(Player winner) {
        String colorCode = getColorANSI(winner.getColor());
        String name = winner.getName();
        display(String.format(Messages.MATCH_WIN, colorCode, name));
    }

    public void displaySingleWin() { display(Messages.SINGLE_WIN); }

    public void displayDoubleRefused() { display(Messages.DOUBLE_REFUSED); }

    public void displayGammonWin() { display(Messages.GAMMON_WIN); }

    public void displayBackgammonWin() { display(Messages.BACKGAMMON_WIN); }

    public void displayGameResult(Player winner, int pointsWon) {
        display(String.format(Messages.GAME_WINNER, getColorANSI(winner.getColor()), winner.getName(), pointsWon));
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
        String name = player.getName();
        String colorCode = getColorANSI(player.getColor());
        display(String.format(Messages.INITIAL_ROLL_MESSAGE, colorCode, name, roll));
    }

    public void displayRollAgain() {
        display(Messages.ROLL_AGAIN);
    }

    public void displayXPlaysFirst(Player firstToPlay) {
        String name = firstToPlay.getName();
        String colorCode = getColorANSI(firstToPlay.getColor());
        display(String.format(Messages.FIRST_TO_PLAY, colorCode, name));
    }

    private String getColorANSI(Color color) {
        return switch(color) {
            case BLUE -> ColorANSICodes.BLUE;
            case RED -> ColorANSICodes.RED;
        };
    }

    public PlayerInput getPlayerInput(Player player) {
        String name = player.getName();
        String colorCode = getColorANSI(player.getColor());
        PlayerInput playerInput;
        do {
            display(String.format(Messages.PLAYER_INPUT_PROMPT, colorCode, name));
            String commandLineInput = getInput();

            playerInput = parsePlayerInput(commandLineInput);
        } while(playerInput == null);

        return playerInput;
    }

    private String cleanInput(String input) {
        return input.replaceAll("\\s", "");
    }

    private PlayerInput parsePlayerInput(String input) {
        String cleanedInput = cleanInput(input);

        QuitCommand quitCommand = QuitCommand.parse(cleanedInput);
        if (quitCommand != null) return quitCommand;

        RollCommand rollCommand = RollCommand.parse(cleanedInput);
        if (rollCommand != null) return rollCommand;

        SetDiceCommand setDiceCommand = SetDiceCommand.parse(cleanedInput);
        if (setDiceCommand != null) return setDiceCommand;

        PipCommand pipCommand = PipCommand.parse(cleanedInput);
        if (pipCommand != null) return pipCommand;

        HintCommand hintCommand = HintCommand.parse(cleanedInput);
        if (hintCommand != null) return hintCommand;

        DoubleCommand doubleCommand = DoubleCommand.parse(cleanedInput);
        if (doubleCommand != null) return doubleCommand;

        display(Messages.INVALID_COMMAND);
        return null;
    }

    // Updated displayBoard method to accept match score and match length
    public void displayBoard(Board board, List<Integer> rollToPlay, Player playerToPlay, Integer pip,
                             Player player1, int player1Score, Player player2, int player2Score, int matchLength, DoubleDice doubleDice) {

        display("\n");
        if (playerToPlay != null) {
            displayMatchInfo(player1, player1Score, player2, player2Score, matchLength);

            // Display board with player-specific details
            String colorCode = getColorANSI(playerToPlay.getColor());
            String name = playerToPlay.getName();
            display("\n" + String.format(Messages.BOARD_TITLE, colorCode, name, pip));

            String boardToDisplay = BoardDisplayBuilder.buildBoard(board, doubleDice, rollToPlay, playerToPlay.getColor());
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
        display(String.format(Messages.MATCH_SCORE, getColorANSI(player1.getColor()), player1.getName(), player1Score,
                getColorANSI(player2.getColor()), player2.getName(), player2Score));
    }

    public void displayPipCount(int redPipCount, int bluePipCount) {
        display("Red Pip Count: " + redPipCount);
        display("Blue Pip Count: " + bluePipCount + "\n");
    }

    public boolean getDoubleDecision(Player player) {
        String name = player.getName();
        String colorCode = getColorANSI(player.getColor());
        boolean accepted;
        while (true) {
            display(String.format(Messages.OFFER_DOUBLE, colorCode, name));
            String commandLineInput = cleanInput(getInput());

            if (commandLineInput.equalsIgnoreCase("accept")) return true;
            if (commandLineInput.equalsIgnoreCase("refuse")) return false;

            display(Messages.INVALID_DECISION);
        }
    }

    public void cannotOfferDouble(Player owner) {
        display(String.format(Messages.DOUBLE_DICE_OWNER, getColorANSI(owner.getColor()), owner.getName()));
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
            display(String.format(Messages.MOVE_OPTION_TITLE, 1+i));
            for (Move move : possibleMoveSequences.get(i)) {
                display("  " + move);
            }
        }
    }

    public void displayNoMovesAvailable(Player player) {
        String name = player.getName();
        String colorCode = getColorANSI(player.getColor());
        display(String.format(Messages.NO_POSSIBLE_MOVES, colorCode, name));
    }

    public void displayOnlyOnePossibleMove(List<Move> moves) {
        display(Messages.ONLY_POSSIBLE_MOVE);
        for (Move move : moves) {
            display("  " + move);
        }
    }

    public void displayRoll(Player player, List<Integer> diceRolls) {
        System.out.println(player.getName() + " rolled: " + diceRolls);
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
            String input = getInput().trim().toLowerCase();
            if (input.equals("yes")) {
                return true;
            } else if (input.equals("no")) {
                return false;
            } else {
                display("Invalid input. Please type 'yes' or 'no'.");
            }
        }
    }
}
