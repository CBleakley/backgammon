package backgammon.gameLogic;

import backgammon.board.Bar;
import backgammon.board.Board;
import backgammon.board.Color;
import backgammon.board.Checker;
import backgammon.board.Off;
import backgammon.board.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MoveGeneratorTests {

    /**
     * Helper method to initialize an empty board.
     */
    private void initializeEmptyBoard(Board board) {
        for (Point point : board.getPoints()) {
            while (point.hasCheckers()) {
                point.removeTopChecker(); // Remove all checkers from the point
            }
        }
        board.getBar().getBarOfColor(Color.RED).clear();
        board.getBar().getBarOfColor(Color.BLUE).clear();
        board.getOff().getOffOfColor(Color.RED).clear();
        board.getOff().getOffOfColor(Color.BLUE).clear();
    }


    /**
     * Helper method to add a checker to a specific point.
     */
    private void addChecker(Board board, int pointIndex, Color color) {
        board.getPoints().get(pointIndex).addChecker(new Checker(color));
    }

    /**
     * Test Scenario: Player has no checkers on the board and cannot make any moves.
     */
    /*@Test
    void should_ReturnEmptyList_When_PlayerHasNoCheckers() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        List<Integer> dice = Arrays.asList(3, 5);
        Color playerColor = Color.RED;

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        assertTrue(possibleMoves.isEmpty(), "Expected no possible moves when player has no checkers.");
    }*/

    /**
     * Test Scenario: Player has checkers on the bar and must enter them onto the board.
     */
    @Test
    void should_ReturnMovesToEnterFromBar_When_PlayerHasCheckersOnBar() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        board.getBar().addChecker(new Checker(Color.RED));
        board.getBar().addChecker(new Checker(Color.RED)); // Player has 2 checkers on the bar
        List<Integer> dice = Arrays.asList(1, 4);
        Color playerColor = Color.RED;

        // PlayerColor RED enters from the bar to points 0 (die=1) and 3 (die=4)
        List<Move> expectedMoves = Arrays.asList(
                new Move(-3, 0, Color.RED), // -3 indicates moving from the bar
                new Move(-3, 3, Color.RED)
        );

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        List<Move> actualMoves = possibleMoves.stream()
                .flatMap(List::stream)
                .toList();

        assertEquals(2, actualMoves.size(), "Expected two possible moves to enter from the bar.");
        assertTrue(actualMoves.containsAll(expectedMoves), "Moves to enter from bar do not match expected.");
    }

    /**
     * Test Scenario: Player has a single checker that can move using one of the dice.
     */
    /*@Test
    void should_ReturnSingleMoveSequence_When_OnlyOneMovePossible() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 0, Color.BLUE); // Player has one checker on point 0
        List<Integer> dice = Arrays.asList(2, 5);
        Color playerColor = Color.BLUE;

        // Expected Moves:
        // Using die=2: Move from point 0 to point 2
        // Using die=5: Move from point 0 to point 5
        List<List<Move>> expected = Arrays.asList(
                Collections.singletonList(new Move(0, 2, Color.BLUE)),
                Collections.singletonList(new Move(0, 5, Color.BLUE))
        );

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        assertEquals(2, possibleMoves.size(), "Expected two possible move sequences.");
        assertTrue(possibleMoves.containsAll(expected), "Move sequences do not match expected.");
    }*/

    /**
     * Test Scenario: Player has multiple checkers and can make multiple move sequences.
     */
    /*@Test
    void should_ReturnMultipleMoveSequences_When_MultipleMovesPossible() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 0, Color.RED);
        addChecker(board, 5, Color.RED);
        List<Integer> dice = Arrays.asList(3, 4);
        Color playerColor = Color.RED;

        // Expected Moves:
        // Using die=3: Move from point 0 to point 3, then die=4: Move from point 3 to point 7
        // Using die=4: Move from point 0 to point 4, then die=3: Move from point 5 to point 8
        List<Move> sequence1 = Arrays.asList(
                new Move(0, 3, Color.RED),
                new Move(3, 7, Color.RED)
        );
        List<Move> sequence2 = Arrays.asList(
                new Move(0, 4, Color.RED),
                new Move(5, 8, Color.RED)
        );

        List<List<Move>> expected = Arrays.asList(sequence1, sequence2);

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        assertEquals(2, possibleMoves.size(), "Expected two possible move sequences.");
        assertTrue(possibleMoves.containsAll(expected), "Move sequences do not match expected.");
    }*/

    /**
     * Test Scenario: Player can bear off with exact and non-exact dice values.
     */
    /*@Test
    void should_ReturnBearOffMoves_When_PlayerCanBearOff() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 19, Color.BLUE); // Point 19 (index 18)
        addChecker(board, 23, Color.BLUE); // Point 23 (index 22)
        List<Integer> dice = Arrays.asList(4, 5);
        Color playerColor = Color.BLUE;

        // Expected Moves:
        // Using die=4: Move from point 19 (index 18) to point 23 (index 22)
        // Using die=5: Bear off from point 19 (index 18) using die=5
        // Using die=5: Bear off from point 23 (index 22) using die=5
        List<Move> expectedMoves = Arrays.asList(
                new Move(18, 22, Color.BLUE),
                new Move(18, -1, Color.BLUE),
                new Move(22, -1, Color.BLUE)
        );

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        List<Move> actualMoves = possibleMoves.stream()
                .flatMap(List::stream)
                .toList();

        assertEquals(3, actualMoves.size(), "Expected three possible bear-off moves.");
        assertTrue(actualMoves.containsAll(expectedMoves), "Bear-off moves do not match expected.");
    }*/

    /**
     * Test Scenario: Player has checkers on the bar but cannot enter due to blocked entry points.
     */
    @Test
    void should_ReturnNoMoves_When_PlayerHasCheckersOnBarButCannotEnter() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        board.getBar().addChecker(new Checker(Color.RED)); // Player has 1 checker on the bar
        addChecker(board, 0, Color.BLUE);
        addChecker(board, 0, Color.BLUE); // Blocking entry point for RED: point 0 has two BLUE checkers
        addChecker(board, 1, Color.BLUE);
        addChecker(board, 1, Color.BLUE);
        List<Integer> dice = Arrays.asList(1, 2);
        Color playerColor = Color.RED;

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        assertTrue(possibleMoves.get(0).isEmpty(), "Expected no possible moves when entry points are blocked.");
    }

    /**
     * Test Scenario: Player must use the highest possible dice when not all dice can be used.
     */
    /*@Test
    void should_EnforceForcedMoveRules_When_NotAllDiceCanBeUsed() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 0, Color.RED);
        // Player RED cannot make a move with die=3 but can with die=2
        List<Integer> dice = Arrays.asList(3, 2);
        Color playerColor = Color.RED;

        // Expected: Must use die=2 since die=3 is not possible
        List<Move> expectedMove = Collections.singletonList(new Move(0, 2, Color.RED));

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        assertEquals(1, possibleMoves.size(), "Expected one move sequence enforcing forced move rules.");
        assertEquals(expectedMove, possibleMoves.get(0), "Move does not match expected.");
    }*/

    /**
     * Test Scenario: Player rolls doubles and can make four moves.
     */
    @Test
    void should_HandleDoubles_Correctly() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 12, Color.BLUE);
        List<Integer> dice = Arrays.asList(1, 1, 1, 1); // Doubles
        Color playerColor = Color.BLUE;

        // Expected Moves:
        // Four moves: 0->3, 3->6, 6->9, 9->12
        List<Move> expectedMoves = Arrays.asList(
                new Move(12, 11, Color.BLUE),
                new Move(11, 10, Color.BLUE),
                new Move(10, 9, Color.BLUE),
                new Move(9, 8, Color.BLUE)
        );

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        assertEquals(1, possibleMoves.size(), "Expected one move sequence with doubles.");
        assertEquals(expectedMoves, possibleMoves.get(0), "Move sequence does not match expected for doubles.");
    }

    /**
     * Test Scenario: Player cannot bear off because not all checkers are in the home board.
     */
    @Test
    void should_NotAllowBearingOff_When_NotAllCheckersInHomeBoard() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 3, Color.BLUE);
        addChecker(board, 20, Color.BLUE); // Outside home board
        List<Integer> dice = Arrays.asList(5, 4);
        Color playerColor = Color.BLUE;


        // Expected Moves:
        List<Move> sequence1 = Arrays.asList(
                new Move(20, 15, Color.BLUE),
                new Move(15, 11, Color.BLUE)
        );


        // Act
        List<List<Move>> expected = Arrays.asList(sequence1);
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);


        // Assert
        assertEquals(1, possibleMoves.size(), "Expected one move sequence not bearing off.");
        assertTrue(possibleMoves.containsAll(expected), "Move sequences do not match expected when not all checkers are in home board.");
    }

    /**
     * Test Scenario: Player has all checkers in the home board and can bear off.
     */
    /*@Test
    void should_AllowBearingOff_When_AllCheckersInHomeBoard() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 0, Color.RED); // Home board points for RED: 18-23
        addChecker(board, 1, Color.RED);
        addChecker(board, 2, Color.RED);
        List<Integer> dice = Arrays.asList(1, 2);
        Color playerColor = Color.RED;

        // Expected Moves:
        // Using die=1: Bear off from point 23 (index 22)
        // Using die=2: Bear off from point 22 (index 21)
        List<Move> expectedMoves = Arrays.asList(
                new Move(22, -1, Color.RED),
                new Move(21, -1, Color.RED)
        );

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);
        System.out.println("Start");
        System.out.println(possibleMoves);
        System.out.println("End");
        System.out.println(expectedMoves);
        // Assert
        List<Move> actualMoves = possibleMoves.stream()
                .flatMap(List::stream)
                .toList();


        assertEquals(2, actualMoves.size(), "Expected two bear-off moves.");
        assertTrue(actualMoves.containsAll(expectedMoves), "Bear-off moves do not match expected.");
    }*/

    /**
     * Test Scenario: Player cannot make any moves because all moves are blocked by opponent's checkers.
     */
    /*@Test
    void should_ReturnEmptyList_When_AllMovesAreBlocked_ByOpponent() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 0, Color.RED);
        addChecker(board, 2, Color.RED);
        // Opponent BLUE has two checkers on points 1 and 3 (blocking all possible moves)
        addChecker(board, 1, Color.BLUE);
        addChecker(board, 1, Color.BLUE);
        addChecker(board, 3, Color.BLUE);
        addChecker(board, 3, Color.BLUE);
        List<Integer> dice = Arrays.asList(1, 2);
        Color playerColor = Color.RED;

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        assertTrue(possibleMoves.isEmpty(), "Expected no possible moves when all moves are blocked by opponent.");
    }*/

    /**
     * Test Scenario: Player has multiple checkers and must generate the longest possible move sequences.
     */
    /*@Test
    void should_ReturnLongestMoveSequences_When_MultipleOptionsExist() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 0, Color.RED);
        addChecker(board, 2, Color.RED);
        addChecker(board, 4, Color.RED);
        List<Integer> dice = Arrays.asList(1, 2, 3);
        Color playerColor = Color.RED;

        // Expected: All possible longest move sequences using as many dice as possible
        List<Move> sequence1 = Arrays.asList(
                new Move(0, 1, Color.RED),
                new Move(1, 3, Color.RED),
                new Move(3, 6, Color.RED)
        );
        List<Move> sequence2 = Arrays.asList(
                new Move(0, 1, Color.RED),
                new Move(1, 4, Color.RED),
                new Move(2, 4, Color.RED)
        );
        List<Move> sequence3 = Arrays.asList(
                new Move(2, 4, Color.RED),
                new Move(4, 7, Color.RED),
                new Move(0, 1, Color.RED)
        );

        List<List<Move>> expected = Arrays.asList(sequence1, sequence2, sequence3);

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        for (List<Move> expectedSequence : expected) {
            assertTrue(possibleMoves.contains(expectedSequence),
                    "Expected move sequence not found: " + expectedSequence);
        }
    }*/

    /**
     * Test Scenario: Player attempts to bear off a checker from a higher point using a lower die value.
     */
    /*@Test
    void should_NotAllowBearingOff_When_DieValueLowerThan_PipCount() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 4, Color.BLUE); // Point 4 (pip count = 5 for BLUE)
        List<Integer> dice = Arrays.asList(2, 3);
        Color playerColor = Color.BLUE;

        // Expected Moves:
        // Using die=2: Move from point 4 to point 6
        // Using die=3: Move from point 4 to point 7
        List<Move> expectedMoves = Arrays.asList(
                new Move(4, 6, Color.BLUE),
                new Move(4, 7, Color.BLUE)
        );

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        List<Move> actualMoves = possibleMoves.stream()
                .flatMap(List::stream)
                .toList();

        assertEquals(2, actualMoves.size(), "Expected two possible moves without bearing off.");
        assertTrue(actualMoves.containsAll(expectedMoves), "Moves do not match expected without bearing off.");
    }*/

    /**
     * Test Scenario: Player can bear off multiple checkers in one sequence.
     */
    /*@Test
    void should_BearOffMultipleCheckers_InSingleSequence() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 0, Color.RED); // Home board points for RED: 18-23 (point 23 is index 22)
        addChecker(board, 1, Color.RED);
        List<Integer> dice = Arrays.asList(1, 2);
        Color playerColor = Color.RED;

        // Expected Moves:
        // Using die=1: Bear off from point 23 (index 22)
        // Using die=2: Bear off from point 22 (index 21)
        List<Move> expectedMoves = Arrays.asList(
                new Move(22, -1, Color.RED),
                new Move(21, -1, Color.RED)
        );

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        assertEquals(1, possibleMoves.size(), "Expected one move sequence with multiple bear-offs.");
        assertEquals(expectedMoves, possibleMoves.get(0), "Move sequence does not match expected.");
    }*/

    /**
     * Test Scenario: Player has checkers on multiple points and must generate all valid sequences.
     */
    /*@Test
    void should_ReturnAllValidMoveSequences_When_CheckersOnMultiplePoints() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 0, Color.BLUE);
        addChecker(board, 2, Color.BLUE);
        addChecker(board, 4, Color.BLUE);
        List<Integer> dice = Arrays.asList(2, 3);
        Color playerColor = Color.BLUE;

        // Expected Move Sequences:
        // Sequence 1:
        // - Move from 0 to 2 (die=2)
        // - Move from 2 to 5 (die=3)
        // Sequence 2:
        // - Move from 0 to 3 (die=3)
        // - Move from 2 to 5 (die=2)
        // Sequence 3:
        // - Move from 2 to 4 (die=2)
        // - Move from 4 to 7 (die=3)
        // Sequence 4:
        // - Move from 2 to 5 (die=3)
        // - Move from 0 to 2 (die=2)
        // Sequence 5:
        // - Move from 4 to 6 (die=2)
        // - Move from 0 to 3 (die=3)

        List<Move> sequence1 = Arrays.asList(
                new Move(0, 2, Color.BLUE),
                new Move(2, 5, Color.BLUE)
        );
        List<Move> sequence2 = Arrays.asList(
                new Move(0, 3, Color.BLUE),
                new Move(2, 5, Color.BLUE)
        );
        List<Move> sequence3 = Arrays.asList(
                new Move(2, 4, Color.BLUE),
                new Move(4, 7, Color.BLUE)
        );
        List<Move> sequence4 = Arrays.asList(
                new Move(2, 5, Color.BLUE),
                new Move(0, 2, Color.BLUE)
        );
        List<Move> sequence5 = Arrays.asList(
                new Move(4, 6, Color.BLUE),
                new Move(0, 3, Color.BLUE)
        );

        List<List<Move>> expected = Arrays.asList(sequence1, sequence2, sequence3, sequence4, sequence5);

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        for (List<Move> expectedSequence : expected) {
            assertTrue(possibleMoves.contains(expectedSequence),
                    "Expected move sequence not found: " + expectedSequence);
        }
    }

    /**
     * Test Scenario: Player has checkers that can hit opponent's single checkers.
     */
    /*@Test
    void should_ReturnHitMoves_When_LandingOnOpponentSingleChecker() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 0, Color.RED); // Player RED has a checker on point 0
        addChecker(board, 2, Color.BLUE); // Opponent BLUE has one checker on point 2
        List<Integer> dice = Arrays.asList(2, 3);
        Color playerColor = Color.RED;

        // Expected Moves:
        // - Using die=2: Move from 0 to 2 (hit)
        // - Using die=3: Move from 0 to 3
        List<Move> expectedMoves = Arrays.asList(
                new Move(0, 2, Color.RED),
                new Move(0, 3, Color.RED)
        );

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        List<Move> actualMoves = possibleMoves.stream()
                .flatMap(List::stream)
                .toList();

        assertEquals(2, actualMoves.size(), "Expected two possible hit and standard moves.");
        assertTrue(actualMoves.containsAll(expectedMoves), "Hit and standard moves do not match expected.");
    }*/

    /**
     * Test Scenario: Player cannot bear off because some checkers are still on the bar.
     */
    /*@Test
    void should_NotAllowBearingOff_When_CheckersAreOnBar() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        board.getBar().addChecker(new Checker(Color.BLUE)); // Player has 1 checker on the bar
        addChecker(board, 5, Color.BLUE); // Player BLUE has a checker on point 5 (home board)
        List<Integer> dice = Arrays.asList(1, 2);
        Color playerColor = Color.BLUE;

        // Expected Moves:
        // - Must enter the checker from the bar first
        // - Using die=1: Enter from bar to point 0
        // - Using die=2: Enter from bar to point 1
        List<List<Move>> expected = Arrays.asList(
                Collections.singletonList(new Move(-3, 0, Color.BLUE)),
                Collections.singletonList(new Move(-3, 1, Color.BLUE))
        );

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        assertEquals(2, possibleMoves.size(), "Expected two move sequences to enter from bar.");
        assertTrue(possibleMoves.containsAll(expected), "Move sequences to enter from bar do not match expected.");
    }*/

    /**
     * Test Scenario: Player attempts to bear off using all available dice.
     */
    /*@Test
    void should_BearOffUsingAllAvailableDice() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 22, Color.RED); // Point 23 (index 22)
        addChecker(board, 21, Color.RED); // Point 22 (index 21)
        List<Integer> dice = Arrays.asList(1, 2);
        Color playerColor = Color.RED;

        // Expected Moves:
        // - Using die=1: Bear off from point 22
        // - Using die=2: Bear off from point 21
        List<Move> expectedMoves = Arrays.asList(
                new Move(22, -1, Color.RED),
                new Move(21, -1, Color.RED)
        );

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        assertEquals(1, possibleMoves.size(), "Expected one move sequence with multiple bear-offs.");
        assertEquals(expectedMoves, possibleMoves.get(0), "Move sequence does not match expected.");
    }*/

    /**
     * Test Scenario: Player attempts to bear off when only some checkers are in home board.
     */
    /*@Test
    void should_NotAllowPartialBearingOff_When_NotAllCheckersInHomeBoard() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 23, Color.RED); // Home board
        addChecker(board, 18, Color.RED); // Home board
        addChecker(board, 10, Color.RED); // Outside home board
        List<Integer> dice = Arrays.asList(5, 3);
        Color playerColor = Color.RED;

        // Expected Moves:
        // Must move the checker from point 10 first
        List<List<Move>> expected = Arrays.asList(
                Collections.singletonList(new Move(10, 15, Color.RED)),
                Collections.singletonList(new Move(10, 13, Color.RED))
        );

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        assertEquals(3, possibleMoves.size(), "Expected 3 move sequences moving checker outside home board.");
        //System.out.println(possibleMoves.size());
        //assertTrue(possibleMoves.containsAll(expected), "Move sequences do not match expected when not all checkers are in home board.");
        //System.out.println(possibleMoves);
    }*/

    /**
     * Test Scenario: Player has multiple checkers on the bar and can make multiple entries.
     */
    /*@Test
    void should_ReturnMultipleEntries_FromBar_When_PlayerHasMultipleCheckersOnBar() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        board.getBar().addChecker(new Checker(Color.BLUE));
        board.getBar().addChecker(new Checker(Color.BLUE)); // Player has 2 checkers on the bar
        List<Integer> dice = Arrays.asList(2, 4);
        Color playerColor = Color.BLUE;

        // Player BLUE enters from the bar to points 22 (die=1) and 20 (die=4)
        List<Move> sequence1 = Arrays.asList(
                new Move(-3, 22, Color.BLUE),
                new Move(-3, 20, Color.BLUE)
        );


        List<List<Move>> expected = Arrays.asList(sequence1);

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        assertEquals(1, possibleMoves.size(), "Expected two move sequences for multiple entries from bar.");
        assertTrue(possibleMoves.containsAll(expected), "Move sequences for multiple entries from bar do not match expected.");
    }*/

    /**
     * Test Scenario: Player attempts to bear off multiple checkers in one sequence.
     */
    /*@Test
    void should_BearOffMultipleCheckers_When_DiceAllow() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 0, Color.RED); // Home board points for RED: 18-23 (point 23 is index 22)
        addChecker(board, 1, Color.RED); // point 22 is index 21
        List<Integer> dice = Arrays.asList(1, 2);
        Color playerColor = Color.RED;

        // Expected Moves:
        // - Using die=1: Bear off from point 22
        // - Using die=2: Bear off from point 21
        List<Move> expectedMoves = Arrays.asList(
                new Move(22, -1, Color.RED),
                new Move(21, -1, Color.RED)
        );

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        assertEquals(1, possibleMoves.size(), "Expected one move sequence with multiple bear-offs.");
        assertEquals(expectedMoves, possibleMoves.get(0), "Move sequence does not match expected.");
    }*/

    /**
     * Test Scenario: Player has checkers on multiple points and must generate all valid sequences involving entering and bearing off.
     */
    @Test
    void should_HandleComplexMoveSequences_Correctly() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 15, Color.BLUE);
        addChecker(board, 5, Color.BLUE);
        addChecker(board, 10, Color.BLUE);
        List<Integer> dice = Arrays.asList(4, 3);
        Color playerColor = Color.BLUE;

        // Expected Move Sequences:
        // Sequence 1:
        // - Move from 11 to 16 (die=5)
        // - Move from 6 to 9 (die=3)
        // Sequence 2:
        // - Move from 6 to 9 (die=3)
        // - Move from 11 to 14 (die=5)
        // Sequence 3:
        // - Move from 1 to 6 (die=5)
        // - Move from 6 to 9 (die=3)
        List<Move> sequence1 = Arrays.asList(
                new Move(5, 1, Color.BLUE),
                new Move(10, 7, Color.BLUE)
        );
        List<Move> sequence2 = Arrays.asList(
                new Move(5, 1, Color.BLUE),
                new Move(15, 12, Color.BLUE)
        );
        List<Move> sequence3 = Arrays.asList(
                new Move(10, 6, Color.BLUE),
                new Move(5, 2, Color.BLUE)
        );
        List<Move> sequence4 = Arrays.asList(
                new Move(10, 6, Color.BLUE),
                new Move(6, 3, Color.BLUE)
        );
        List<Move> sequence5 = Arrays.asList(
                new Move(10, 6, Color.BLUE),
                new Move(15, 12, Color.BLUE)
        );
        List<Move> sequence6 = Arrays.asList(
                new Move(15, 11, Color.BLUE),
                new Move(5, 2, Color.BLUE)
        );
        List<Move> sequence7 = Arrays.asList(
                new Move(15, 11, Color.BLUE),
                new Move(10, 7, Color.BLUE)
        );
        List<Move> sequence8 = Arrays.asList(
                new Move(15, 11, Color.BLUE),
                new Move(11, 8, Color.BLUE)
        );



        List<List<Move>> expected = Arrays.asList(sequence1, sequence2, sequence3, sequence4, sequence5, sequence6, sequence7, sequence8);

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        for (List<Move> expectedSequence : expected) {
            assertTrue(possibleMoves.contains(expectedSequence),
                    "Expected move sequence not found: " + expectedSequence);
        }
    }

    /**
     * Test Scenario: Player attempts to make a move that lands on a point occupied by two opponent checkers (blocked).
     */
    @Test
    void should_NotAllowMove_When_LandingOnBlockedPoint() {
        // Arrange
        Board board = new Board();
        initializeEmptyBoard(board);
        addChecker(board, 0, Color.RED);
        addChecker(board, 2, Color.BLUE);
        addChecker(board, 2, Color.BLUE); // Point 2 is blocked by two BLUE checkers
        List<Integer> dice = Arrays.asList(2, 3);
        Color playerColor = Color.RED;

        // Expected Moves:
        // - Using die=2: Move from 0 to 2 is blocked
        // - Using die=3: Move from 0 to 3 is allowed
        List<Move> expectedMoves = Collections.singletonList(
                new Move(0, 3, Color.RED)
        );

        // Act
        List<List<Move>> possibleMoves = MoveGenerator.generateAllPossibleMoveSequences(board, dice, playerColor);

        // Assert
        assertEquals(1, possibleMoves.size(), "Expected one possible move sequence.");
        assertEquals(expectedMoves.get(0), possibleMoves.get(0).get(0), "Move does not match expected.");
    }
}
