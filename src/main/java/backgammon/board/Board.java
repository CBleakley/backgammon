package backgammon.board;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final List<Point> points;
    private final Bar bar;
    private final Off off;

    static final int NUMBER_OF_POINTS = 24;

    public Board() {
        this.points = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_POINTS; i++) {
            this.points.add(new Point());
        }

        initialiseStartingPosition();

        this.bar = new Bar();
        this.off = new Off();
    }

    public Board(List<Point> points, Bar bar, Off off) {
        this.points = points;
        this.bar = bar;
        this.off = off;
    }

    public List<Point> getPoints() {
        return List.copyOf(points);
    }

    public Bar getBar() {
        return this.bar;
    }

    public Off getOff() {
        return this.off;
    }

    public Board cloneBoard() {
        List<Point> clonedPoints = new ArrayList<>();
        for (Point point : this.points) {
            clonedPoints.add(point.clonePoint());
        }
        Bar clonedBar = this.bar.cloneBar();
        Off clonedOff = this.off.cloneOff();

        return new Board(clonedPoints, clonedBar, clonedOff);
    }

    private void initialiseStartingPosition() {
        // Black checkers
        this.points.get(0).addChecker(new Checker(Color.RED));  // 2 checkers on point 1
        this.points.get(0).addChecker(new Checker(Color.RED));

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

        // White checkers
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
