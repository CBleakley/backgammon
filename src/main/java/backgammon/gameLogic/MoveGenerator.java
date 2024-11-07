package backgammon.gameLogic;

import backgammon.board.Board;
import backgammon.board.Point;
import backgammon.board.Color;
import backgammon.board.Checker;

import java.util.*;

public class MoveGenerator {

    public List<List<Move>> generateAllPossibleMoveSequences(Board board, List<Integer> diceRolls, Color playerColor) {
        Map<Integer, List<List<Move>>> movesByDiceUsed = new HashMap<>();
        Set<String> uniqueSequences = new HashSet<>();
        int maxDiceUsed = generateMovesRecursive(board, diceRolls, playerColor, new ArrayList<>(), movesByDiceUsed, 0, uniqueSequences);

        // Return only the sequences that use the maximum number of dice
        return movesByDiceUsed.getOrDefault(maxDiceUsed, Collections.emptyList());
    }

    private int generateMovesRecursive(Board board, List<Integer> diceRolls, Color playerColor,
                                       List<Move> currentSequence, Map<Integer, List<List<Move>>> movesByDiceUsed,
                                       int diceUsed, Set<String> uniqueSequences) {
        if (diceRolls.isEmpty()) {
            if (!currentSequence.isEmpty()) {
                List<Move> sortedSequence = new ArrayList<>(new HashSet<>(currentSequence));
                sortedSequence.sort(Comparator.comparingInt(Move::getFromPoint).thenComparingInt(Move::getToPoint));

                String sequenceKey = generateSequenceKey(sortedSequence);

                if (!uniqueSequences.contains(sequenceKey)) {
                    uniqueSequences.add(sequenceKey);
                    movesByDiceUsed.computeIfAbsent(diceUsed, k -> new ArrayList<>())
                            .add(sortedSequence);
                }
            }
            return diceUsed;
        }

        int maxDiceUsed = diceUsed;
        boolean movePossible = false;

        // Generate combinations of dice rolls for a single checker move
        List<List<Integer>> diceCombinations = generateDiceCombinations(diceRolls);

        for (List<Integer> diceCombo : diceCombinations) {
            int comboValue = diceCombo.stream().mapToInt(Integer::intValue).sum();

            List<Move> possibleMoves = getPossibleMoves(board, comboValue, playerColor);

            if (possibleMoves.isEmpty()) {
                continue;
            }

            movePossible = true;

            for (Move move : possibleMoves) {
                Board newBoard = board.cloneBoard();
                applyMove(newBoard, move);

                List<Integer> remainingDice = new ArrayList<>(diceRolls);
                diceCombo.forEach(remainingDice::remove);  // Remove used dice from remaining dice

                currentSequence.add(move);

                int recursiveDiceUsed = generateMovesRecursive(newBoard, remainingDice, playerColor,
                        currentSequence, movesByDiceUsed, diceUsed + diceCombo.size(), uniqueSequences);

                maxDiceUsed = Math.max(maxDiceUsed, recursiveDiceUsed);

                currentSequence.remove(currentSequence.size() - 1);
            }
        }

        if (!movePossible && !currentSequence.isEmpty()) {
            List<Move> sortedSequence = new ArrayList<>(currentSequence);
            sortedSequence.sort(Comparator.comparingInt(Move::getFromPoint).thenComparingInt(Move::getToPoint));

            String sequenceKey = generateSequenceKey(sortedSequence);

            if (!uniqueSequences.contains(sequenceKey)) {
                uniqueSequences.add(sequenceKey);
                movesByDiceUsed.computeIfAbsent(diceUsed, k -> new ArrayList<>())
                        .add(new ArrayList<>(sortedSequence));
            }
            maxDiceUsed = Math.max(maxDiceUsed, diceUsed);
        }

        return maxDiceUsed;
    }

