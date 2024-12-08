/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

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

/**
 * This class represents the core logic of the backgammon game, including rolling dice,
 * handling player commands, generating moves, and managing game state transitions.
 */
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
        //board.initialiseStartingPosition();
        board.initialiseStartingPosition();

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

    /**
     * Handles the initial rolls for both players to determine who plays first.
     * If both players roll the same value, they roll again until a winner is decided.
     */
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
        } while (player1Roll == player2Roll);   // Keep rolling until a player rolls a higher number than the other

        // Player who rolls the higher number is first to play
        nextToPlay = (player1Roll > player2Roll) ? player1 : player2;

        view.displayWhoPlaysFirst(nextToPlay);
        setNextRollToPlay(player1Roll, player2Roll);
    }

    /**
     * Sets the next roll to play based on the given dice values.
     * Handles special cases like doubles.
     *
     * @param dice1 the value of the first die
     * @param dice2 the value of the second die
     */
    private void setNextRollToPlay(int dice1, int dice2) {
        nextRollToPlay.clear();

        if (dice1 == dice2) {   // If the player rolls doubles they get 4 of that dice roll
            for (int i = 0; i < 4; i++) {
                nextRollToPlay.add(dice1);
            }
            return;
        }

        nextRollToPlay.add(dice1);
        nextRollToPlay.add(dice2);
    }

    /**
     * Handles a single turn of the game, processing player commands and determining
     * if a game-winning condition is met.
     *
     * @return a {@code GameWinner} object if the game is won during the turn, otherwise {@code null}
     */
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

                if (gameWinner != null) return gameWinner;  // Player won the game
            } else {
                view.display("Invalid command. Please try again.");
            }
        }

        // Generate possible moves
        List<List<Move>> possibleMoveSequences = MoveGenerator.generateAllPossibleMoveSequences(board, nextRollToPlay, nextToPlay.color());

        // Check if there are no possible moves
        if (possibleMoveSequences.size() == 1 && possibleMoveSequences.getFirst().isEmpty()) {
            view.displayNoMovesAvailable(nextToPlay);
            nextRollToPlay.clear();
            return null;
        }

        // Check if there is only one valid move that the player is forced to play
        if (possibleMoveSequences.size() == 1) {
            List<Move> onlyPossibleMoves = possibleMoveSequences.getFirst();
            view.displayOnlyOnePossibleMove(onlyPossibleMoves);

            for (Move move : onlyPossibleMoves) {
                MoveExecutor.executeMove(board, move);
            }

            displayBoardNoRoll();
            nextRollToPlay.clear();

            return GameWinChecker.checkGameWon(board.cloneBoard(), doubleDice, player1, player2);
        }

        // Prompt player to select a move sequence
        view.displayPossibleMoves(possibleMoveSequences);
        int selectedOption = view.promptMoveSelection(possibleMoveSequences.size());

        List<Move> selectedMoves = possibleMoveSequences.get(selectedOption);
        for (Move move : selectedMoves) {   // Execute the selected move sequence
            MoveExecutor.executeMove(board, move);
        }

        displayBoardNoRoll();   // Display board after the move is executed

        nextRollToPlay.clear();
        return GameWinChecker.checkGameWon(board.cloneBoard(), doubleDice, player1, player2);
    }

    /**
     * Handles a test command, to read following actions from a .txt file.
     *
     * @param testCommand the test command containing the filename for input
     */
    private void handleTestCommand(TestCommand testCommand) {
        String filename = testCommand.getFilename();
        view.setInputSource(filename); // Pass the filename directly
    }


    /**
     * Calculates the pip count for a player based on their color.
     *
     * @param color the color of the player
     * @return the total pip count for the player
     */
    private int calculatePipCount(Color color) {
        return PipCounter.calculatePipCount(board, color);
    }

    /**
     * Displays the current pip counts for both players on the board.
     */
    private void displayPipCounts() {
        int redPipCount = calculatePipCount(Color.RED);
        int bluePipCount = calculatePipCount(Color.BLUE);

        view.displayPipCount(redPipCount, bluePipCount);
    }

    /**
     * Displays a hint of the valid commands a player can make.
     * If the player whose turn it is or no one has possession of the double dice include this.
     */
    private void displayHint() {
        Player doubleDiceOwner = doubleDice.getOwner();
        view.displayHint(doubleDiceOwner == null || doubleDiceOwner == nextToPlay);
    }

    /**
     * Offers the doubling die to the opponent and handles their decision.
     *
     * @return a {@code GameWinner} object if the opponent refuses the double, otherwise {@code null}
     */
    private GameWinner offerDouble() {
        // Check if the player whose turn it is can offer the double dice
        if (doubleDice.getOwner() != null && nextToPlay != doubleDice.getOwner()) {
            view.cannotOfferDouble(doubleDice.getOwner());
            return null;
        }

        // Get the offeree's decision to accept or refuse
        Player offerRecipient = (nextToPlay == player1) ? player2 : player1;
        boolean accepted = view.getDoubleDecision(offerRecipient);

        if (accepted) {
            doubleDice.updateMultiplier();
            doubleDice.setOwner(offerRecipient);    // Oferee now possesses the double dice
            return null;
        }

        return new GameWinner(nextToPlay, doubleDice.getMultiplier(), EndingType.DOUBLE_REFUSED);
    }

    /**
     * Passes the turn to the next player.
     */
    private void passToNextPlayer() {
        nextToPlay = (nextToPlay == player1) ? player2 : player1;
    }

    /**
     * Rolls the dice and sets the next roll to play.
     */
    private void roll() {
        List<Integer> diceFaceValue = dice.roll();
        setNextRollToPlay(diceFaceValue.get(0), diceFaceValue.get(1));
    }

    /**
     * Displays the game board without including dice roll information.
     */
    private void displayBoardNoRoll() {
        view.displayBoard(board.cloneBoard(), null, null, null, player1, matchScore.get(player1),
                player2, matchScore.get(player2), matchLength, doubleDice);
    }

    /**
     * Displays the game board with the current roll information.
     */
    private void displayBoardWithRoll() {
        int pipCount = calculatePipCount(nextToPlay.color());
        view.displayBoard(board.cloneBoard(), nextRollToPlay, nextToPlay, pipCount, player1, matchScore.get(player1),
                player2, matchScore.get(player2), matchLength, doubleDice);
    }
}
