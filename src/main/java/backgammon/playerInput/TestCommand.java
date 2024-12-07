package backgammon.playerInput;

public class TestCommand implements PlayerInput {
    private final String filename;

    private TestCommand(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public static TestCommand parse(String input) {
        if (input == null) return null;
        if (input.startsWith("test ")) {
            String filename = input.substring(5).trim();
            if (!filename.isEmpty()) {
                return new TestCommand(filename);
            }
        }
        return null;
    }
}
