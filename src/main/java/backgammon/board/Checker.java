/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.board;

/**
 * Represents a single checker in a backgammon game.
 * Each checker has a specific color indicating which player it belongs to.
 */
public class Checker {
    private final Color color;

    /**
     * Creates a new Checker with the specified color.
     *
     * @param color the color of the checker
     */
    public Checker(Color color) {
        this.color = color;
    }

    /**
     * Retrieves the color of this checker.
     *
     * @return the color of the checker
     */
    public Color getColor() {
        return color;
    }
}
