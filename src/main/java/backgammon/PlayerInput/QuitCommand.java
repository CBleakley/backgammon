package backgammon.PlayerInput;

public class QuitCommand implements PlayerInput {

    public static QuitCommand parse(String string) {
        if(string == null) return null;
        return string.equalsIgnoreCase("quit") ? new QuitCommand() : null;
    }
}
