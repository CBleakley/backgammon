/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.view;

import backgammon.Dice.DoubleDice;
import backgammon.board.*;
import backgammon.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Utility class for building a string representation of the backgammon board.
 * This class formats the board for display, including points, checkers, bar, off areas, dice rolls, and the doubling dice.
 */
public class BoardDisplayBuilder {

    /**
     * Builds a string representation of the backgammon board.
     *
     * @param board        the current state of the game board
     * @param doubleDice   the doubling die used in the game
     * @param rollToPlay   the dice rolls available for the current turn (or {@code null} if no rolls are present)
     * @param colorToPlay  the color of the player currently taking their turn
     * @return a string representation of the board
     */
    static public String buildBoard(Board board, DoubleDice doubleDice, List<Integer> rollToPlay, Color colorToPlay) {
        List<Stack<Checker>> checkersOnPoints = getCheckersOnPoints(board.getPoints());
        Player doubleDiceOwner = doubleDice.getOwner();

        StringBuilder displayBoard = new StringBuilder();

        // Display top section of the board
        Stack<Checker> redOff = board.getOff().getOffOfColor(Color.RED);
        String numberOfRedCheckersOff = String.format(BoardFormatting.RED_NUMBER, redOff.size());
        if (colorToPlay == Color.BLUE) {
            displayBoard.append(String.format(BoardFormatting.PIP_13_24, numberOfRedCheckersOff));
        } else if (colorToPlay == Color.RED) {
            displayBoard.append(String.format(BoardFormatting.PIP_12_1, numberOfRedCheckersOff));
        } else {
            displayBoard.append(String.format(BoardFormatting.NO_PIP_BOARDER, numberOfRedCheckersOff));
        }

        if (doubleDiceOwner != null && doubleDice.getOwner().color() == Color.RED) {
            displayBoard.append(String.format(BoardFormatting.DOUBLE_DICE, doubleDice.getMultiplier()));
        }

        displayBoard.append("\n");

        // Render top points
        int maxOfTopPoints = getMaxSizeInRange(checkersOnPoints, 12, 24);
        Bar bar = board.getBar();
        Stack<Checker> blueBar = bar.getBarOfColor(Color.BLUE);
        for (int j = 0; j < maxOfTopPoints; j++) {
            displayBoard.append("|  ");
            for (int i = 12; i < 24; i++) {
                if (checkersOnPoints.get(i).size() > j) {
                    Checker checker = checkersOnPoints.get(i).get(j);
                    displayBoard.append(getCheckerDisplayString(checker));
                } else {
                    displayBoard.append(" ");
                }

                if (i == 12 || i == 18) {
                    displayBoard.append("  ");
                } else if (i == 17) {
                    if (j == 0) {
                        String blueCheckersOnBar = (blueBar.isEmpty()) ? " " : String.format(BoardFormatting.BLUE_NUMBER, blueBar.size());
                        displayBoard.append(" | ").append(blueCheckersOnBar).append(" |  ");
                    } else {
                        displayBoard.append(" |   |  ");
                    }
                } else if (i == 23) {
                    displayBoard.append(" |");
                } else {
                    displayBoard.append("   ");
                }
            }
            displayBoard.append("\n");
        }

        // Add dice rolls (if applicable)
        if (rollToPlay == null) {
            displayBoard.append(BoardFormatting.EMPTY_BOARD_LINE);
        } else if (rollToPlay.size() == 2) {
            displayBoard.append(String.format(BoardFormatting.BOARD_WITH_2_DICE, rollToPlay.get(0), rollToPlay.get(1)));
        } else if (rollToPlay.size() == 4) {
            displayBoard.append(String.format(BoardFormatting.BOARD_WITH_4_DICE,
                    rollToPlay.get(0), rollToPlay.get(1), rollToPlay.get(2), rollToPlay.get(3)));
        }

        if (doubleDiceOwner == null) {
            displayBoard.append(String.format(BoardFormatting.DOUBLE_DICE, doubleDice.getMultiplier()));
        }

        displayBoard.append("\n");

        // Render bottom points
        int maxOfBottomPoints = getMaxSizeInRange(checkersOnPoints, 0, 12);
        Stack<Checker> redBar = bar.getBarOfColor(Color.RED);
        for (int j = maxOfBottomPoints; j > 0; j--) {
            displayBoard.append("|  ");
            for (int i = 11; i >= 0; i--) {
                if (checkersOnPoints.get(i).size() >= j) {
                    Checker checker = checkersOnPoints.get(i).get(j - 1);
                    displayBoard.append(getCheckerDisplayString(checker));
                } else {
                    displayBoard.append(" ");
                }

                if (i == 11 || i == 5) {
                    displayBoard.append("  ");
                } else if (i == 6) {
                    if (j == 1) {
                        String redCheckersOnBar = (redBar.isEmpty()) ? " " : String.format(BoardFormatting.RED_NUMBER, redBar.size());
                        displayBoard.append(" | ").append(redCheckersOnBar).append(" |  ");
                    } else {
                        displayBoard.append(" |   |  ");
                    }
                } else if (i == 0) {
                    displayBoard.append(" |");
                } else {
                    displayBoard.append("   ");
                }
            }
            displayBoard.append("\n");
        }

        // Display bottom section of the board
        Stack<Checker> blueOff = board.getOff().getOffOfColor(Color.BLUE);
        String numberOfBlueCheckersOff = String.format(BoardFormatting.BLUE_NUMBER, blueOff.size());
        if (colorToPlay == Color.BLUE) {
            displayBoard.append(String.format(BoardFormatting.PIP_12_1, numberOfBlueCheckersOff));
        } else if (colorToPlay == Color.RED) {
            displayBoard.append(String.format(BoardFormatting.PIP_13_24, numberOfBlueCheckersOff));
        } else {
            displayBoard.append(String.format(BoardFormatting.NO_PIP_BOARDER, numberOfBlueCheckersOff));
        }

        if (doubleDiceOwner != null && doubleDiceOwner.color() == Color.BLUE) {
            displayBoard.append(String.format(BoardFormatting.DOUBLE_DICE, doubleDice.getMultiplier()));
        }

        displayBoard.append("\n");

        return displayBoard.toString();
    }

