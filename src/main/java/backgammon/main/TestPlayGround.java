package backgammon.main;

import backgammon.board.Board;
import backgammon.board.Checker;
import backgammon.board.Color;
import backgammon.gameLogic.Move;
import backgammon.gameLogic.NewMoveGenerator;

import java.util.ArrayList;
import java.util.List;

public class TestPlayGround {
    public static void main(String[] args) {
        Board board = new Board();

        Checker blueChecker1 = new Checker(Color.BLUE);
        Checker blueChecker2 = new Checker(Color.BLUE);
        Checker blueChecker3 = new Checker(Color.BLUE);
        Checker blueChecker4 = new Checker(Color.BLUE);

        board.getPoints().get(8).addChecker(blueChecker1);
        board.getPoints().get(8).addChecker(blueChecker2);
        board.getPoints().get(10).addChecker(blueChecker3);
        board.getPoints().get(10).addChecker(blueChecker4);

        List<Integer> dice = new ArrayList<>();
        dice.add(2);
        dice.add(2);
        dice.add(2);
        dice.add(2);

        List<List<Move>> moves = NewMoveGenerator.generateAllPossibleMoveSequences(board, dice, Color.BLUE);
//        List<Move> sequence1 = new ArrayList<>();
//        sequence1.add(new Move(3, 4, Color.BLUE));
//        sequence1.add(new Move(7, 8, Color.BLUE));
//
//        List<Move> sequence2 = new ArrayList<>();
//        sequence2.add(new Move(7, 8, Color.BLUE));
//        sequence2.add(new Move(3, 4, Color.BLUE));
//
//        System.out.println(NewMoveGenerator.areSequencesDuplicates(sequence1, sequence2));

    }
}
