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

    public boolean hasCheckers() {
        return !checkerStack.isEmpty();
    }

    public Color getTopCheckerColor() {
        if (!checkerStack.isEmpty()) {
            return checkerStack.peek().getColor();
        }
        return null; // Or throw an exception if appropriate
    }

    public int getNumberOfCheckers() {
        return checkerStack.size();
    }

    public void addChecker(Checker checker) {
        checkerStack.push(checker);
    }

    public Checker removeTopChecker() {
        if (!checkerStack.isEmpty()) {
            return checkerStack.pop();
        }
        return null; // Or throw an exception
    }

    public Stack<Checker> getCheckerStackCopy() {
        Stack<Checker> copiedStack = new Stack<>();
        copiedStack.addAll(checkerStack);
        return copiedStack;
    }

    public Point clonePoint() {
        Point clonedPoint = new Point();
        clonedPoint.checkerStack.addAll(this.checkerStack);
        return clonedPoint;
    }

}
