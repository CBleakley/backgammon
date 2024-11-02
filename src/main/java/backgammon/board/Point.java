package backgammon.board;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Point {
    private final Stack<Checker> checkerStack;

    public Point() {
        this.checkerStack = new Stack<>();
    }

    public List<Checker> getCheckers() {
        return Collections.unmodifiableList(checkerStack);
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
