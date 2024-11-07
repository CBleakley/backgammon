package backgammon.gameLogic;

import backgammon.Dice.DicePair;
import backgammon.Dice.Die;
import backgammon.PlayerInput.PlayerInput;
import backgammon.PlayerInput.QuitCommand;
import backgammon.PlayerInput.RollCommand;
import backgammon.board.Board;
import backgammon.board.Checker;
import backgammon.board.Color;
import backgammon.board.Point;
import backgammon.player.Player;
import backgammon.view.View;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final View view;
    private final Board board;
    private final DicePair dice;

    private final Player player1;
    private final Player player2;

    private Player nextToPlay;
    private List<Integer> nextRollToPlay;

    private boolean gameOver = false;

    public Game(View view, Player player1, Player player2) {
        this.view = view;
        this.board = new Board();
        this.dice = new DicePair();

        this.player1 = player1;
        this.player2 = player2;

        this.nextRollToPlay = new ArrayList<>();
    }

    public void play() {
        makeInitialRolls();
        // Initial rolls are used, this was modified in the turn() method
        view.displayBoard(board, nextRollToPlay);

        do {
            turn();
            displayPipCounts();
            passToNextPlayer();
        } while(!gameOver);
    }

    public void makeInitialRolls() {
        Die die = new Die();
        int player1Roll;
        int player2Roll;
        do {
            player1Roll = die.roll();
            view.displayInitialRoll(player1, player1Roll);

            player2Roll = die.roll();
            view.displayInitialRoll(player2, player2Roll);

            if(player1Roll == player2Roll) {
                view.displayRollAgain();
            }
        } while(player1Roll == player2Roll);

        nextToPlay = (player1Roll > player2Roll) ? player1 : player2;

        view.displayXPlaysFirst(nextToPlay);
        setNextRollToPlay(player1Roll, player2Roll);

    }

    private void setNextRollToPlay(int dice1, int dice2) {
        nextRollToPlay.clear();

        if(dice1 != dice2) {
            nextRollToPlay.add(dice1);
            nextRollToPlay.add(dice2);
            return;
        }

        nextRollToPlay.add(dice1);
        nextRollToPlay.add(dice1);
        nextRollToPlay.add(dice1);
        nextRollToPlay.add(dice1);
    }

    public void turn() {
        // Check if we need to roll, which should only happen after the first turn
        if (nextRollToPlay.isEmpty()) {
            PlayerInput playerInput = view.getPlayerInput(nextToPlay);
            if (playerInput instanceof QuitCommand) {
                gameOver = true;
                return;
            }

            if (playerInput instanceof RollCommand) {
                roll();
            }
        }

        // Generate possible moves with the current dice values in nextRollToPlay
        MoveGenerator moveGenerator = new MoveGenerator();
        List<List<Move>> possibleMoveSequences = moveGenerator.generateAllPossibleMoveSequences(board, nextRollToPlay, nextToPlay.getColor());

        if (possibleMoveSequences.isEmpty()) {
            view.displayNoMovesAvailable(nextToPlay);
        } else {
            view.displayPossibleMoves(possibleMoveSequences);

            // Prompt player to select a move sequence
            int selectedOption = view.promptMoveSelection(possibleMoveSequences.size());

            if (selectedOption >= 0 && selectedOption < possibleMoveSequences.size()) {
                List<Move> selectedMoves = possibleMoveSequences.get(selectedOption);

                // Apply the selected moves to the actual game board
                for (Move move : selectedMoves) {
                    applyMove(board, move);
                }

                view.displayBoard(board, nextRollToPlay);
            } else {
                System.out.println("Invalid selection. No moves applied.");
                // TODO: if invalid selection, go back to start of if statement and ask user again, until valid selection made
            }
        }

        nextRollToPlay.clear();
    }


    private void applyMove(Board board, Move move) {
        // Same logic as in MoveGenerator.applyMove
        int fromPointIndex = move.getFromPoint();
        int toPointIndex = move.getToPoint();
        Color playerColor = move.getPlayerColor();

        List<Point> points = board.getPoints();
        Point fromPoint = points.get(fromPointIndex);
        Point toPoint = points.get(toPointIndex);

        Checker movingChecker = fromPoint.removeTopChecker();

        if (toPoint.hasCheckers() && toPoint.getTopCheckerColor() != playerColor && toPoint.getNumberOfCheckers() == 1) {
            Checker opponentChecker = toPoint.removeTopChecker();
            board.getBar().addChecker(opponentChecker);
        }

        toPoint.addChecker(movingChecker);
    }
    public int calculatePipCount(Color color) {
        PipCounter pipCounter = new PipCounter(board);
        return pipCounter.calculatePipCount(color);
    }

    public void displayPipCounts() {
        int redPipCount = calculatePipCount(Color.RED);
        int bluePipCount = calculatePipCount(Color.BLUE);

        view.displayPipCount(redPipCount, bluePipCount);
    }

    public void passToNextPlayer() {
        nextToPlay = (nextToPlay == player1) ? player2 : player1;
    }

    private void roll() {
        List<Integer> diceFaceValue = dice.roll();
        setNextRollToPlay(diceFaceValue.getFirst(), diceFaceValue.getLast());
        view.displayRoll(nextRollToPlay);
    }
}
