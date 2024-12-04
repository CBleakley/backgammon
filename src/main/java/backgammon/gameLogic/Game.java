package backgammon.gameLogic;

import backgammon.Dice.DicePair;
import backgammon.Dice.Die;
import backgammon.Dice.DoubleDice;
import backgammon.playerInput.*;
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

    private final Map<Player, Integer> matchScore;
    private final int matchLength;

    private final DoubleDice doubleDice;

    public Game(View view, Player player1, Player player2, Map<Player, Integer> matchScore, int matchLength) {
        this.view = view;
        this.board = new Board();
        this.dice = new DicePair();
        this.doubleDice = new DoubleDice();

        this.player1 = player1;
        this.player2 = player2;

        this.matchScore = matchScore;
        this.matchLength = matchLength;

        this.nextRollToPlay = new ArrayList<>();
    }

    public GameWinner play() {
        makeInitialRolls();
        displayBoardWithRoll();

        do {
            GameWinner gameWinner = turn();
            if (gameWinner != null) {
                return gameWinner;
            }
            passToNextPlayer();
        } while (!gameQuit);

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

            if (player1Roll == player2Roll) {
                view.displayRollAgain();
            }
        } while (player1Roll == player2Roll);

        nextToPlay = (player1Roll > player2Roll) ? player1 : player2;

        view.displayWhoPlaysFirst(nextToPlay);
        setNextRollToPlay(player1Roll, player2Roll);
    }

    private void setNextRollToPlay(int dice1, int dice2) {
        nextRollToPlay.clear();

        if (dice1 == dice2) {
            for (int i = 0; i < 4; i++) {
                nextRollToPlay.add(dice1);
            }
            return;
        }

        nextRollToPlay.add(dice1);
        nextRollToPlay.add(dice2);
    }

    private GameWinner turn() {
        while (nextRollToPlay.isEmpty()) {
            PlayerInput playerInput = view.getPlayerInput(nextToPlay);
            if (playerInput instanceof QuitCommand) {
                gameQuit = true;
                return null;
            } else if (playerInput instanceof TestCommand testCommand) {
                handleTestCommand(testCommand);
            } else if (playerInput instanceof RollCommand) {
                roll();
                displayBoardWithRoll();
            } else if (playerInput instanceof SetDiceCommand setDiceCommand) {
                setNextRollToPlay(setDiceCommand.getDice1(), setDiceCommand.getDice2());
                displayBoardWithRoll();
            } else if (playerInput instanceof PipCommand) {
                displayPipCounts();
            } else if (playerInput instanceof HintCommand) {
                displayHint();
            } else if (playerInput instanceof DoubleCommand) {
                GameWinner gameWinner = offerDouble();
                if (gameWinner != null) return gameWinner;
            } else {
                view.display("Invalid command. Please try again.");
            }
        }

        // Generate possible moves
        List<List<Move>> possibleMoveSequences = MoveGenerator.generateAllPossibleMoveSequences(board, nextRollToPlay, nextToPlay.color());

        if (possibleMoveSequences.isEmpty() || (possibleMoveSequences.size() == 1 && possibleMoveSequences.get(0).isEmpty())) { // Does the job to check if there are no possible moves
            view.displayNoMovesAvailable(nextToPlay);
            nextRollToPlay.clear();
            return null;
        }

        if (possibleMoveSequences.size() == 1) {
            List<Move> onlyPossibleMoves = possibleMoveSequences.get(0);
            view.displayOnlyOnePossibleMove(onlyPossibleMoves);
            for (Move move : onlyPossibleMoves) {
                MoveExecutor.executeMove(board, move);
            }
            displayBoardNoRoll();
            nextRollToPlay.clear();
            return GameWinChecker.checkGameWon(board.cloneBoard(), doubleDice, player1, player2);
        }

        view.displayPossibleMoves(possibleMoveSequences);

        // Prompt player to select a move sequence
        int selectedOption = view.promptMoveSelection(possibleMoveSequences.size());

        List<Move> selectedMoves = possibleMoveSequences.get(selectedOption);
        for (Move move : selectedMoves) {
            MoveExecutor.executeMove(board, move);
        }

        displayBoardNoRoll();

        nextRollToPlay.clear();
        return GameWinChecker.checkGameWon(board.cloneBoard(), doubleDice, player1, player2);
    }

    private void handleTestCommand(TestCommand testCommand) {
        String filename = testCommand.getFilename();
        view.setInputSource(filename); // Pass the filename directly
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
        Player doubleDiceOwner = doubleDice.getOwner();
        view.displayHint(doubleDiceOwner == null || doubleDiceOwner == nextToPlay);
    }

    private GameWinner offerDouble() {
        if (doubleDice.getOwner() != null && nextToPlay != doubleDice.getOwner()) {
            view.cannotOfferDouble(doubleDice.getOwner());
            return null;
        }

        Player offerRecipient = (nextToPlay == player1) ? player2 : player1;
        boolean accepted = view.getDoubleDecision(offerRecipient);

        if (accepted) {
            doubleDice.updateMultiplier();
            doubleDice.setOwner(offerRecipient);
            return null;
        }

        return new GameWinner(nextToPlay, doubleDice.getMultiplier(), EndingType.DOUBLE_REFUSED);
    }

    private void passToNextPlayer() {
        nextToPlay = (nextToPlay == player1) ? player2 : player1;
    }

    private void roll() {
        List<Integer> diceFaceValue = dice.roll();
        setNextRollToPlay(diceFaceValue.get(0), diceFaceValue.get(1));
    }

    private void displayBoardNoRoll() {
        view.displayBoard(board.cloneBoard(), null, null, null, player1, matchScore.get(player1),
                player2, matchScore.get(player2), matchLength, doubleDice);
    }

    private void displayBoardWithRoll() {
        int pipCount = calculatePipCount(nextToPlay.color());
        view.displayBoard(board.cloneBoard(), nextRollToPlay, nextToPlay, pipCount, player1, matchScore.get(player1),
                player2, matchScore.get(player2), matchLength, doubleDice);
    }
}
