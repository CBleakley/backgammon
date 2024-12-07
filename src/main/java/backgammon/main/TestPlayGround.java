package backgammon.main;

import backgammon.board.Board;
import backgammon.board.Checker;
import backgammon.board.Color;
import backgammon.gameLogic.Move;
import backgammon.gameLogic.MoveGenerator;

import java.util.ArrayList;
import java.util.List;

public class TestPlayGround {
    public static void main(String[] args) {
        Board board = new Board();

        Checker blueChecker1 = new Checker(Color.BLUE);


        board.getPoints().get(1).addChecker(blueChecker1);

        List<Integer> dice = new ArrayList<>();
        dice.add(4);
        dice.add(5);

        List<List<Move>> moves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, Color.BLUE);
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
