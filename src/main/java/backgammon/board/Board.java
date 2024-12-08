/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.board;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the board in a backgammon game, consisting of points, a bar, and an off area.
 */
public class Board {
    /**
     * The number of checkers each player has in a game.
     */
    static public int NUMBER_OF_CHECKERS_PER_PLAYER = 15;

    /**
     * The total number of points on the board.
     */
    static final int NUMBER_OF_POINTS = 24;

    /**
     * -3 used to denote the bar in indexing
     */
    static public int BAR_FLAG = -3;

    /**
    * -1 used to denote the Blue off in indexing
    */
    static public int BLUE_OFF_FLAG = -1;

    /**
     * -2 used to denote the Red off in indexing
     */
    static public int RED_OFF_FLAG = -2;

    private final List<Point> points;
    private final Bar bar;
    private final Off off;

    /**
     * Initializes a new Board with empty points, a bar, and an off area.
     */
    public Board() {
        this.points = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_POINTS; i++) {
            this.points.add(new Point());
        }

        this.bar = new Bar();
        this.off = new Off();
    }

    /**
     * Initializes a new Board with the specified points, bar, and off area.
     *
     * @param points the list of points on the board
     * @param bar    the bar containing checkers
     * @param off    the off area where checkers are moved after being borne off
     */
    public Board(List<Point> points, Bar bar, Off off) {
        this.points = points;
        this.bar = bar;
        this.off = off;
    }

    /**
     * Retrieves an unmodifiable list of points on the board.
     *
     * @return the list of points
     */
    public List<Point> getPoints() {
        return List.copyOf(points);
    }

    /**
     * Retrieves the bar containing checkers that have been hit.
     *
     * @return the bar
     */
    public Bar getBar() {
        return this.bar;
    }

    /**
     * Retrieves the off area where checkers are placed after being borne off.
     *
     * @return the off area
     */
    public Off getOff() {
        return this.off;
    }

    /**
     * Creates a deep copy of the current Board instance.
     *
     * @return a new Board instance that is a deep copy of the current board
     */
    public Board cloneBoard() {
        List<Point> clonedPoints = new ArrayList<>();
        for (Point point : this.points) {
            clonedPoints.add(point.clonePoint());
        }
        Bar clonedBar = this.bar.cloneBar();
        Off clonedOff = this.off.cloneOff();

        return new Board(clonedPoints, clonedBar, clonedOff);
    }

    /**
     * Initializes the board to the starting position for a backgammon game,
     * placing checkers on their respective points according to the standard rules.
     */
    public void initialiseStartingPosition() {
        // RED checkers
        this.points.getFirst().addChecker(new Checker(Color.RED));  // 2 checkers on point 1
        this.points.getFirst().addChecker(new Checker(Color.RED));

        this.points.get(11).addChecker(new Checker(Color.RED));  // 5 checkers on point 12
        for (int i = 0; i < 4; i++) {
            this.points.get(11).addChecker(new Checker(Color.RED));
        }

        this.points.get(16).addChecker(new Checker(Color.RED));  // 3 checkers on point 17
        for (int i = 0; i < 2; i++) {
            this.points.get(16).addChecker(new Checker(Color.RED));
        }

        this.points.get(18).addChecker(new Checker(Color.RED));  // 5 checkers on point 19
        for (int i = 0; i < 4; i++) {
            this.points.get(18).addChecker(new Checker(Color.RED));
        }

        // Blue checkers
        this.points.get(23).addChecker(new Checker(Color.BLUE));  // 2 checkers on point 24
        this.points.get(23).addChecker(new Checker(Color.BLUE));

        this.points.get(12).addChecker(new Checker(Color.BLUE));  // 5 checkers on point 13
        for (int i = 0; i < 4; i++) {
            this.points.get(12).addChecker(new Checker(Color.BLUE));
        }

        this.points.get(7).addChecker(new Checker(Color.BLUE));   // 3 checkers on point 8
        for (int i = 0; i < 2; i++) {
            this.points.get(7).addChecker(new Checker(Color.BLUE));
        }

        this.points.get(5).addChecker(new Checker(Color.BLUE));   // 5 checkers on point 6
        for (int i = 0; i < 4; i++) {
            this.points.get(5).addChecker(new Checker(Color.BLUE));
        }
    }
}
