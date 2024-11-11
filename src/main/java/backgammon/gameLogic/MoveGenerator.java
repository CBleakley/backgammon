package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.board.Bar;
import backgammon.board.Checker;
import backgammon.board.Color;
import backgammon.board.Point;

import java.util.*;

public class MoveGenerator {

    public static List<List<Move>> generateAllPossibleMoveSequences(Board board, List<Integer> diceValues, Color playerColor) {
        // Use BFS to explore all possible move sequences
        // Initialize the queue with the initial state
        Queue<GameState> queue = new LinkedList<>();
        Set<StateKey> visitedStates = new HashSet<>();
        List<List<Move>> result = new ArrayList<>();
        int maxMovesUsed = 0;

        // Initial state
        GameState initialState = new GameState(board.cloneBoard(), new ArrayList<>(diceValues), new ArrayList<>());
        queue.add(initialState);
        visitedStates.add(new StateKey(initialState));

        System.out.println("Starting BFS...");

        while (!queue.isEmpty()) {
            GameState currentState = queue.poll();

            // Print the current state and move sequence being considered
            System.out.println("\nConsidering moves for state with remaining dice: " + currentState.remainingDice);
            System.out.println("Move sequence: " + currentState.moveSequence);

            // If no remaining dice or no possible moves, collect the move sequence
            if (currentState.remainingDice.isEmpty() || !hasPossibleMoves(currentState.board, currentState.remainingDice, playerColor)) {
                int movesUsed = currentState.moveSequence.size();
                if (movesUsed > maxMovesUsed) {
                    // Found sequences using more dice, clear previous results
                    maxMovesUsed = movesUsed;
                    result.clear();
                    System.out.println("New maximum moves found, clearing previous results...");
                }
                if (movesUsed == maxMovesUsed) {
                    result.add(new ArrayList<>(currentState.moveSequence));
                    System.out.println("Adding sequence to results: " + currentState.moveSequence);
                }
                continue;
            }

            // Generate all possible moves from the current state
            List<MoveOption> possibleMoves = generatePossibleMoves(currentState.board, currentState.remainingDice, playerColor);

            for (MoveOption moveOption : possibleMoves) {
                // Apply the move to a cloned board
                Board newBoard = currentState.board.cloneBoard();
                MoveExecutor.executeMove(newBoard, moveOption.move);

                // Prepare new remaining dice
                List<Integer> newRemainingDice = new ArrayList<>(currentState.remainingDice);
                newRemainingDice.remove((Integer) moveOption.dieUsed);

                // Prepare new move sequence
                List<Move> newMoveSequence = new ArrayList<>(currentState.moveSequence);
                newMoveSequence.add(moveOption.move);

                // Create new state
                GameState newState = new GameState(newBoard, newRemainingDice, newMoveSequence);

                // Create a key for visited states
                StateKey newStateKey = new StateKey(newState);

                if (!visitedStates.contains(newStateKey)) {
                    // Print the move sequence that is being added
                    System.out.println("Adding new state with move sequence: " + newMoveSequence);
                    visitedStates.add(newStateKey);
                    queue.add(newState);
                } else {
                    // Print the sequence that is discarded as a duplicate
                    System.out.println("Discarding duplicate state with move sequence: " + newMoveSequence);
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

        // Determine playable dice
        List<Integer> playableDice = getPlayableDice(board, diceValues, playerColor);

        if (playableDice.isEmpty()) {
            return moveOptions; // No possible moves
        }

        // Enforce forced move rules
        if (playableDice.size() == diceValues.size()) {
            // All dice can be played, proceed as normal
        } else if (playableDice.size() == 1) {
            // Only one die can be played
            diceValues = new ArrayList<>();
            diceValues.add(playableDice.get(0));
        } else if (playableDice.size() > 1) {
            // Both dice are playable separately, but cannot both be used
            // Must play the larger die
            int dieToUse = Collections.max(playableDice);
            diceValues = new ArrayList<>();
            diceValues.add(dieToUse);
        }

        // Generate moves using adjusted diceValues
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


    private static List<MoveOption> generatePossibleMovesForDie(Board board, int dieValue, Color playerColor) {
        List<MoveOption> possibleMoves = new ArrayList<>();

        // Check if the player can bear off
        if (isBearingOff(board, playerColor)) {
            List<Integer> bearOffPoints = getBearOffPoints(board, dieValue, playerColor);
            for (int fromPointIndex : bearOffPoints) {
                Move move = new Move(fromPointIndex, -1, playerColor); // -1 indicates bearing off
                possibleMoves.add(new MoveOption(move, dieValue));
            }
            return possibleMoves;
        }

        // First, check if the player has any checkers on the bar
        if (!board.getBar().getBarOfColor(playerColor).isEmpty()) {
            // The player must enter checkers from the bar
            int entryPointIndex;
            if (playerColor == Color.RED) {
                entryPointIndex = dieValue - 1; // Points are 0-indexed
            } else { // BLUE
                entryPointIndex = 24 - dieValue;
            }

            if (entryPointIndex >= 0 && entryPointIndex < 24) {
                Point entryPoint = board.getPoints().get(entryPointIndex);
                if (isPointOpenForPlayer(entryPoint, playerColor)) {
                    Move move = new Move(-3, entryPointIndex, playerColor);
                    possibleMoves.add(new MoveOption(move, dieValue));
                }
            }
        } else {
            // No checkers on the bar
            List<Point> points = board.getPoints();
            for (int i = 0; i < points.size(); i++) {
                Point fromPoint = points.get(i);
                if (fromPoint.hasCheckers() && fromPoint.getTopCheckerColor() == playerColor) {
                    int toPointIndex = (playerColor == Color.RED) ? i + dieValue : i - dieValue;

                    boolean canBearOff = isBearingOff(board, playerColor);
                    boolean isBearingOffMove = false;

                    if (canBearOff) {
                        if ((playerColor == Color.RED && toPointIndex >= 24) ||
                                (playerColor == Color.BLUE && toPointIndex < 0)) {
                            // Bearing off
                            toPointIndex = -1; // Indicate bearing off
                            isBearingOffMove = true;
                        }
                    }

                    if (toPointIndex >= 0 && toPointIndex < 24) {
                        Point toPoint = points.get(toPointIndex);
                        if (isPointOpenForPlayer(toPoint, playerColor)) {
                            Move move = new Move(i, toPointIndex, playerColor);
                            possibleMoves.add(new MoveOption(move, dieValue));
                        }
                    } else if (isBearingOffMove) {
                        // Bearing off move
                        Move move = new Move(i, -1, playerColor);
                        possibleMoves.add(new MoveOption(move, dieValue));
                    }
                }
            }
        }

        return possibleMoves;
    }

    private static List<Integer> getBearOffPoints(Board board, int dieValue, Color playerColor) {
        List<Integer> bearOffPoints = new ArrayList<>();
        List<Point> points = board.getPoints();

        int start = (playerColor == Color.RED) ? 18 : 0;
        int end = (playerColor == Color.RED) ? 23 : 5;
        int direction = (playerColor == Color.RED) ? 1 : -1;

        // Determine target point based on die value
        int targetPointIndex = (playerColor == Color.RED) ? 24 - dieValue : dieValue - 1;

        // Check if the target point has a checker to bear off
        if (targetPointIndex >= start && targetPointIndex <= end) {
            Point targetPoint = points.get(targetPointIndex);
            if (targetPoint.hasCheckers() && targetPoint.getTopCheckerColor() == playerColor) {
                bearOffPoints.add(targetPointIndex);
                return bearOffPoints; // Return immediately if exact match is found
            }
        }

        // If no checker is on the exact point, check for higher points
        boolean foundHigherChecker = false;
        int rangeStart = (playerColor == Color.RED) ? targetPointIndex + 1 : targetPointIndex - 1;
        int rangeEnd = (playerColor == Color.RED) ? end + 1 : start - 1;
        int step = (playerColor == Color.RED) ? 1 : -1;

        for (int i = rangeStart; i != rangeEnd; i += step) {
            Point point = points.get(i);
            if (point.hasCheckers() && point.getTopCheckerColor() == playerColor) {
                foundHigherChecker = true;
                bearOffPoints.add(i);
                break;
            }
        }

        // If no higher points have checkers, bear off from the highest occupied point
        if (!foundHigherChecker) {
            for (int i = end; i != start - direction; i -= direction) {
                Point point = points.get(i);
                if (point.hasCheckers() && point.getTopCheckerColor() == playerColor) {
                    bearOffPoints.add(i);
                    break;
                }
            }
        }

        return bearOffPoints;
    }


    private static boolean isPointOpenForPlayer(Point point, Color playerColor) {
        if (!point.hasCheckers()) {
            return true;
        } else if (point.getTopCheckerColor() == playerColor) {
            return true;
        } else {
            return point.getNumberOfCheckers() == 1;
        }
    }

    private static boolean isBearingOff(Board board, Color playerColor) {
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
        } else { // BLUE
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
        // Include bar and off in the key
        Bar bar = board.getBar();
        sb.append("BarRed:").append(bar.getBarOfColor(Color.RED).size()).append("|");
        sb.append("BarBlue:").append(bar.getBarOfColor(Color.BLUE).size()).append("|");

        // Include off
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