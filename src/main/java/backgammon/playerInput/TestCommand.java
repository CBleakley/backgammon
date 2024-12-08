/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.playerInput;

/**
 * Represents a command for executing a test using an input file in the Backgammon game.
 * Implements the {@code PlayerInput} interface.
 */
public class TestCommand implements PlayerInput {
    private final String filename;

    /**
     * Constructs a new {@code TestCommand} with the specified filename.
     *
     * @param filename the name of the file containing test commands
     */
    public TestCommand(String filename) {
        this.filename = filename;
    }

    /**
     * Returns the filename associated with this test command.
     *
     * @return the filename as a {@code String}
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Parses a string to determine if it represents a test command.
     * The string must start with "test" followed by a filename.
     *
     * @param input the input string to parse
     * @return a new {@code TestCommand} instance with the specified filename,
     *         or {@code null} if the input is invalid
     */
    public static TestCommand parse(String input) {
        if (input == null) return null;
        if (input.startsWith("test")) {
            String filename = input.substring(4).trim();

            if (!filename.isEmpty()) {
                return new TestCommand(filename);
            }
        }
        return null;
    }
}
