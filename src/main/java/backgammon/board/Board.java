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

    public List<Point> getPoints() {
        return List.copyOf(points);
    }

    private void initialiseStartingPosition() {
        // Black checkers
        this.points.get(0).addChecker(new Checker(Color.BLACK));  // 2 checkers on point 1
        this.points.get(0).addChecker(new Checker(Color.BLACK));

        this.points.get(11).addChecker(new Checker(Color.BLACK));  // 5 checkers on point 12
        for (int i = 0; i < 4; i++) {
            this.points.get(11).addChecker(new Checker(Color.BLACK));
        }

        this.points.get(16).addChecker(new Checker(Color.BLACK));  // 3 checkers on point 17
        for (int i = 0; i < 2; i++) {
            this.points.get(16).addChecker(new Checker(Color.BLACK));
        }

        this.points.get(18).addChecker(new Checker(Color.BLACK));  // 5 checkers on point 19
        for (int i = 0; i < 4; i++) {
            this.points.get(18).addChecker(new Checker(Color.BLACK));
        }

        // White checkers
        this.points.get(23).addChecker(new Checker(Color.WHITE));  // 2 checkers on point 24
        this.points.get(23).addChecker(new Checker(Color.WHITE));

        this.points.get(12).addChecker(new Checker(Color.WHITE));  // 5 checkers on point 13
        for (int i = 0; i < 4; i++) {
            this.points.get(12).addChecker(new Checker(Color.WHITE));
        }

        this.points.get(7).addChecker(new Checker(Color.WHITE));   // 3 checkers on point 8
        for (int i = 0; i < 2; i++) {
            this.points.get(7).addChecker(new Checker(Color.WHITE));
        }

        this.points.get(5).addChecker(new Checker(Color.WHITE));   // 5 checkers on point 6
        for (int i = 0; i < 4; i++) {
            this.points.get(5).addChecker(new Checker(Color.WHITE));
        }
    }
}
