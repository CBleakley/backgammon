package backgammon.input;

import java.util.Scanner;

public class ConsoleInputSource implements InputSource {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getInput() {
        return scanner.nextLine(); // Read a line from the console
    }
}
