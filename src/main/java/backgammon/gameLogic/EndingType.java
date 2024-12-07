/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.gameLogic;

public enum EndingType {
    SINGLE, GAMMON, BACKGAMMON, DOUBLE_REFUSED;

    public int getMultiplier() {
        return switch (this) {
            case SINGLE -> 1;
            case DOUBLE_REFUSED -> 1;
            case GAMMON -> 2;
            case BACKGAMMON -> 3;
        };
    }
}
