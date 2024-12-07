package gameLogicTests;

import backgammon.gameLogic.EndingType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EndingTypeTests {
    @Test
    void testSingleMultiplier() {
        assertEquals(1, EndingType.SINGLE.getMultiplier(), "SINGLE multiplier should be 1");
    }

    @Test
    void testDoubleRefusedMultiplier() {
        assertEquals(1, EndingType.DOUBLE_REFUSED.getMultiplier(), "DOUBLE_REFUSED multiplier should be 1");
    }

    @Test
    void testGammonMultiplier() {
        assertEquals(2, EndingType.GAMMON.getMultiplier(), "GAMMON multiplier should be 2");
    }

    @Test
    void testBackgammonMultiplier() {
        assertEquals(3, EndingType.BACKGAMMON.getMultiplier(), "BACKGAMMON multiplier should be 3");
    }
}
