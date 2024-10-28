package backgammon.board;

import java.util.Stack;

public class Point {
    private final Stack<Checker> checkerStack;

    public Point() {
        this.checkerStack = new Stack<>();
    }

    public Stack<Checker> getCheckerStackCopy() {
        Stack<Checker> copiedStack = new Stack<>();
        copiedStack.addAll(checkerStack);

        return copiedStack;
    }

    public void addChecker(Checker checker) {
        checkerStack.push(checker);
    }
}
