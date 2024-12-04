package playerInputTests;

import backgammon.playerInput.TestCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestCommandTests {

    @Test
    void testParse_ValidInput() {
        // Test with valid "test" inputs
        TestCommand command = TestCommand.parse("test file.txt");
        assertNotNull(command, "Expected TestCommand object for input 'test file.txt'");
        assertEquals("file.txt", command.getFilename(), "Filename should be 'file.txt'");

        command = TestCommand.parse("test testfile");
        assertNotNull(command, "Expected TestCommand object for input 'test testfile'");
        assertEquals("testfile", command.getFilename(), "Filename should be 'testfile'");

        command = TestCommand.parse("test  anotherfile.json");
        assertNotNull(command, "Expected TestCommand object for input 'test  anotherfile.json'");
        assertEquals("anotherfile.json", command.getFilename(), "Filename should be 'anotherfile.json'");
    }

    @Test
    void testParse_NullInput() {
        // Test with null input
        TestCommand command = TestCommand.parse(null);
        assertNull(command, "Expected null for null input");
    }

    @Test
    void testParse_InvalidPrefix() {
        // Test with inputs not starting with "test "
        TestCommand command = TestCommand.parse("notatest file.txt");
        assertNull(command, "Expected null for input 'notatest file.txt'");
    }

    @Test
    void testParse_EmptyFilename() {
        // Test with "test " but no filename
        TestCommand command = TestCommand.parse("test ");
        assertNull(command, "Expected null for input 'test '");

        command = TestCommand.parse("test");
        assertNull(command, "Expected null for input 'test'");
    }

    @Test
    void testParse_ExtraSpaces() {
        // Test with extra spaces around the filename
        TestCommand command = TestCommand.parse("test    spacedfile.txt   ");
        assertNotNull(command, "Expected TestCommand object for input 'test    spacedfile.txt   '");
        assertEquals("spacedfile.txt", command.getFilename(), "Filename should be 'spacedfile.txt'");
    }

    @Test
    void testParse_FilenameWithSpecialCharacters() {
        // Test with a filename containing special characters
        TestCommand command = TestCommand.parse("test file_with-special.characters123");
        assertNotNull(command, "Expected TestCommand object for input 'test file_with-special.characters123'");
        assertEquals("file_with-special.characters123", command.getFilename(), "Filename should be 'file_with-special.characters123'");
    }
}
