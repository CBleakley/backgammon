/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.board;

import java.util.EnumMap;
import java.util.Stack;

/**
 * Represents a bar in a backgammon game that holds checkers of different colors.
 */
public class Bar {
    private final EnumMap<Color, Stack<Checker>> checkerStacks;

    /**
     * Initializes a new Bar instance with separate stacks for each color of checkers.
     */
    public Bar() {
        this.checkerStacks = new EnumMap<>(Color.class);
        this.checkerStacks.put(Color.RED, new Stack<>());
        this.checkerStacks.put(Color.BLUE, new Stack<>());
    }

    /**
     * Retrieves the stack of checkers for a specific color.
     *
     * @param color the color of the checkers stack to retrieve
     * @return the stack of checkers for the specified color
     */
    public Stack<Checker> getBarOfColor(Color color) {
        return this.checkerStacks.get(color);
    }

    /**
     * Adds a checker to the appropriate stack in the bar based on its color.
     *
     * @param checker the checker to add to the bar
     */
    public void addChecker(Checker checker) {
        Color color = checker.getColor();
        this.checkerStacks.get(color).push(checker);
    }

    /**
     * Creates a deep copy of the current Bar instance.
     *
     * @return a new Bar instance that is a deep copy of the current bar
     */
    public Bar cloneBar() {
        Bar clonedBar = new Bar();
        
        for (Color color : this.checkerStacks.keySet()) {
            Stack<Checker> originalStack = this.checkerStacks.get(color);
            Stack<Checker> clonedStack = new Stack<>();
            clonedStack.addAll(originalStack);
            clonedBar.checkerStacks.put(color, clonedStack);
        }

        return clonedBar;
    }
}
