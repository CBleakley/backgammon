/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.board;

import java.util.EnumMap;
import java.util.Stack;

public class Off {
    private final EnumMap<Color, Stack<Checker>> checkerStacks;

    public Off() {
        this.checkerStacks = new EnumMap<>(Color.class);
        this.checkerStacks.put(Color.RED, new Stack<>());
        this.checkerStacks.put(Color.BLUE, new Stack<>());
    }

    public Stack<Checker> getOffOfColor(Color color) {
        return this.checkerStacks.get(color);
    }

    public void addChecker(Checker checker) {
        Color color = checker.getColor();
        this.checkerStacks.get(color).push(checker);
    }

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
