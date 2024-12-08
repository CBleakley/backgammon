/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.input;

/**
 * Represents a source of input for the Backgammon game.
 * Classes implementing this interface provide a way to retrieve input, such as from the console or a file.
 */
public interface InputSource {

    /**
     * Retrieves input from the implemented input source.
     *
     * @return the input as a {@code String}
     */
    String getInput(); // Method to get input
}