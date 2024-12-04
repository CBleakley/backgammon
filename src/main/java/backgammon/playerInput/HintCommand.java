package backgammon.playerInput;

public class HintCommand implements PlayerInput{

    public static HintCommand parse(String string) {
        if(string == null) return null;
        return string.equalsIgnoreCase("hint") ? new HintCommand() : null;
    }
}
