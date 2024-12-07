/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.view;

class Messages {
    static final String WELCOME_MESSAGE = "Welcome to Backgammon";

    static final String COLORED_PLAYER_NAME = "%s%s" + ColorANSICodes.RESET;

    static final String MATCH_QUIT = "Match was quit";
    static final String MATCH_WIN = COLORED_PLAYER_NAME + " won the match!";
    static final String GAME_WINNER = COLORED_PLAYER_NAME + " won the game, winning %s point(s)";
    static final String SINGLE_WIN = "Game ended in a Single";
    static final String DOUBLE_REFUSED = "Game ended because the double was refused";
    static final String GAMMON_WIN = "Game ended in a Gammon";
    static final String BACKGAMMON_WIN = "Game ended in a Backgammon";

    static final String ENTER_TO_CONTINUE = "Press ENTER to continue to the next game";

    static final String MATCH_SCORE = COLORED_PLAYER_NAME + " - %s | " + COLORED_PLAYER_NAME + " - %s";
    static final String PLAYING_TO = "Match length: %s";
    static final String BOARD_TITLE = COLORED_PLAYER_NAME + "  pip: %s";

    static final String INVALID_INPUT = "Invalid input: ";
    static final String PLEASE_TRY_AGAIN = "Please try again:";

    static final String NAME_PROMPT = "Please enter player%s's name:";
    static final String INVALID_NAME =  INVALID_INPUT + "player names cannot be empty";

    static final String POINTS_TO_PLAY_TO_PROMPT = "How many points would you like to play to in this match?";
    static final String INVALID_POSITIVE = INVALID_INPUT + "must be a positive";
    static final String INVALID_INTEGER = INVALID_INPUT + "must be a positive whole number";
    static final String OUT_OF_VALID_RANGE = INVALID_INPUT + "must be between 1 and %s";

    static final String DOUBLE_DICE_OWNER = COLORED_PLAYER_NAME + " owns the double dice, you cannot offer it";
    static final String OFFER_DOUBLE = COLORED_PLAYER_NAME + " accept or refuse double";
    static final String INVALID_DECISION = INVALID_INPUT + " must be \"accept\" or \"refuse\"";

    static final String INITIAL_ROLL_MESSAGE = COLORED_PLAYER_NAME + " rolled a %s";
    static final String ROLL_AGAIN = "Both players rolled the same, roll again...";
    static final String FIRST_TO_PLAY = COLORED_PLAYER_NAME + " rolled the higher number, and therefore plays first";

    static final String PLAYER_INPUT_PROMPT = COLORED_PLAYER_NAME + " please enter a command (enter \"hint\" for a list of commands): ";

    static final String INVALID_COMMAND = "Invalid Command: enter \"hint\" to see valid commands\n";

    static final String CHOSE_MOVE_PROMPT = "Chose a move sequence (1 to %s): ";
    static final String ONLY_POSSIBLE_MOVE = "Only one possible move, forced to play: ";
    static final String NO_POSSIBLE_MOVES = COLORED_PLAYER_NAME + " has no available moves";

    static final String POSSIBLE_MOVES_TITLE = "Possible moves: ";
    static final String MOVE_OPTION_TITLE = "Option %s:";

    static final String HINT =
            """
             The following commands are available:
                "quit" - quits the game
                "roll" - rolls the dice
                "pip" - returns the current player's pip count
                "double" - offer a double to the other player, who can accept or reject the proposal
                """;
    static final String HINT_WITH_DOUBLE = HINT + "   \"hint\"\n";
}
