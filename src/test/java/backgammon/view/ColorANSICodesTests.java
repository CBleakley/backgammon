package backgammon.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorANSICodesTests {
    @Test
    void MessagesTest() {
        String msg = Messages.WELCOME_MESSAGE;

        assertEquals("Welcome to Backgammon", msg);
    }
}