    private List<List<Integer>> generateDiceCombinations(List<Integer> diceRolls) {
        List<List<Integer>> combinations = new ArrayList<>();

        // Generate all subsets of the dice rolls list
        int n = diceRolls.size();
        for (int i = 1; i < (1 << n); i++) {  // Start from 1 to exclude the empty subset
            List<Integer> combo = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    combo.add(diceRolls.get(j));
                }
            }
            combinations.add(combo);
        }

        return combinations;
    }

    private String generateSequenceKey(List<Move> moves) {
        List<String> moveStrings = new ArrayList<>();
        for (Move move : moves) {
            moveStrings.add(move.getFromPoint() + "-" + move.getToPoint());
        }
        Collections.sort(moveStrings);  // Sort moves as strings to ensure consistent order in key
        return String.join(";", moveStrings);  // Combine into a single string key
    }



    private List<Move> getPossibleMoves(Board board, int dieValue, Color playerColor) {
        List<Move> possibleMoves = new ArrayList<>();
        List<Point> points = board.getPoints();

        boolean canBearOff = canBearOff(board, playerColor);

        if (playerColor == Color.RED) {
            // Red moves from lower points to higher points
            for (int i = 0; i < points.size(); i++) {
                Point point = points.get(i);

                if (point.hasCheckers() && point.getTopCheckerColor() == playerColor) {
                    int fromPoint = i;
                    int toPoint = i + dieValue;

                    // Check for bearing off
                    if (canBearOff && fromPoint >= 18 && toPoint >= 24) {
                        possibleMoves.add(new Move(fromPoint, -1, playerColor));  // -1 indicates bearing off for Red
                    } else if (toPoint < 24) {  // Normal move within the board
                        Point destinationPoint = points.get(toPoint);
                        if (isLegalMove(destinationPoint, playerColor)) {
                            possibleMoves.add(new Move(fromPoint, toPoint, playerColor));
                        }
                    }
                }
            }
        } else if (playerColor == Color.BLUE) {
            // Blue moves from higher points to lower points
            for (int i = points.size() - 1; i >= 0; i--) {
                Point point = points.get(i);

                if (point.hasCheckers() && point.getTopCheckerColor() == playerColor) {
                    int fromPoint = i;
                    int toPoint = i - dieValue;

                    // Check for bearing off
                    if (canBearOff && fromPoint <= 5 && toPoint < 0) {
                        possibleMoves.add(new Move(fromPoint, -2, playerColor));  // -2 indicates bearing off for Blue
                    } else if (toPoint >= 0) {  // Normal move within the board
                        Point destinationPoint = points.get(toPoint);
                        if (isLegalMove(destinationPoint, playerColor)) {
                            possibleMoves.add(new Move(fromPoint, toPoint, playerColor));
                        }
                    }
                }
            }
        }

        return possibleMoves;
    }


    private boolean canBearOff(Board board, Color playerColor) {
        List<Point> points = board.getPoints();
        int homeStart = (playerColor == Color.RED) ? 18 : 0;  // Red’s home board is points 18-23, Blue’s is 0-5
        int homeEnd = homeStart + 5;

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (point.hasCheckers() && point.getTopCheckerColor() == playerColor) {
                if (i < homeStart || i > homeEnd) {
                    return false;  // Checker outside home board
                }
            }
        }
        return true;
    }


    private boolean isLegalMove(Point destinationPoint, Color playerColor) {
        if (!destinationPoint.hasCheckers()) {
            return true;
        } else {
            Color destinationColor = destinationPoint.getTopCheckerColor();
            if (destinationColor == playerColor) {
                return true;
            } else {
                int numCheckers = destinationPoint.getNumberOfCheckers();
                return numCheckers == 1;
            }
        }
    }

    private void applyMove(Board board, Move move) {
        int fromPointIndex = move.getFromPoint();
        int toPointIndex = move.getToPoint();
        Color playerColor = move.getPlayerColor();

        List<Point> points = board.getPoints();
        Point fromPoint = points.get(fromPointIndex);

        // Check if fromPoint has a checker to move
        // TODO: remove printing and move to view
        if (!fromPoint.hasCheckers()) {
            System.out.println("Error: No checkers to move from point " + fromPointIndex);
            return;
        }

        Checker movingChecker = fromPoint.removeTopChecker();

        // Bearing off logic
        if (toPointIndex == -1 || toPointIndex == -2) {
            // Add checker to the Off stack for the player's color
            board.getOff().addChecker(movingChecker);
        } else {
            // Normal move within the board
            Point toPoint = points.get(toPointIndex);

            // Check if we are hitting an opponent's checker
            if (toPoint.hasCheckers() && toPoint.getTopCheckerColor() != playerColor && toPoint.getNumberOfCheckers() == 1) {
                Checker opponentChecker = toPoint.removeTopChecker();
                board.getBar().addChecker(opponentChecker);
            }

            toPoint.addChecker(movingChecker);
        }
    }

}