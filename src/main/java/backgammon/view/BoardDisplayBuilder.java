package backgammon.view;

import backgammon.board.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BoardDisplayBuilder {
    static public String buildBoard(Board board) {
        return buildBoard(board, null, null, 0, 0, 0);
    }

    static public String buildBoard(Board board, List<Integer> rollToPlay, Color colorToPlay, int matchScore1, int matchScore2, int matchLength) {
        List<Stack<Checker>> checkersOnPoints = getCheckersOnPoints(board.getPoints());

        StringBuilder displayBoard = new StringBuilder();

        // Add match score and length at the top
        displayBoard.append(String.format(BoardFormatting.MATCH_INFO, matchScore1, matchScore2, matchLength));

        Stack<Checker> redOff = board.getOff().getOffOfColor(Color.RED);
        String numberOfRedCheckersOff = String.format(BoardFormatting.RED_NUMBER, redOff.size());
        if (colorToPlay == Color.BLUE) {
            displayBoard.append(String.format(BoardFormatting.PIP_13_24, numberOfRedCheckersOff));
        } else if (colorToPlay == Color.RED) {
            displayBoard.append(String.format(BoardFormatting.PIP_12_1, numberOfRedCheckersOff));
        } else {
            displayBoard.append(String.format(BoardFormatting.NO_PIP_BOARDER, numberOfRedCheckersOff));
        }

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

        if (rollToPlay == null) {
            displayBoard.append(BoardFormatting.EMPTY_BOARD_LINE);
        } else if (rollToPlay.size() == 2) {
            displayBoard.append(String.format(BoardFormatting.BOARD_WITH_2_DICE, rollToPlay.get(0), rollToPlay.get(1)));
        } else if (rollToPlay.size() == 4) {
            displayBoard.append(String.format(BoardFormatting.BOARD_WITH_4_DICE,
                    rollToPlay.get(0), rollToPlay.get(1), rollToPlay.get(2), rollToPlay.get(3)));
        }

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

        Stack<Checker> blueOff = board.getOff().getOffOfColor(Color.BLUE);
        String numberOfBlueCheckersOff = String.format(BoardFormatting.BLUE_NUMBER, blueOff.size());
        if (colorToPlay == Color.BLUE) {
            displayBoard.append(String.format(BoardFormatting.PIP_12_1, numberOfBlueCheckersOff));
        } else if (colorToPlay == Color.RED) {
            displayBoard.append(String.format(BoardFormatting.PIP_13_24, numberOfBlueCheckersOff));
        } else {
            displayBoard.append(String.format(BoardFormatting.NO_PIP_BOARDER, numberOfBlueCheckersOff));
        }

        return displayBoard.toString();
    }

    static private String getCheckerDisplayString(Checker checker) {
        return switch (checker.getColor()) {
            case BLUE -> BoardFormatting.BLUE_CHECKER;
            case RED -> BoardFormatting.RED_CHECKER;
        };
    }

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

    static private List<Stack<Checker>> getCheckersOnPoints(List<Point> points) {
        List<Stack<Checker>> checkersOnPoints = new ArrayList<>();
        for (Point point : points) {
            checkersOnPoints.add(point.getCheckerStackCopy());
        }
        return checkersOnPoints;
    }
}
