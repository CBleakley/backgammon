package backgammon.gameLogic;

import backgammon.Dice.DicePair;
import backgammon.Dice.Die;
import backgammon.PlayerInput.*;
import backgammon.board.*;
import backgammon.player.Player;
import backgammon.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game {
    private final View view;
    private final Board board;
    private final DicePair dice;

    private final Player player1;
    private final Player player2;

    private Player nextToPlay;
    private final List<Integer> nextRollToPlay;

    private boolean gameQuit = false;

    // Added fields for match score and match length
    private final Map<Player, Integer> matchScore;
    private final int matchLength;

    public Game(View view, Player player1, Player player2, Map<Player, Integer> matchScore, int matchLength) {
        this.view = view;
        this.board = new Board();
        this.dice = new DicePair();

        this.player1 = player1;
        this.player2 = player2;

        this.matchScore = matchScore;
        this.matchLength = matchLength;

        this.nextRollToPlay = new ArrayList<>();
    }

    public GameWinner play() {
        makeInitialRolls();
        int pipCount = calculatePipCount(nextToPlay.getColor());
        view.displayBoard(board.cloneBoard(), nextRollToPlay, nextToPlay, pipCount, matchScore.get(player1), matchScore.get(player2), matchLength);

        do {
            turn();
            GameWinner gameWinner = checkGameWon();
            if (gameWinner != null) { return gameWinner; }
            passToNextPlayer();
        } while(!gameQuit);

        return null;
    }

    private void makeInitialRolls() {
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

    private void turn() {
        // Check if we need to roll, which should only happen after the first turn
        while (nextRollToPlay.isEmpty()) {
            PlayerInput playerInput = view.getPlayerInput(nextToPlay);
            if (playerInput instanceof QuitCommand) {
                gameQuit = true;
                return;
            }

            if (playerInput instanceof RollCommand) {
                roll();
                int pipCount = calculatePipCount(nextToPlay.getColor());
                view.displayBoard(board.cloneBoard(), nextRollToPlay, nextToPlay, pipCount, matchScore.get(player1), matchScore.get(player2), matchLength);
            }

            if (playerInput instanceof PipCommand) {
                displayPipCounts();
            }

            if (playerInput instanceof HintCommand) {
                displayHint();
            }
        }

        // Generate possible moves with the current dice values in nextRollToPlay
        List<List<Move>> possibleMoveSequences = MoveGenerator.generateAllPossibleMoveSequences(board, nextRollToPlay, nextToPlay.getColor());

        if (possibleMoveSequences.isEmpty()) {
            view.displayNoMovesAvailable(nextToPlay);
            nextRollToPlay.clear();
            return;
        }

        if (possibleMoveSequences.size() == 1) {
            List<Move> onlyPossibleMoves = possibleMoveSequences.get(0);
            view.displayOnlyOnePossibleMove(onlyPossibleMoves);
            for (Move move : onlyPossibleMoves) {
                MoveExecutor.executeMove(board, move);
            }
            view.displayBoard(board.cloneBoard(), null, null, 0, matchScore.get(player1), matchScore.get(player2), matchLength);
            nextRollToPlay.clear();
            return;
        }

        view.displayPossibleMoves(possibleMoveSequences);

        // Prompt player to select a move sequence
        int selectedOption = view.promptMoveSelection(possibleMoveSequences.size());

        List<Move> selectedMoves = possibleMoveSequences.get(selectedOption);
        for (Move move : selectedMoves) {   // Apply the selected moves to the actual game board
            MoveExecutor.executeMove(board, move);
        }

        view.displayBoard(board.cloneBoard(), null, null, 0, matchScore.get(player1), matchScore.get(player2), matchLength);

        nextRollToPlay.clear();
    }

    private GameWinner checkGameWon() {
        int numberOfCheckersOffToWin = Board.NUMBER_OF_CHECKERS_PER_PLAYER;

        Board boardClone = board.cloneBoard();
        Off off = boardClone.getOff();
        if (off.getOffOfColor(player1.getColor()).size() == numberOfCheckersOffToWin) {
            return new GameWinner(player1, 1);
        }

        if (off.getOffOfColor((player2.getColor())).size() == numberOfCheckersOffToWin) {
            return new GameWinner(player2, 1);
        }

        return null;
    }

    private int calculatePipCount(Color color) {
        return PipCounter.calculatePipCount(board, color);
    }

    private void displayPipCounts() {
        int redPipCount = calculatePipCount(Color.RED);
        int bluePipCount = calculatePipCount(Color.BLUE);

        view.displayPipCount(redPipCount, bluePipCount);
    }

    private void displayHint() {
        view.displayHint();
    }

    private void passToNextPlayer() {
        nextToPlay = (nextToPlay == player1) ? player2 : player1;
    }

    private void roll() {
        List<Integer> diceFaceValue = dice.roll();
        setNextRollToPlay(diceFaceValue.get(0), diceFaceValue.get(1));
        view.displayRoll(nextToPlay, nextRollToPlay);
    }
}
