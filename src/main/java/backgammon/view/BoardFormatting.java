package backgammon.view;

class BoardFormatting {
    static final String DOUBLE_DICE = "  [%s]";
    static final String NO_PIP_BOARDER = "|--+--+---+---+---+---+-|BAR|--+--+---+---+---+---+-|  OFF: %s";
    static final String PIP_13_24 = "|-13--+---+---+---+--18-|BAR|-19--+---+---+---+--24-|  OFF: %s";
    static final String PIP_12_1 = "|-12--+---+---+---+--07-|BAR|-06--+---+---+---+--01-|  OFF: %s";
    static final String EMPTY_BOARD_LINE = "|                       |   |                       |";
    static final String BOARD_WITH_2_DICE = "|          %s-%s          |   |                       |";
    static final String BOARD_WITH_4_DICE = "|        %s-%s-%s-%s        |   |                       |";

    static final String CHECKER_SYMBOL = "O";
    static final String RED_CHECKER = ColorANSICodes.RED + CHECKER_SYMBOL + ColorANSICodes.RESET;
    static final String BLUE_CHECKER = ColorANSICodes.BLUE + CHECKER_SYMBOL + ColorANSICodes.RESET;
    static final String RED_NUMBER = ColorANSICodes.RED + "%s" + ColorANSICodes.RESET;
    static final String BLUE_NUMBER = ColorANSICodes.BLUE + "%s" + ColorANSICodes.RESET;
}
