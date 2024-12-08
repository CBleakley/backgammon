/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.playerInput;

/**
 * Represents a command for setting dice values in the Backgammon game.
 * Typically used for testing purposes.
 * Implements the {@code PlayerInput} interface.
 */
public class SetDiceCommand implements PlayerInput {
    private final int dice1;
    private final int dice2;

    /**
     * Constructs a new {@code SetDiceCommand} with specified dice values.
     *
     * @param dice1 the value of the first die
     * @param dice2 the value of the second die
     */
    public SetDiceCommand(int dice1, int dice2) {
        this.dice1 = dice1;
        this.dice2 = dice2;
    }

    /**
     * Returns the value of the first die.
     *
     * @return the first die value
     */
    public int getDice1() { return dice1; }

    /**
     * Returns the value of the second die.
     *
     * @return the second die value
     */
    public int getDice2() { return dice2; }

    /**
     * Parses a string to determine if it represents a set dice command.
     * The string must start with "dice" followed by two digits (e.g., "dice34").
     *
     * @param string the input string to parse
     * @return a new {@code SetDiceCommand} instance with the specified dice values,
     *         or {@code null} if the input is invalid
     */
    public static SetDiceCommand parse (String string) {
        if (string == null || !string.startsWith("dice")) { return null; }

        String diceValues = string.substring(4);

        if (diceValues.length() != 2 || !Character.isDigit(diceValues.charAt(0)) || !Character.isDigit(diceValues.charAt(1))) {
            return null;
        }

        int dice1 = Character.getNumericValue(diceValues.charAt(0));
        int dice2 = Character.getNumericValue(diceValues.charAt(1));

        return new SetDiceCommand(dice1, dice2);
    }
}
