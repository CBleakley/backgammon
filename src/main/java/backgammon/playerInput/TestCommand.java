package backgammon.playerInput;

public class TestCommand implements PlayerInput {
    private final String filename;

    public TestCommand(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

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
