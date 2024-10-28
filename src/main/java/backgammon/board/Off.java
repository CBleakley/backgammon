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
        return checkerStacks.get(color);
    }
}
