package backgammon.playerInput;

public class DoubleCommand implements PlayerInput {
    public static DoubleCommand parse(String string) {
        if (string == null) return null;
        return (string.equalsIgnoreCase("double")) ? new DoubleCommand() : null;
    }
}
