package gameLogicTests;

import backgammon.board.Color;
import backgammon.gameLogic.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTests {
    @Test
    void testGetFromPoint() {
        Move move = new Move(3, 5, Color.BLUE, 2);
        assertEquals(3, move.getFromPoint(), "getFromPoint should return the correct starting point");
    }

    @Test
    void testGetToPoint() {
        Move move = new Move(3, 5, Color.BLUE, 2);
        assertEquals(5, move.getToPoint(), "getToPoint should return the correct destination point");
    }

    @Test
    void testGetPlayerColor() {
        Move move = new Move(3, 5, Color.RED, 2);
        assertEquals(Color.RED, move.getPlayerColor(), "getPlayerColor should return the correct player color");
    }

    @Test
    void testToStringFromBarToOff() {
        Move move = new Move(-3, -1, Color.BLUE, 2);
        assertEquals("Bar-Off", move.toString(), "toString should return 'Move: Bar-Off' for a move from Bar to Off");
    }

    @Test
    void testToStringRegularMoveBlue() {
        Move move = new Move(2, 5, Color.BLUE, 2);
        assertEquals("3-6", move.toString(), "toString should return 'Move: 3-6' for a BLUE player move");
    }

    @Test
    void testToStringRegularMoveRed() {
        Move move = new Move(2, 5, Color.RED, 2);
        assertEquals("22-19", move.toString(), "toString should return 'Move: 22-19' for a RED player move");
    }

    @Test
    void testToStringToOffBlue() {
        Move move = new Move(22, -1, Color.BLUE, 2);
        assertEquals("23-Off", move.toString(), "toString should return 'Move: 23-Off' for a BLUE player moving to Off");
    }

    @Test
    void testEqualsSameObject() {
        Move move = new Move(2, 5, Color.BLUE, 2);
        assertEquals(move, move, "A Move object should be equal to itself");
    }

    @Test
    void testEqualsDifferentObjectSameValues() {
        Move move1 = new Move(2, 5, Color.BLUE, 2);
        Move move2 = new Move(2, 5, Color.BLUE, 2);
        assertEquals(move1, move2, "Two Move objects with the same values should be equal");
    }

    @Test
    void testNotEqualsDifferentFromPoint() {
        Move move1 = new Move(2, 5, Color.BLUE, 2);
        Move move2 = new Move(3, 5, Color.BLUE, 2);
        assertNotEquals(move1, move2, "Move objects with different fromPoint values should not be equal");
    }

    @Test
    void testNotEqualsDifferentToPoint() {
        Move move1 = new Move(2, 5, Color.BLUE, 2);
        Move move2 = new Move(2, 6, Color.BLUE, 2);
        assertNotEquals(move1, move2, "Move objects with different toPoint values should not be equal");
    }

    @Test
    void testNotEqualsDifferentPlayerColor() {
        Move move1 = new Move(2, 5, Color.BLUE, 2);
        Move move2 = new Move(2, 5, Color.RED, 2);
        assertNotEquals(move1, move2, "Move objects with different playerColor values should not be equal");
    }

    @Test
    void testHashCodeSameValues() {
        Move move1 = new Move(2, 5, Color.BLUE, 2);
        Move move2 = new Move(2, 5, Color.BLUE, 2);
        assertEquals(move1.hashCode(), move2.hashCode(), "Move objects with the same values should have the same hash code");
    }

    @Test
    void testHashCodeDifferentValues() {
        Move move1 = new Move(2, 5, Color.BLUE, 2);
        Move move2 = new Move(3, 5, Color.BLUE, 2);
        assertNotEquals(move1.hashCode(), move2.hashCode(), "Move objects with different values should have different hash codes");
    }
}
