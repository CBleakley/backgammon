package backgammon.board;

import java.util.Stack;

public class Point {
    private final Stack<Checker> checkerStack;

    public Point() {
        this.checkerStack = new Stack<>();
    }

    public void addChecker(Checker checker) {
        checkerStack.push(checker);
    }
}
