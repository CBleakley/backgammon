/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.board;

import java.util.EnumMap;
import java.util.Stack;

/**
 * Represents the off area in a backgammon game where checkers are placed after being borne off.
 * Each player has a separate stack of checkers in the off area.
 */
public class Off {
    private final EnumMap<Color, Stack<Checker>> checkerStacks;

    /**
     * Initializes a new Off area with separate stacks for each color of checkers.
     */
    public Off() {
        this.checkerStacks = new EnumMap<>(Color.class);
        this.checkerStacks.put(Color.RED, new Stack<>());
        this.checkerStacks.put(Color.BLUE, new Stack<>());
    }

    /**
     * Retrieves the stack of checkers in the off area for a specific color.
     *
     * @param color the color of the checkers to retrieve
     * @return the stack of checkers in the off area for the specified color
     */
    public Stack<Checker> getOffOfColor(Color color) {
        return this.checkerStacks.get(color);
    }

    /**
     * Adds a checker to the appropriate stack in the off area based on its color.
     *
     * @param checker the checker to add to the off area
     */
    public void addChecker(Checker checker) {
        Color color = checker.getColor();
        this.checkerStacks.get(color).push(checker);
    }

    /**
     * Creates a deep copy of the current Off instance.
     *
     * @return a new Off instance that is a deep copy of the current off area
     */
    public Off cloneOff() {
        Off clonedOff = new Off();

        for (Color color : this.checkerStacks.keySet()) {
            Stack<Checker> originalStack = this.checkerStacks.get(color);
            Stack<Checker> clonedStack = new Stack<>();
            clonedStack.addAll(originalStack);
            clonedOff.checkerStacks.put(color, clonedStack);
        }

        return clonedOff;
    }
}
