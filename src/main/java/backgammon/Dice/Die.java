/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.Dice;

public class Die {
    private Integer faceValue;

    public Die() {
        this.faceValue = null;
    }

    public Integer getFaceValue() {
        if (faceValue == null) return null;
        return faceValue;
    }

    public int roll() {
        faceValue = (int)(Math.random() * 6) + 1;
        return faceValue;
    }
}
