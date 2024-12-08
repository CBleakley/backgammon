/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.view;

/**
 * Utility class that contains formatting strings and constants for displaying the backgammon board.
 * This class provides predefined ANSI codes, symbols, and templates for rendering the board, checkers,
 * and dice rolls in a user-friendly way.
 */
public class BoardFormatting {
    public static final String DOUBLE_DICE = "  [%s]";
    public static final String NO_PIP_BOARDER = "|--+--+---+---+---+---+-|BAR|--+--+---+---+---+---+-|  OFF: %s";
    public static final String PIP_13_24 = "|-13--+---+---+---+--18-|BAR|-19--+---+---+---+--24-|  OFF: %s";
    public static final String PIP_12_1 = "|-12--+---+---+---+--07-|BAR|-06--+---+---+---+--01-|  OFF: %s";
    public static final String EMPTY_BOARD_LINE = "|                       |   |                       |";
    public static final String BOARD_WITH_2_DICE = "|          %s-%s          |   |                       |";
    public static final String BOARD_WITH_4_DICE = "|        %s-%s-%s-%s        |   |                       |";

    public static final String CHECKER_SYMBOL = "O";
    public static final String RED_CHECKER = ColorANSICodes.RED + CHECKER_SYMBOL + ColorANSICodes.RESET;
    public static final String BLUE_CHECKER = ColorANSICodes.BLUE + CHECKER_SYMBOL + ColorANSICodes.RESET;
    public static final String RED_NUMBER = ColorANSICodes.RED + "%s" + ColorANSICodes.RESET;
    public static final String BLUE_NUMBER = ColorANSICodes.BLUE + "%s" + ColorANSICodes.RESET;
}
