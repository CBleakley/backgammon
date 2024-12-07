package backgammon.playerInput;

public class RollCommand implements PlayerInput {

    public static RollCommand parse(String string) {
        if(string == null) return null;
        if (string.equalsIgnoreCase("roll")) return new RollCommand();
        if (string.equalsIgnoreCase("r")) return new RollCommand();
        return null;
    }
}
