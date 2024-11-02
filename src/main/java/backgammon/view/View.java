package backgammon.view;

import backgammon.PlayerInput.PlayerInput;
import backgammon.PlayerInput.QuitCommand;
import backgammon.PlayerInput.RollCommand;
import backgammon.board.Board;
import backgammon.board.Color;
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
        displayBoard(board, null);
    }

    public void displayBoard(Board board, List<Integer> rollToPlay) {
        String boardToDisplay = BoardDisplayBuilder.buildBoard(board, rollToPlay);
        display(boardToDisplay);
    }

    public void displayPipCount(int redPipCount, int bluePipCount) {
        System.out.println("Red Pip Count: " + redPipCount);
        System.out.println("Blue Pip Count: " + bluePipCount);
    }

}
