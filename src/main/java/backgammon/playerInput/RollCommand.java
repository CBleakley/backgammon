package backgammon.playerInput;

public class RollCommand implements PlayerInput {

    public static RollCommand parse(String string) {
        if(string == null) return null;
        return string.equalsIgnoreCase("roll") ? new RollCommand() : null;
    }
}
