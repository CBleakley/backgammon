package backgammon.view;

class Messages {
    static final String WELCOME_MESSAGE = "Welcome to Backgammon";

    static final String COLORED_PLAYER_NAME = "%s%s" + ColorANSICodes.RESET;

    static final String INVALID_INPUT = "Invalid input: ";
    static final String PLEASE_TRY_AGAIN = "Please try again:";

    static final String NAME_PROMPT = "Please enter player%s's name:";
    static final String INVALID_NAME =  INVALID_INPUT + "player names cannot be empty";

    static final String POINTS_TO_PLAY_TO_PROMPT = "How many points would you like to play to in this match?";
    static final String INVALID_POSITIVE = INVALID_INPUT + "must be a positive";
    static final String INVALID_INTEGER = INVALID_INPUT + "must be a positive whole number";

    static final String INITIAL_ROLL_MESSAGE = COLORED_PLAYER_NAME + " rolled a %s";
    static final String ROLL_AGAIN = "Both players rolled the same, roll again...";
    static final String FIRST_TO_PLAY = COLORED_PLAYER_NAME + " rolled the higher number, and therfore plays first";

    static final String PLAYER_INPUT_PROMPT = COLORED_PLAYER_NAME + " please enter a Command (\"quit\" or \"roll\"): ";

    static final String INVALID_COMMAND = "Invalid Input: See game controls\n";

    static final String PLAYER_ROLL = "Roll: %s and %s";
    static final String PLAYER_ROLL_DOUBLES = "Roll: double %s's";
}
