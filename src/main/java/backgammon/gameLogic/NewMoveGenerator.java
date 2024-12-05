package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.board.Color;
import backgammon.board.Point;

import java.util.*;

public class NewMoveGenerator {
    public static List<List<Move>> generateAllPossibleMoveSequences(Board board, List<Integer> diceValues, Color playerColor) {
        GameState initialGameState = new GameState(board, diceValues, new ArrayList<>());
        List<List<Move>> foundMoves = searchForMoves(initialGameState, playerColor);

        if (foundMoves.size() == 1) return foundMoves;

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
                foundMoves.removeIf(sequence -> sequence.get(0).getDiceUsed() == smallerDice);
            }
        }

        List<List<Move>> filteredMoves = new ArrayList<>();
        for (List<Move> moveSequence : foundMoves) {
            if (moveSequence.size() == maxLength) {
                filteredMoves.add(moveSequence);
            }
        }
        List<Move> moveOfInterest = new ArrayList<>();
        moveOfInterest.add(new Move(8, 6, Color.BLUE, 2));
        moveOfInterest.add(new Move(8, 6, Color.BLUE, 2));
        moveOfInterest.add(new Move(10, 8, Color.BLUE, 2));
        moveOfInterest.add(new Move(10, 8, Color.BLUE, 2));

        List<List<Move>> duplicates = new ArrayList<>();
        List<List<Move>> duplicateSequencesRemoved = new ArrayList<>(filteredMoves);
        for (int i = 0; i < duplicateSequencesRemoved.size(); i++) {
            for (int j = i+1; j < duplicateSequencesRemoved.size(); j++) {
                if (areSequencesDuplicates(duplicateSequencesRemoved.get(i), duplicateSequencesRemoved.get(j))) {
                    duplicateSequencesRemoved.remove(j);
                    j--;
                }
            }
        }

        return duplicateSequencesRemoved;
    }

    private static List<List<Move>> searchForMoves(GameState gameState, Color color) {
        List<List<Move>> allSequences = new ArrayList<>();

        if (gameState.remainingDice.isEmpty()) {
            allSequences.add(new ArrayList<>(gameState.moveSequence));
            return allSequences;
        }

        Board board = gameState.board;
        if (!board.getBar().getBarOfColor(color).isEmpty()) {
            for (Integer dice : gameState.remainingDice) {
                int toPoint = (color == Color.BLUE) ? 24 - dice : dice - 1;
                Move potentialMove = new Move(-3, toPoint, color, dice);

                if (isMoveValid(board, potentialMove, dice, color)) {
                    Board newBoard = board.cloneBoard();
                    MoveExecutor.executeMove(newBoard, potentialMove);

                    List<Integer> newRemainingDice = new ArrayList<>(gameState.remainingDice);
                    newRemainingDice.remove(dice);

                    List<Move> newMoveSequence = new ArrayList<>(gameState.moveSequence);
                    newMoveSequence.add(potentialMove);

                    allSequences.addAll(searchForMoves(new GameState(newBoard, newRemainingDice, newMoveSequence), color));
                }
            }
        } else {
            for (Integer dice : gameState.remainingDice) {
                List<Point> points = gameState.board.getPoints();

                for (int i = 0; i < points.size(); i++) {
//                    if (!points.get(i).hasCheckers() || points.get(i).getTopCheckerColor() != color) {
//                        continue;
//                    }

                    int toPoint = (color == Color.BLUE) ? i - dice : i + dice;

                    if (toPoint > 23) {
                        toPoint = -2;
                    }
                    if (toPoint < 0) {
                        toPoint = -1;
                    }

                    Move potentialMove = new Move(i, toPoint, color, dice);
                    if (isMoveValid(board, potentialMove, dice, color)) {
                        Board newBoard = board.cloneBoard();
                        MoveExecutor.executeMove(newBoard, potentialMove);

                        List<Integer> newRemainingDice = new ArrayList<>(gameState.remainingDice);
                        newRemainingDice.remove(dice);

                        List<Move> newMoveSequence = new ArrayList<>(gameState.moveSequence);
                        newMoveSequence.add(potentialMove);

                        allSequences.addAll(searchForMoves(new GameState(newBoard, newRemainingDice, newMoveSequence), color));
                    }
                }
            }
        }

        return allSequences;
    }

    private static boolean isMoveValid(Board board, Move move, int diceUsed, Color color) {
        int fromPoint = move.getFromPoint();

        if (!playerHasCheckerOnPoint(board, fromPoint, color)) {
            return false;
        }

        int toPoint = move.getToPoint();

        if (toPoint == -1 || toPoint == -2) {
            // Some logic dealing with bearing off
            return canBearOff(board, move, diceUsed, color);
        }

        return playerCanMoveToPoint(board, toPoint, color);
    }

    private static boolean playerHasCheckerOnPoint(Board board, int fromPoint, Color color) {
        if (fromPoint == -3) {
            return !board.getBar().getBarOfColor(color).isEmpty();
        }

        if (!board.getPoints().get(fromPoint).hasCheckers()) {
            return false;
        }

        return board.getPoints().get(fromPoint).getTopCheckerColor() == color;
    }

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

    private static boolean canBearOff(Board board, Move move, int diceUsed, Color color) {
        if (!board.getBar().getBarOfColor(color).isEmpty()) {
            return false;
        }

        int start = (color == Color.BLUE) ? 6 : 0;
        int end = (color == Color.BLUE) ? 23 : 17;

        List<Point> points = board.getPoints();
        for (int i = start; i <= end; i++) {
            Point pointToCheck = points.get(i);

            if (!pointToCheck.hasCheckers()) continue;
            if (pointToCheck.getTopCheckerColor() == color) {
                return false;
            }
        }

        if (color == Color.BLUE) {
            int destination = move.getFromPoint() + diceUsed * -1;
            if (destination == -1) { return true; }
            for (int i = 5; i > move.getFromPoint(); i--) {
                if (points.get(i).hasCheckers() && points.get(i).getTopCheckerColor() == color) {
                    return false;
                }
            }
            return true;
        }

        int destination = move.getFromPoint() + diceUsed;
        if (destination == 24) { return true;}
        for (int i = 18; i < move.getFromPoint(); i++) {
            if (points.get(i).hasCheckers() && points.get(i).getTopCheckerColor() == color) {
                return false;
            }
        }
        return true;
    }

    public static boolean areSequencesDuplicates (List<Move> sequence1, List<Move> sequence2) {
        if (sequence1.size() != sequence2.size()) {
            return false;
        }

        Map<Move, Integer> sequence1MovesMap = new HashMap<>();
        for (Move move : sequence1) {
            if (sequence1MovesMap.containsKey(move)) {
                sequence1MovesMap.put(move, sequence1MovesMap.get(move) + 1);
                continue;
            }
            sequence1MovesMap.put(move, 1);
        }

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
}