/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.board;

import java.util.EnumMap;
import java.util.Stack;

public class Bar {
    private final EnumMap<Color, Stack<Checker>> checkerStacks;

    public Bar() {
        this.checkerStacks = new EnumMap<>(Color.class);
        this.checkerStacks.put(Color.RED, new Stack<>());
        this.checkerStacks.put(Color.BLUE, new Stack<>());
    }

    public Stack<Checker> getBarOfColor(Color color) {
        return this.checkerStacks.get(color);
    }

    // Adds a checker to the bar based on its color
    public void addChecker(Checker checker) {
        Color color = checker.getColor();
        this.checkerStacks.get(color).push(checker);
    }

    // Creates a deep copy of the Bar
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
