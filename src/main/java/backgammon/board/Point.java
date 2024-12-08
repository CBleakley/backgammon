/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.board;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Represents a point on the backgammon board, which can hold a stack of checkers.
 */
public class Point {
    private final Stack<Checker> checkerStack;

    /**
     * Initializes a new, empty Point.
     */
    public Point() {
        this.checkerStack = new Stack<>();
    }

    /**
     * Retrieves an unmodifiable list of checkers currently on the point.
     *
     * @return an unmodifiable list of checkers
     */
    public List<Checker> getCheckers() {
        return Collections.unmodifiableList(checkerStack);
    }

    /**
     * Checks whether there are any checkers on the point.
     *
     * @return {@code true} if the point has checkers, {@code false} otherwise
     */
    public boolean hasCheckers() {
        return !checkerStack.isEmpty();
    }

    /**
     * Retrieves the color of the checker at the top of the stack.
     *
     * @return the color of the top checker, or {@code null} if the stack is empty
     */
    public Color getTopCheckerColor() {
        if (!checkerStack.isEmpty()) {
            return checkerStack.peek().getColor();
        }
        return null;
    }

    /**
     * Retrieves the number of checkers currently on the point.
     *
     * @return the number of checkers on the point
     */
    public int getNumberOfCheckers() {
        return checkerStack.size();
    }

    /**
     * Adds a checker to the stack on this point.
     *
     * @param checker the checker to add
     */
    public void addChecker(Checker checker) {
        checkerStack.push(checker);
    }

    /**
     * Removes and returns the checker at the top of the stack.
     *
     * @return the top checker, or {@code null} if the stack is empty
     */
    public Checker removeTopChecker() {
        if (!checkerStack.isEmpty()) {
            return checkerStack.pop();
        }
        return null;
    }

    /**
     * Creates and returns a copy of the stack of checkers on this point.
     *
     * @return a copy of the checker stack
     */
    public Stack<Checker> getCheckerStackCopy() {
        Stack<Checker> copiedStack = new Stack<>();
        copiedStack.addAll(checkerStack);
        return copiedStack;
    }

    /**
     * Creates a deep copy of the current Point instance.
     *
     * @return a new Point instance that is a deep copy of this point
     */
    public Point clonePoint() {
        Point clonedPoint = new Point();
        clonedPoint.checkerStack.addAll(this.checkerStack);
        return clonedPoint;
    }

}