    /**
     * Retrieves the string representation of a checker for display.
     *
     * @param checker the checker whose display string is needed
     * @return a string representing the checker (color-specific format)
     */
    static private String getCheckerDisplayString(Checker checker) {
        return switch (checker.getColor()) {
            case BLUE -> BoardFormatting.BLUE_CHECKER;
            case RED -> BoardFormatting.RED_CHECKER;
        };
    }

    /**
     * Finds the maximum size of the stacks of checkers within a specified range of points.
     *
     * @param listOfLists a list of checker stacks corresponding to points on the board
     * @param startIndex  the starting index of the range
     * @param endIndex    the ending index of the range
     * @return the maximum size of the stacks in the specified range
     */
    static private int getMaxSizeInRange(List<Stack<Checker>> listOfLists, int startIndex, int endIndex) {
        int maxSize = 0;

        for (int i = startIndex; i <= endIndex && i < listOfLists.size(); i++) {
            int currentSize = listOfLists.get(i).size();
            if (currentSize > maxSize) {
                maxSize = currentSize;
            }
        }

        return maxSize;
    }

    /**
     * Retrieves a list of stacks of checkers from the points on the board.
     *
     * @param points the list of points on the board
     * @return a list of stacks, where each stack contains the checkers on a point
     */
    static private List<Stack<Checker>> getCheckersOnPoints(List<Point> points) {
        List<Stack<Checker>> checkersOnPoints = new ArrayList<>();
        for (Point point : points) {
            checkersOnPoints.add(point.getCheckerStackCopy());
        }
        return checkersOnPoints;
    }
}
