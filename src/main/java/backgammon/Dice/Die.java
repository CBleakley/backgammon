package backgammon.Dice;

public class Die {
    private Integer faceValue;

    public Die() {
        this.faceValue = null;
    }

    public int getFaceValue() {
        return faceValue;
    }

    public int roll() {
        faceValue = (int)(Math.random() * 6) + 1;
        return faceValue;
    }
}
