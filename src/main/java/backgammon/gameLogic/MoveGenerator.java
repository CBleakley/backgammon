package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.board.Bar;
import backgammon.board.Color;
import backgammon.board.Point;

import java.util.*;

public class MoveGenerator {

    public static List<List<Move>> generateAllPossibleMoveSequences(Board board, List<Integer> diceValues, Color playerColor) {
        Queue<GameState> queue = new LinkedList<>();
        Set<StateKey> visitedStates = new HashSet<>();
        List<List<Move>> result = new ArrayList<>();
        int maxMovesUsed = 0;

        GameState initialState = new GameState(board.cloneBoard(), new ArrayList<>(diceValues), new ArrayList<>());
        queue.add(initialState);
        visitedStates.add(new StateKey(initialState));

        while (!queue.isEmpty()) {
            GameState currentState = queue.poll();

            if (currentState.remainingDice.isEmpty() || !hasPossibleMoves(currentState.board, currentState.remainingDice, playerColor)) {
                int movesUsed = currentState.moveSequence.size();
                if (movesUsed > maxMovesUsed) {
                    maxMovesUsed = movesUsed;
                    result.clear();
                }
                if (movesUsed == maxMovesUsed) {
                    result.add(new ArrayList<>(currentState.moveSequence));
                }
                continue;
            }

            List<MoveOption> possibleMoves = generatePossibleMoves(currentState.board, currentState.remainingDice, playerColor);

            for (MoveOption moveOption : possibleMoves) {
                Board newBoard = currentState.board.cloneBoard();
                MoveExecutor.executeMove(newBoard, moveOption.move);

                List<Integer> newRemainingDice = new ArrayList<>(currentState.remainingDice);
                newRemainingDice.remove((Integer) moveOption.dieUsed);

                List<Move> newMoveSequence = new ArrayList<>(currentState.moveSequence);
                newMoveSequence.add(moveOption.move);

                GameState newState = new GameState(newBoard, newRemainingDice, newMoveSequence);

                StateKey newStateKey = new StateKey(newState);

                if (!visitedStates.contains(newStateKey)) {
                    visitedStates.add(newStateKey);
                    queue.add(newState);
                }
            }
        }

        return result;
    }

    private static List<Integer> getPlayableDice(Board board, List<Integer> diceValues, Color playerColor) {
        List<Integer> playableDice = new ArrayList<>();
        for (int dieValue : diceValues) {
            if (!generatePossibleMovesForDie(board, dieValue, playerColor).isEmpty()) {
                playableDice.add(dieValue);
            }
        }
        return playableDice;
    }

