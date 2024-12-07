/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.playerInput;

public class SetDiceCommand implements PlayerInput {
    private final int dice1;
    private final int dice2;

    public SetDiceCommand(int dice1, int dice2) {
        this.dice1 = dice1;
        this.dice2 = dice2;
    }

    public int getDice1() { return dice1; }

    public int getDice2() { return dice2; }

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
