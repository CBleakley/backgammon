package backgammon.view;

import backgammon.board.Board;
import backgammon.board.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    public List<String> retrievePlayerNames() {
        List<String> playerNames = new ArrayList<>();
        playerNames.add(retrievePlayerName(String.format(Messages.NAME_PROMPT, 1)));
        playerNames.add(retrievePlayerName(String.format(Messages.NAME_PROMPT, 2)));
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

    public void displayBoard(Board board) {
        List<Point> points = board.getPoints();

        StringBuilder displayBoard = new StringBuilder();
        displayBoard.append(BoardFormatting.TOP);

        displayBoard.append(BoardFormatting.BOTTOM);
        display(displayBoard.toString());
    }

    private void moveCursorTo(int row, int col) {
        System.out.print(String.format("\033[%d;%dH", row, col));
    }
}
