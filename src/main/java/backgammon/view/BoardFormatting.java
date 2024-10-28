package backgammon.view;

class BoardFormatting {
    static final String TOP = "|-13--+---+---+---+--18-|BAR|-19--+---+---+---+--24-|  OFF: %s\n";
    static final String BOTTOM = "|-12--+---+---+---+--07-|BAR|-06--+---+---+---+--01-|  OFF: %s\n";

    static final String TOP_BORDER = "|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|‾‾‾|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|\n";
    static final String BOTTOM_BORDER = "|_______________________|___|_______________________|";

    static final String EMPTY_BOARD_LINE = "|                       |   |                       |\n";

    static final String CHECKER_SYMBOL = "O";
    static final String RED_CHECKER = ColorANSICodes.RED + CHECKER_SYMBOL + ColorANSICodes.RESET;
    static final String BLUE_CHECKER = ColorANSICodes.BLUE + CHECKER_SYMBOL + ColorANSICodes.RESET;
}