    private static boolean hasPossibleMoves(Board board, List<Integer> diceValues, Color playerColor) {
        for (int dieValue : diceValues) {
            if (!generatePossibleMovesForDie(board, dieValue, playerColor).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static List<MoveOption> generatePossibleMoves(Board board, List<Integer> diceValues, Color playerColor) {
        List<MoveOption> moveOptions = new ArrayList<>();
        Set<MoveOption> uniqueMoves = new HashSet<>();

        List<Integer> playableDice = getPlayableDice(board, diceValues, playerColor);

        if (playableDice.isEmpty()) {
            return moveOptions;
        }

        diceValues = enforceForcedMoveRules(board, playableDice, playerColor);

        for (int dieValue : diceValues) {
            List<MoveOption> movesForDie = generatePossibleMovesForDie(board, dieValue, playerColor);
            for (MoveOption moveOption : movesForDie) {
                if (uniqueMoves.add(moveOption)) {
                    moveOptions.add(moveOption);
                }
            }
        }

        return moveOptions;
    }

    private static List<Integer> enforceForcedMoveRules(Board board, List<Integer> playableDice, Color playerColor) {
        if (playableDice.size() == 1) {
            return new ArrayList<>(Collections.singletonList(playableDice.get(0)));
        } else if (playableDice.size() > 1 && !canPlayBothDice(board, playableDice, playerColor)) {
            return new ArrayList<>(Collections.singletonList(Collections.max(playableDice)));
        }
        return playableDice;
    }

    private static boolean canPlayBothDice(Board board, List<Integer> diceValues, Color playerColor) {
        for (int i = 0; i < diceValues.size(); i++) {
            int firstDie = diceValues.get(i);
            for (int j = 0; j < diceValues.size(); j++) {
                if (i == j) continue;
                int secondDie = diceValues.get(j);

                Board testBoard = board.cloneBoard();
                List<Integer> testDice = Arrays.asList(firstDie, secondDie);

                List<MoveOption> firstMoves = generatePossibleMovesForDie(testBoard, firstDie, playerColor);
                for (MoveOption firstMove : firstMoves) {
                    MoveExecutor.executeMove(testBoard, firstMove.move);

                    List<MoveOption> secondMoves = generatePossibleMovesForDie(testBoard, secondDie, playerColor);
                    if (!secondMoves.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static List<MoveOption> generatePossibleMovesForDie(Board board, int dieValue, Color playerColor) {
        List<MoveOption> possibleMoves = new ArrayList<>();

        boolean canBearOff = isBearingOff(board, playerColor);

        // First, generate all standard moves and exact bear-offs
        List<MoveOption> standardMoves = generateStandardMoves(board, dieValue, playerColor, canBearOff);

        if (!standardMoves.isEmpty()) {
            return standardMoves;
        }

        // If no moves are found, and the player can bear off, consider non-exact bear-offs
        if (canBearOff) {
            List<MoveOption> nonExactBearOffMoves = generateNonExactBearOffMoves(board, dieValue, playerColor);
            possibleMoves.addAll(nonExactBearOffMoves);
        }

        return possibleMoves;
    }

    private static List<MoveOption> generateStandardMoves(Board board, int dieValue, Color playerColor, boolean canBearOff) {
        List<MoveOption> possibleMoves = new ArrayList<>();

        if (!board.getBar().getBarOfColor(playerColor).isEmpty()) {
            // The player must enter checkers from the bar
            int entryPointIndex = (playerColor == Color.RED) ? dieValue - 1 : 24 - dieValue;

            if (entryPointIndex >= 0 && entryPointIndex < 24) {
                Point entryPoint = board.getPoints().get(entryPointIndex);
                if (isPointOpenForPlayer(entryPoint, playerColor)) {
                    Move move = new Move(-3, entryPointIndex, playerColor);
                    possibleMoves.add(new MoveOption(move, dieValue));
                }
            }
        } else {
            List<Point> points = board.getPoints();
            for (int i = 0; i < points.size(); i++) {
                Point fromPoint = points.get(i);
                if (fromPoint.hasCheckers() && fromPoint.getTopCheckerColor() == playerColor) {
                    int toPointIndex = (playerColor == Color.RED) ? i + dieValue : i - dieValue;
                    boolean isBearingOffMove = false;

                    if (canBearOff) {
                        int bearOffPointIndex = (playerColor == Color.RED) ? 23 : 0;
                        if ((playerColor == Color.RED && toPointIndex == 24) ||
                                (playerColor == Color.BLUE && toPointIndex == -1)) {
                            if (i == bearOffPointIndex - (dieValue - 1)) {
                                toPointIndex = -1; // Indicates bearing off
                                isBearingOffMove = true;
                            }
                        }
                    }

                    if (toPointIndex >= 0 && toPointIndex < 24) {
                        Point toPoint = points.get(toPointIndex);
                        if (isPointOpenForPlayer(toPoint, playerColor)) {
                            Move move = new Move(i, toPointIndex, playerColor);
                            possibleMoves.add(new MoveOption(move, dieValue));
                        }
                    } else if (isBearingOffMove) {
                        Move move = new Move(i, -1, playerColor);
                        possibleMoves.add(new MoveOption(move, dieValue));
                    }
                }
            }
        }

        return possibleMoves;
    }

    private static List<MoveOption> generateNonExactBearOffMoves(Board board, int dieValue, Color playerColor) {
        List<MoveOption> possibleMoves = new ArrayList<>();
        List<Point> points = board.getPoints();

        int start = (playerColor == Color.RED) ? 23 : 0;
        int end = (playerColor == Color.RED) ? 18 : 5;
        int step = (playerColor == Color.RED) ? -1 : 1;

        // Iterate over the home board from the furthest point to the nearest
        for (int i = start; (playerColor == Color.RED) ? i >= end : i <= end; i += step) {
            Point point = points.get(i);
            if (point.hasCheckers() && point.getTopCheckerColor() == playerColor) {
                // Calculate the distance of the point to the end
                int pointDistance = (playerColor == Color.RED) ? 24 - i : i + 1;

                // Check if the die value is greater than or equal to the distance
                if (pointDistance <= dieValue) {
                    // Allow bearing off from this point
                    Move move = new Move(i, -1, playerColor); // -1 indicates bearing off
                    possibleMoves.add(new MoveOption(move, dieValue));
                    break; // Only the furthest point is considered
                }
            }
        }

        return possibleMoves;
    }


    private static boolean isPointOpenForPlayer(Point point, Color playerColor) {
        if (!point.hasCheckers()) {
            return true;
        }
        if (point.getTopCheckerColor() == playerColor) {
            return true;
        }
        return point.getNumberOfCheckers() == 1;
    }

    private static boolean isBearingOff(Board board, Color playerColor) {
        if (!board.getBar().getBarOfColor(playerColor).isEmpty()) {
            return false;
        }

        List<Point> points = board.getPoints();
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (point.hasCheckers() && point.getTopCheckerColor() == playerColor) {
                if (!isPointInHomeBoard(i, playerColor)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean isPointInHomeBoard(int pointIndex, Color playerColor) {
        if (playerColor == Color.RED) {
            return pointIndex >= 18 && pointIndex <= 23;
        } else {
            return pointIndex >= 0 && pointIndex <= 5;
        }
    }

    private static class GameState {
        public Board board;
        public List<Integer> remainingDice;
        public List<Move> moveSequence;

        public GameState(Board board, List<Integer> remainingDice, List<Move> moveSequence) {
            this.board = board;
            this.remainingDice = remainingDice;
            this.moveSequence = moveSequence;
        }
    }

    private static class StateKey {
        public String boardStateKey;
        public List<Integer> remainingDice;

        public StateKey(GameState gameState) {
            this.boardStateKey = getBoardStateKey(gameState.board);
            this.remainingDice = new ArrayList<>(gameState.remainingDice);
            Collections.sort(this.remainingDice);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof StateKey)) {
                return false;
            }
            StateKey other = (StateKey) obj;
            return this.boardStateKey.equals(other.boardStateKey) && this.remainingDice.equals(other.remainingDice);
        }

        @Override
        public int hashCode() {
            return boardStateKey.hashCode() * 31 + remainingDice.hashCode();
        }
    }

    private static String getBoardStateKey(Board board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 24; i++) {
            Point point = board.getPoints().get(i);
            if (point.hasCheckers()) {
                sb.append(i).append(":").append(point.getTopCheckerColor()).append(":").append(point.getNumberOfCheckers()).append("|");
            }
        }
        Bar bar = board.getBar();
        sb.append("BarRed:").append(bar.getBarOfColor(Color.RED).size()).append("|");
        sb.append("BarBlue:").append(bar.getBarOfColor(Color.BLUE).size()).append("|");
        sb.append("OffRed:").append(board.getOff().getOffOfColor(Color.RED).size()).append("|");
        sb.append("OffBlue:").append(board.getOff().getOffOfColor(Color.BLUE).size()).append("|");
        return sb.toString();
    }

    private static class MoveOption {
        public Move move;
        public int dieUsed;

        public MoveOption(Move move, int dieUsed) {
            this.move = move;
            this.dieUsed = dieUsed;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof MoveOption)) {
                return false;
            }
            MoveOption other = (MoveOption) obj;
            return this.move.equals(other.move) && this.dieUsed == other.dieUsed;
        }

        @Override
        public int hashCode() {
            return move.hashCode() * 31 + dieUsed;
        }
    }
}
