package backgammon.PlayerInput;

public class PipCommand implements PlayerInput {

    public static PipCommand parse(String string) {
        if(string == null) return null;
        return string.equalsIgnoreCase("pip") ? new PipCommand() : null;
    }
}
