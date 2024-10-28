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
}
