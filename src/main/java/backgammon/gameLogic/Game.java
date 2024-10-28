package backgammon.gameLogic;

import backgammon.Dice.DicePair;
import backgammon.Dice.Die;
import backgammon.PlayerInput.PlayerInput;
import backgammon.PlayerInput.QuitCommand;
import backgammon.PlayerInput.RollCommand;
import backgammon.board.Board;
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
        // TODO: make the player who plays first use the initial roll before loop.

        do {
            turn();
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

    private void turn() {
        PlayerInput playerInput = view.getPlayerInput(nextToPlay);
        if (playerInput instanceof QuitCommand) {
            gameOver = true;
            return;
        }

        if (playerInput instanceof RollCommand) {
            roll();
        }

        view.displayBoard(board, nextRollToPlay);
        System.out.println(nextToPlay.getName() + " plays their move"); // TODO: Replace with actual move logic
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
