package backgammon.view;

import backgammon.board.Color;
import backgammon.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessagesTest {

    @Test
    void should_HaveCorrectWelcomeMessage() {
        String expected = "Welcome to Backgammon";
        assertEquals(expected, Messages.WELCOME_MESSAGE, "WELCOME_MESSAGE should match");
    }


    @Test
    void should_HaveCorrectMatchQuitMessage() {
        String expected = "Match was quit";
        assertEquals(expected, Messages.MATCH_QUIT, "MATCH_QUIT should match");
    }



    @Test
    void should_HaveCorrectSingleWinMessage() {
        String expected = "Game ended in a Single";
        assertEquals(expected, Messages.SINGLE_WIN, "SINGLE_WIN should match");
    }

    @Test
    void should_HaveCorrectDoubleRefusedMessage() {
        String expected = "Game ended because the double was refused";
        assertEquals(expected, Messages.DOUBLE_REFUSED, "DOUBLE_REFUSED should match");
    }

    @Test
    void should_HaveCorrectGammonWinMessage() {
        String expected = "Game ended in a Gammon";
        assertEquals(expected, Messages.GAMMON_WIN, "GAMMON_WIN should match");
    }

    @Test
    void should_HaveCorrectBackgammonWinMessage() {
        String expected = "Game ended in a Backgammon";
        assertEquals(expected, Messages.BACKGAMMON_WIN, "BACKGAMMON_WIN should match");
    }

    @Test
    void should_HaveCorrectEnterToContinueMessage() {
        String expected = "Press ENTER to continue to the next game";
        assertEquals(expected, Messages.ENTER_TO_CONTINUE, "ENTER_TO_CONTINUE should match");
    }


    @Test
    void should_HaveCorrectPlayingToMessage_Format() {
        String expected = "Match length: %s";
        assertEquals(expected, Messages.PLAYING_TO, "PLAYING_TO format should match");
    }


    @Test
    void should_HaveCorrectInvalidInputMessage() {
        String expected = "Invalid input: ";
        assertEquals(expected, Messages.INVALID_INPUT, "INVALID_INPUT should match");
    }

    @Test
    void should_HaveCorrectPleaseTryAgainMessage() {
        String expected = "Please try again:";
        assertEquals(expected, Messages.PLEASE_TRY_AGAIN, "PLEASE_TRY_AGAIN should match");
    }

    @Test
    void should_HaveCorrectNamePromptMessage() {
        String expected = "Please enter player%s's name:";
        assertEquals(expected, Messages.NAME_PROMPT, "NAME_PROMPT format should match");
    }

    @Test
    void should_HaveCorrectInvalidNameMessage() {
        String expected = "Invalid input: player names cannot be empty";
        assertEquals(expected, Messages.INVALID_NAME, "INVALID_NAME should match");
    }

    @Test
    void should_HaveCorrectPointsToPlayToPrompt() {
        String expected = "How many points would you like to play to in this match?";
        assertEquals(expected, Messages.POINTS_TO_PLAY_TO_PROMPT, "POINTS_TO_PLAY_TO_PROMPT should match");
    }

    @Test
    void should_HaveCorrectInvalidPositiveMessage() {
        String expected = "Invalid input: must be a positive";
        assertEquals(expected, Messages.INVALID_POSITIVE, "INVALID_POSITIVE should match");
    }

    @Test
    void should_HaveCorrectInvalidIntegerMessage() {
        String expected = "Invalid input: must be a positive whole number";
        assertEquals(expected, Messages.INVALID_INTEGER, "INVALID_INTEGER should match");
    }

    @Test
    void should_HaveCorrectOutOfValidRangeMessage_Format() {
        String expected = "Invalid input: must be between 1 and %s";
        assertEquals(expected, Messages.OUT_OF_VALID_RANGE, "OUT_OF_VALID_RANGE format should match");
    }




    @Test
    void should_HaveCorrectRollAgainMessage() {
        String expected = "Both players rolled the same, roll again...";
        assertEquals(expected, Messages.ROLL_AGAIN, "ROLL_AGAIN should match");
    }

    @Test
    void should_HaveCorrectInvalidCommandMessage() {
        String expected = "Invalid Command: enter \"hint\" to see valid commands\n";
        assertEquals(expected, Messages.INVALID_COMMAND, "INVALID_COMMAND should match");
    }

    @Test
    void should_HaveCorrectChoseMovePrompt_Format() {
        String expected = "Chose a move sequence (1 to %s): ";
        assertEquals(expected, Messages.CHOSE_MOVE_PROMPT, "CHOSE_MOVE_PROMPT format should match");
    }

    @Test
    void should_HaveCorrectOnlyPossibleMoveMessage() {
        String expected = "Only one possible move, forced to play: ";
        assertEquals(expected, Messages.ONLY_POSSIBLE_MOVE, "ONLY_POSSIBLE_MOVE should match");
    }

    @Test
    void should_HaveCorrectPossibleMovesTitle() {
        String expected = "Possible moves: ";
        assertEquals(expected, Messages.POSSIBLE_MOVES_TITLE, "POSSIBLE_MOVES_TITLE should match");
    }

    @Test
    void should_HaveCorrectMoveOptionTitle_Format() {
        String expected = "Option %s:";
        assertEquals(expected, Messages.MOVE_OPTION_TITLE, "MOVE_OPTION_TITLE format should match");
    }

    @Test
    void should_HaveCorrectHintMessage() {
        String expected = """
                 The following commands are available:
                    "quit"
                    "roll"
                    "pip"
                    """;
        assertEquals(expected, Messages.HINT, "HINT should match");
    }

}
