/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.gameLogic;

import backgammon.board.Board;
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
     * Generates all possible move sequences for a player.
     *
     * @param board      the current game board
     * @param diceValues the list of available dice values
     * @param playerColor the color of the player making the moves
     * @return a list of move sequences, each sequence being a list of {@code Move} objects
     */
    public static List<List<Move>> generateAllPossibleMoveSequences(Board board, List<Integer> diceValues, Color playerColor) {
        GameState initialGameState = new GameState(board, diceValues, new ArrayList<>());

        List<List<Move>> foundMoves = searchForMoves(initialGameState, playerColor);

        if (foundMoves.size() == 1) return foundMoves;  // If only one or no move sequences found no further processing

        int maxLength = 0;
        for (List<Move> moveSequence: foundMoves) {
            if (moveSequence.size() > maxLength) maxLength = moveSequence.size();
        }

        if (diceValues.size() == 2 && maxLength == 1) {
            int largerDice = Math.max(diceValues.getFirst(), diceValues.getLast());

            boolean canMoveWithLarger = false;
            for (List<Move> moveSequence : foundMoves) {
                if (moveSequence.getFirst().getDiceUsed() == largerDice) {
                    canMoveWithLarger = true;
                    break;
                }
            }

            if (canMoveWithLarger) {
                int smallerDice = Math.min(diceValues.getFirst(), diceValues.getLast());
                foundMoves.removeIf(sequence -> sequence.getFirst().getDiceUsed() == smallerDice);
            }
        }

        List<List<Move>> filteredMoves = new ArrayList<>();
        for (List<Move> moveSequence : foundMoves) {    // Remove any move sequences shorter than the longest move sequence
            if (moveSequence.size() == maxLength) {
                filteredMoves.add(moveSequence);
            }
        }

        List<List<Move>> duplicateSequencesRemoved = new ArrayList<>(filteredMoves);
        for (int i = 0; i < duplicateSequencesRemoved.size(); i++) {    // Remove duplicate move sequences
            for (int j = i+1; j < duplicateSequencesRemoved.size(); j++) {
                if (areSequencesDuplicates(duplicateSequencesRemoved.get(i), duplicateSequencesRemoved.get(j))) {
                    duplicateSequencesRemoved.remove(j);
                    j--;
                }
            }
        }

        return duplicateSequencesRemoved;
    }

    /**
     * Searches for all possible move sequences using depth first search algorithm starting from a given game state.
     *
     * @param gameState the current state of the game
     * @param color     the color of the player
     * @return a list of all possible move sequences from the given game state
     */
    private static List<List<Move>> searchForMoves(GameState gameState, Color color) {
        List<List<Move>> allSequences = new ArrayList<>();

        // Base case is when no dice are left or there are no more possible moves
        if (gameState.remainingDice.isEmpty() || !validMoveExists(gameState, color)) {
            allSequences.add(new ArrayList<>(gameState.moveSequence));
            return allSequences;
        }

        Board board = gameState.board;
        if (!board.getBar().getBarOfColor(color).isEmpty()) {   // Checkers from bar must be moved before ant other moves
            for (Integer dice : gameState.remainingDice) {
                int toPoint = (color == Color.BLUE) ? 24 - dice : dice - 1;
                Move potentialMove = new Move(-3, toPoint, color, dice);

                if (isMoveValid(board, potentialMove, dice, color)) {
                    Board newBoard = board.cloneBoard();    // Make deep copy of the board
                    MoveExecutor.executeMove(newBoard, potentialMove);  // Execute move on deep copy of the board

                    // Make a deep copy and remove the used dice
                    List<Integer> newRemainingDice = new ArrayList<>(gameState.remainingDice);
                    newRemainingDice.remove(dice);

                    // Copy the move sequence and add the new move
                    List<Move> newMoveSequence = new ArrayList<>(gameState.moveSequence);
                    newMoveSequence.add(potentialMove);

                    // Recursively call searching algorithm with the new game state and remaining dice
                    allSequences.addAll(searchForMoves(new GameState(newBoard, newRemainingDice, newMoveSequence), color));
                }
            }
        } else {
            for (Integer dice : gameState.remainingDice) {
                List<Point> points = gameState.board.getPoints();

                for (int i = 0; i < points.size(); i++) {
                    int toPoint = (color == Color.BLUE) ? i - dice : i + dice;  // Apply the directions in which players move

                    if (toPoint > 23) {
                        toPoint = Board.RED_OFF_FLAG;
                    }
                    if (toPoint < 0) {
                        toPoint = Board.BLUE_OFF_FLAG;
                    }

                    Move potentialMove = new Move(i, toPoint, color, dice);
                    if (isMoveValid(board, potentialMove, dice, color)) {
                        Board newBoard = board.cloneBoard();        // Make deep copy of the board
                        MoveExecutor.executeMove(newBoard, potentialMove);  // Execute move on deep copy of the board

                        // Make a deep copy and remove the used dice
                        List<Integer> newRemainingDice = new ArrayList<>(gameState.remainingDice);
                        newRemainingDice.remove(dice);

                        // Copy the move sequence and add the new move
                        List<Move> newMoveSequence = new ArrayList<>(gameState.moveSequence);
                        newMoveSequence.add(potentialMove);

                        // Recursively call searching algorithm with the new game state and remaining dice
                        allSequences.addAll(searchForMoves(new GameState(newBoard, newRemainingDice, newMoveSequence), color));
                    }
                }
            }
        }

        return allSequences;
    }

    /**
     * Checks if any valid move exists for the current game state and player.
     *
     * @param gameState the current state of the game
     * @param color     the color of the player
     * @return {@code true} if a valid move exists, {@code false} otherwise
     */
    private static boolean validMoveExists(GameState gameState, Color color) {
        Board board = gameState.board;
        if (!board.getBar().getBarOfColor(color).isEmpty()) {   // If player has checkers on the bar they must be moved off
            for (Integer dice : gameState.remainingDice) {
                int toPoint = (color == Color.BLUE) ? 24 - dice : dice - 1;
                Move potentialMove = new Move(-3, toPoint, color, dice);

                if (isMoveValid(board, potentialMove, dice, color)) {
                    return true;
                }
            }
        } else {
            for (Integer dice : gameState.remainingDice) {
                List<Point> points = gameState.board.getPoints();

                for (int i = 0; i < points.size(); i++) {
                    int toPoint = (color == Color.BLUE) ? i - dice : i + dice;

                    if (toPoint > 23) {
                        toPoint = Board.RED_OFF_FLAG;
                    }
                    if (toPoint < 0) {
                        toPoint = Board.BLUE_OFF_FLAG;
                    }

                    Move potentialMove = new Move(i, toPoint, color, dice);
                    if (isMoveValid(board, potentialMove, dice, color)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Validates a potential move to ensure it complies with Backgammon rules.
     *
     * @param board      the current game board
     * @param move       the move to validate
     * @param diceUsed   the dice value used for the move
     * @param color      the color of the player
     * @return {@code true} if the move is valid, {@code false} otherwise
     */
    private static boolean isMoveValid(Board board, Move move, int diceUsed, Color color) {
        int fromPoint = move.getFromPoint();

        if (!playerHasCheckerOnPoint(board, fromPoint, color)) {
            return false;
        }

        int toPoint = move.getToPoint();

        if (toPoint == Board.BLUE_OFF_FLAG || toPoint == Board.RED_OFF_FLAG) {
            return canBearOff(board, move, diceUsed, color);
        }

        return playerCanMoveToPoint(board, toPoint, color);
    }

    /**
     * Checks if a player has a checker on a specific point or on the bar.
     *
     * @param board      the current game board
     * @param fromPoint  the starting point of the move
     * @param color      the color of the player
     * @return {@code true} if the player has a checker on the point, {@code false} otherwise
     */
    private static boolean playerHasCheckerOnPoint(Board board, int fromPoint, Color color) {
        if (fromPoint == Board.BAR_FLAG) {
            return !board.getBar().getBarOfColor(color).isEmpty();
        }

        if (!board.getPoints().get(fromPoint).hasCheckers()) {
            return false;
        }

        return board.getPoints().get(fromPoint).getTopCheckerColor() == color;
    }

    /**
     * Checks if a player can move to a specific point on the board.
     *
     * @param board      the current game board
     * @param toPoint    the destination point
     * @param color      the color of the player
     * @return {@code true} if the player can move to the point, {@code false} otherwise
     */
    private static boolean playerCanMoveToPoint(Board board, int toPoint, Color color) {
        Point point = board.getPoints().get(toPoint);

        if (!point.hasCheckers()) {
            return true;
        }

        Color colorOfPoint = point.getTopCheckerColor();
        if (color == colorOfPoint) {
            return true;
        }

        return point.getCheckers().size() == 1;
    }

    /**
     * Checks if a player can bear off a checker based on the current board state and dice value.
     *
     * @param board      the current game board
     * @param move       the move to evaluate
     * @param diceUsed   the dice value used for the move
     * @param color      the color of the player
     * @return {@code true} if the player can bear off the checker, {@code false} otherwise
     */
    private static boolean canBearOff(Board board, Move move, int diceUsed, Color color) {
        if (!board.getBar().getBarOfColor(color).isEmpty()) {   // Cannot bear off if the player has checkers on the bar
            return false;
        }

        int start = (color == Color.BLUE) ? 6 : 0;
        int end = (color == Color.BLUE) ? 23 : 17;

        List<Point> points = board.getPoints();
        for (int i = start; i <= end; i++) {    // Check if player has checkers outside their home board
            Point pointToCheck = points.get(i);

            if (!pointToCheck.hasCheckers()) continue;
            if (pointToCheck.getTopCheckerColor() == color) {
                return false;
            }
        }

        if (color == Color.BLUE) {
            int destination = move.getFromPoint() + diceUsed * -1;

            if (destination == -1) { return true; }

            // If the dice value is higher than the furthest checker from off they can bear it off
            for (int i = 5; i > move.getFromPoint(); i--) {
                if (points.get(i).hasCheckers() && points.get(i).getTopCheckerColor() == color) {
                    return false;
                }
            }
            return true;
        }

        int destination = move.getFromPoint() + diceUsed;

        if (destination == 24) { return true; }

        // If the dice value is higher than the furthest checker from off they can bear it off
        for (int i = 18; i < move.getFromPoint(); i++) {
            if (points.get(i).hasCheckers() && points.get(i).getTopCheckerColor() == color) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if two move sequences are duplicates by comparing their moves.
     *
     * @param sequence1 the first move sequence
     * @param sequence2 the second move sequence
     * @return {@code true} if the sequences are duplicates, {@code false} otherwise
     */
    public static boolean areSequencesDuplicates (List<Move> sequence1, List<Move> sequence2) {
        if (sequence1.size() != sequence2.size()) {
            return false;
        }

        // Create a map of moves and frequency in sequence 1
        Map<Move, Integer> sequence1MovesMap = new HashMap<>();
        for (Move move : sequence1) {
            if (sequence1MovesMap.containsKey(move)) {
                sequence1MovesMap.put(move, sequence1MovesMap.get(move) + 1);
                continue;
            }
            sequence1MovesMap.put(move, 1);
        }

        // Create a map of moves and frequency in sequence 2
        Map<Move, Integer> sequence2MovesMap = new HashMap<>();
        for (Move move : sequence2) {
            if (sequence2MovesMap.containsKey(move)) {
                sequence2MovesMap.put(move, sequence2MovesMap.get(move) + 1);
                continue;
            }
            sequence2MovesMap.put(move, 1);
        }


        return sequence1MovesMap.equals(sequence2MovesMap);
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
}