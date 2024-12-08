/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.input;

import java.util.Scanner;

/**
 * An implementation of the {@code InputSource} interface that reads input from the console.
 * Uses a {@code Scanner} to retrieve user input line-by-line.
 */
public class ConsoleInputSource implements InputSource {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Retrieves the next line of input from the console.
     *
     * @return the input entered by the user as a {@code String}
     */
    @Override
    public String getInput() {
        return scanner.nextLine(); // Read a line from the console
    }
}
