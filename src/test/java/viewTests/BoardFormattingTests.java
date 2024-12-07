package backgammon.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardFormattingTest {

    @Test
    void should_HaveCorrectDoubleDiceFormat() {
        String expected = "  [%s]";
        assertEquals(expected, BoardFormatting.DOUBLE_DICE, "DOUBLE_DICE format should match");
    }

    @Test
    void should_HaveCorrectNoPipBoarderFormat() {
        String expected = "|--+--+---+---+---+---+-|BAR|--+--+---+---+---+---+-|  OFF: %s";
        assertEquals(expected, BoardFormatting.NO_PIP_BOARDER, "NO_PIP_BOARDER format should match");
    }

    @Test
    void should_HaveCorrectPip13_24Format() {
        String expected = "|-13--+---+---+---+--18-|BAR|-19--+---+---+---+--24-|  OFF: %s";
        assertEquals(expected, BoardFormatting.PIP_13_24, "PIP_13_24 format should match");
    }

    @Test
    void should_HaveCorrectPip12_1Format() {
        String expected = "|-12--+---+---+---+--07-|BAR|-06--+---+---+---+--01-|  OFF: %s";
        assertEquals(expected, BoardFormatting.PIP_12_1, "PIP_12_1 format should match");
    }

    @Test
    void should_HaveCorrectEmptyBoardLineFormat() {
        String expected = "|                       |   |                       |";
        assertEquals(expected, BoardFormatting.EMPTY_BOARD_LINE, "EMPTY_BOARD_LINE format should match");
    }

    @Test
    void should_HaveCorrectBoardWith2DiceFormat() {
        String expected = "|          %s-%s          |   |                       |";
        assertEquals(expected, BoardFormatting.BOARD_WITH_2_DICE, "BOARD_WITH_2_DICE format should match");
    }

    @Test
    void should_HaveCorrectBoardWith4DiceFormat() {
        String expected = "|        %s-%s-%s-%s        |   |                       |";
        assertEquals(expected, BoardFormatting.BOARD_WITH_4_DICE, "BOARD_WITH_4_DICE format should match");
    }

    @Test
    void should_HaveCorrectRedCheckerSymbol() {
        String expected = "\033[31mO\033[0m";
        assertEquals(expected, BoardFormatting.RED_CHECKER, "RED_CHECKER should have correct ANSI codes");
    }

    @Test
    void should_HaveCorrectBlueCheckerSymbol() {
        String expected = "\033[34mO\033[0m";
        assertEquals(expected, BoardFormatting.BLUE_CHECKER, "BLUE_CHECKER should have correct ANSI codes");
    }

    @Test
    void should_HaveCorrectRedNumberFormat() {
        String expected = "\033[31m%s\033[0m";
        assertEquals(expected, BoardFormatting.RED_NUMBER, "RED_NUMBER format should match");
    }

    @Test
    void should_HaveCorrectBlueNumberFormat() {
        String expected = "\033[34m%s\033[0m";
        assertEquals(expected, BoardFormatting.BLUE_NUMBER, "BLUE_NUMBER format should match");
    }
}
