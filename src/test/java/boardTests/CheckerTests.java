package boardTests;

import backgammon.board.Checker;
import backgammon.board.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CheckerTests {

    @Test
    void testCheckerInitialization() {
        // Test Checker with Color.RED
        Checker redChecker = new Checker(Color.RED);
        assertEquals(Color.RED, redChecker.getColor(), "Checker color should be RED");

        // Test Checker with Color.BLUE
        Checker blueChecker = new Checker(Color.BLUE);
        assertEquals(Color.BLUE, blueChecker.getColor(), "Checker color should be BLUE");
    }
}

