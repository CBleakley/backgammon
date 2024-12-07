package backgammon.view;

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

        PipCommand pipCommand = PipCommand.parse(cleanedInput);
        if (pipCommand != null) return pipCommand;

        HintCommand hintCommand = HintCommand.parse(cleanedInput);
        if (hintCommand != null) return hintCommand;

        display(Messages.INVALID_COMMAND);
        return null;
    }

    public void displayRoll(List<Integer> roll) {
        if (roll.size() == 2) {
            display(String.format(Messages.PLAYER_ROLL, roll.getFirst(), roll.getLast()));
            return;
        }
        display(String.format(Messages.PLAYER_ROLL_DOUBLES, roll.getFirst()));
    }

    // Makes the rollToPlay argument in displayBoard() default to null if not specified
    public void displayBoard(Board board) {
        displayBoard(board, null, null, null);
    }

    public void displayBoard(Board board, List<Integer> rollToPlay, Player playerToPlay, Integer pip) {
        if (playerToPlay != null) {
            String boardToDisplay = BoardDisplayBuilder.buildBoard(board, rollToPlay, playerToPlay.getColor());
            String colorCode = getColorANSI(playerToPlay.getColor());
            String name = playerToPlay.getName();
            display("\n" + String.format(Messages.BOARD_TITLE, colorCode, name, pip));
            display(boardToDisplay);
            return;
        }
        String boardToDisplay = BoardDisplayBuilder.buildBoard(board);
        display("\n" + boardToDisplay);
    }

    public void displayPipCount(int redPipCount, int bluePipCount) {
        display("Red Pip Count: " + redPipCount);
        display("Blue Pip Count: " + bluePipCount + "\n");
    }

    public void displayHint() {
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

}
