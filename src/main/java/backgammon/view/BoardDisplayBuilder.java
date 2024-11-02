package backgammon.view;

import backgammon.board.Board;
import backgammon.board.Checker;
import backgammon.board.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BoardDisplayBuilder {
    // Makes the rollToPlay argument in buildBoard() default to null if not specified
    static public void buildBoard(Board board) {
        buildBoard(board, null);
    }

    static public String buildBoard(Board board, List<Integer> rollToPlay) {
        List<Stack<Checker>> checkersOnPoints = getCheckersOnPoints(board.getPoints());

        StringBuilder displayBoard = new StringBuilder();
        //displayBoard.append(BoardFormatting.TOP_BORDER);
        displayBoard.append(BoardFormatting.TOP);

        int maxOfTopPoints = getMaxSizeInRange(checkersOnPoints, 12, 24);
        for (int j = 0; j < maxOfTopPoints; j++) {
            displayBoard.append("|  ");
            for(int i = 12; i < 24; i++) {
                if(checkersOnPoints.get(i).size() > j) {
                    Checker checker = checkersOnPoints.get(i).get(j);
                    displayBoard.append(getCheckerDisplayString(checker));
                } else {
                    displayBoard.append(" ");
                }

                if (i == 12 || i == 19) {
                    displayBoard.append("  ");
                } else if(i == 17) {
                    displayBoard.append(" |   |  ");
                } else if(i == 23) {
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
            displayBoard.append(String.format(BoardFormatting.BOARD_WITH_2_DICE, rollToPlay.getFirst(), rollToPlay.getLast()));
        } else if (rollToPlay.size() == 4) {
            displayBoard.append(String.format(BoardFormatting.BOARD_WITH_4_DICE,
                    rollToPlay.getFirst(), rollToPlay.getFirst(), rollToPlay.getFirst(), rollToPlay.getFirst()));
        }

        int maxOfBottomPoints = getMaxSizeInRange(checkersOnPoints, 0, 12);
        for(int j = maxOfBottomPoints; j > 0; j--) {
            displayBoard.append("|  ");
            for(int i = 11; i >= 0; i--) {
                if(checkersOnPoints.get(i).size() >= j) {
                    Checker checker = checkersOnPoints.get(i).get(j-1);
                    displayBoard.append(getCheckerDisplayString(checker));
                } else {
                    displayBoard.append(" ");
                }

                if (i == 11 || i == 5) {
                    displayBoard.append("  ");
                } else if(i == 6) {
                    displayBoard.append(" |   |  ");
                } else if(i == 0) {
                    displayBoard.append(" |");
                } else {
                    displayBoard.append("   ");
                }
            }
            displayBoard.append("\n");
        }


        displayBoard.append(BoardFormatting.BOTTOM);
        displayBoard.append(BoardFormatting.BOTTOM_BORDER);
        return displayBoard.toString();
    }

    static private String getCheckerDisplayString(Checker checker) {
        return switch(checker.getColor()) {
            case BLUE -> BoardFormatting.BLUE_CHECKER;
            case RED -> BoardFormatting.RED_CHECKER;
        };
    }

    static private int getMaxSizeInRange(List<Stack<Checker>> listOfLists, int startIndex, int endIndex) {
        int maxSize = 0;

        // Loop through the specified index range
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
        for (Point point: points) {
            checkersOnPoints.add(point.getCheckerStackCopy());
        }
        return checkersOnPoints;
    }
}
