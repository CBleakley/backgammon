package backgammon.view;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ColorANSICodesTest {

    @Test
    void should_HaveCorrectRedCode() {
        // Accessing ColorANSICodes.RED
        String expected = "\033[31m";
        assertEquals(expected, ColorANSICodes.RED, "RED ANSI code should match");
    }

    @Test
    void should_HaveCorrectBlueCode() {
        // Accessing ColorANSICodes.BLUE
        String expected = "\033[34m";
        assertEquals(expected, ColorANSICodes.BLUE, "BLUE ANSI code should match");
    }

    @Test
    void should_HaveCorrectResetCode() {
        // Accessing ColorANSICodes.RESET
        String expected = "\033[0m";
        assertEquals(expected, ColorANSICodes.RESET, "RESET ANSI code should match");
    }
}
