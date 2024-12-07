/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.board.Bar;
import backgammon.board.Color;
import backgammon.board.Point;

import java.util.*;

/**
 * The {@code MoveGenerator} class generates all possible move sequences
 * for a player in a Backgammon game based on the current board state, available dice values,
 * and the player's color.
 * <p>
 * It uses a breadth-first search (BFS) algorithm to find all possible moves,
 * ensuring that the maximum number of moves are found.
 * </p>
 */
public class MoveGenerator {

    /**
     * Generates all possible sequences of moves that a player can perform given the current
     * board state, available dice values, and the player's color.
     *
     * @param board       the current state of the game board
     * @param diceValues  a list of integers for dice values rolled
     * @param playerColor the color of the current player
     * @return a list of move sequences, where each sequence is a list of {@code Move} objects
     */
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

    /**
     * Retrieves a list of dice values that can be played based on the current board state,
     * available dice, and the player's color.
     *
     * @param board       the current state of the game board
     * @param diceValues  a list of integers for dice values rolled
     * @param playerColor the color of the current player
     * @return a list of playable dice values
     */
    private static List<Integer> getPlayableDice(Board board, List<Integer> diceValues, Color playerColor) {
        List<Integer> playableDice = new ArrayList<>();
        for (int dieValue : diceValues) {
            if (!generatePossibleMovesForDie(board, dieValue, playerColor).isEmpty()) {
                playableDice.add(dieValue);
            }
        }
        return playableDice;
    }

    /**
     * Determines whether there are any possible moves available for the player given
     * the current board state and remaining dice values.
     *
     * @param board       the current state of the game board
     * @param diceValues  a list of integers representing the remaining dice values
     * @param playerColor the color of the player
     * @return {@code true} if there are possible moves, {@code false} otherwise
     */
    private static boolean hasPossibleMoves(Board board, List<Integer> diceValues, Color playerColor) {
        for (int dieValue : diceValues) {
            if (!generatePossibleMovesForDie(board, dieValue, playerColor).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a list of possible move options based on the current board state,
     * available dice values, and the player's color.
     *
     * @param board       the current state of the game board
     * @param diceValues  a list of integers representing the available dice values
     * @param playerColor the color of the player
     * @return a list of {@code MoveOption} objects representing possible moves
     */
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

    /**
     * Enforces the Backgammon rule that if both dice can be played, both must be used.
     * If only one die can be played, the higher die is preferred.
     *
     * @param board        the current state of the game board
     * @param playableDice a list of playable dice values
     * @param playerColor  the color of the player
     * @return a list of dice values after enforcing forced move rules
     */
    private static List<Integer> enforceForcedMoveRules(Board board, List<Integer> playableDice, Color playerColor) {
        if (playableDice.size() == 1) {
            return new ArrayList<>(Collections.singletonList(playableDice.get(0)));
        } else if (playableDice.size() > 1 && !canPlayBothDice(board, playableDice, playerColor)) {
            return new ArrayList<>(Collections.singletonList(Collections.max(playableDice)));
        }
        return playableDice;
    }

    /**
     * Checks whether both dice can be played given the current board state and player's color.
     *
     * @param board        the current state of the game board
     * @param diceValues   a list of dice values to check
     * @param playerColor  the color of the player
     * @return {@code true} if both dice can be played, {@code false} otherwise
     */
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

    /**
     * Generates all possible move options for a specific die value, anc checks whether
     * the player is allowed to bear off checkers.
     *
     * @param board        the current state of the game board
     * @param dieValue     the value of the die to generate moves for
     * @param playerColor  the color of the player
     * @return a list of {@code MoveOption} objects representing possible moves for the die
     */
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

    /**
     * Generates move options based on the die value, player color, and whether
     * the player can bear off checkers.
     *
     * @param board        the current state of the game board
     * @param dieValue     the value of the die
     * @param playerColor  the color of the player
     * @param canBearOff   {@code true} if the player can bear off, {@code false} otherwise
     * @return a list of {@code MoveOption} objects representing standard moves
     */
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

    /**
     * Generates non-exact bearing off move options when the player can bear off
     * but no exact moves are available. This is done towards the end
     *
     * @param board        the current state of the game board
     * @param dieValue     the value of the die
     * @param playerColor  the color of the player
     * @return a list of {@code MoveOption} objects representing non-exact bearing off moves
     */
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

    /**
     * Determines if a specific point on the board is open for the player to move into.
     * A point is open if it has no checkers, has checkers of the player's color,
     * or has exactly one opposing checker (allowing for a hit).
     *
     * @param point        the point on the board to check
     * @param playerColor  the color of the player
     * @return {@code true} if the point is open, {@code false} otherwise
     */
    private static boolean isPointOpenForPlayer(Point point, Color playerColor) {
        if (!point.hasCheckers()) {
            return true;
        }
        if (point.getTopCheckerColor() == playerColor) {
            return true;
        }
        return point.getNumberOfCheckers() == 1;
    }

    /**
     * Checks whether the player is in a position to bear off checkers.
     * A player can bear off only if all their checkers are in their home board
     * and there are no checkers on the bar.
     *
     * @param board        the current state of the game board
     * @param playerColor  the color of the player
     * @return {@code true} if the player can bear off, {@code false} otherwise
     */
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

    /**
     * Determines whether a given point index is within the player's home board.
     *
     * @param pointIndex   the index of the point on the board
     * @param playerColor  the color of the player
     * @return {@code true} if the point is in the home board, {@code false} otherwise
     */
    private static boolean isPointInHomeBoard(int pointIndex, Color playerColor) {
        if (playerColor == Color.RED) {
            return pointIndex >= 18 && pointIndex <= 23;
        } else {
            return pointIndex >= 0 && pointIndex <= 5;
        }
    }

    /**
     * Represents the state of the game at a particular point in the move generation process.
     * It includes the current board state, remaining dice values, and the sequence of moves made.
     */
    static class GameState {
        public Board board;
        public List<Integer> remainingDice;
        public List<Move> moveSequence;

        /**
         * Constructs a new {@code GameState}.
         *
         * @param board         the current state of the game board
         * @param remainingDice the list of remaining dice values
         * @param moveSequence  the sequence of moves made to reach this state
         */
        public GameState(Board board, List<Integer> remainingDice, List<Move> moveSequence) {
            this.board = board;
            this.remainingDice = remainingDice;
            this.moveSequence = moveSequence;
        }
    }

    /**
     * A unique key representing a game state, used to prevent reuse of
     * the same state more than once during move generation.
     */
    private static class StateKey {
        public String boardStateKey;
        public List<Integer> remainingDice;

        /**
         * Constructs a new {@code StateKey} based on a given {@code GameState}.
         *
         * @param gameState the game state to create a key for
         */
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

    /**
     * Generates a unique string key representing the current state of the board.
     * This includes the positions of all checkers, the bar, and the off positions.
     *
     * @param board the current state of the game board
     * @return a string representing the board state
     */
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

    /**
     * Represents a possible move option, including the move itself and the die value used.
     */
    private static class MoveOption {
        public Move move;
        public int dieUsed;

        /**
         * Constructs a new {@code MoveOption}.
         *
         * @param move     the move being considered
         * @param dieUsed the die value used for this move
         */
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
