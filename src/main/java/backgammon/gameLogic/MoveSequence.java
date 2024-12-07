package backgammon.gameLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MoveSequence {
    private final List<Move> moves;

    public MoveSequence() {
        this.moves = new ArrayList<>();
    }

    public MoveSequence(List<Move> moves) {
        this.moves = new ArrayList<>(moves);
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public void removeLastMove() {
        if (!moves.isEmpty()) {
            moves.remove(moves.size() - 1);
        }
    }

    public List<Move> getMoves() {
        return new ArrayList<>(moves);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoveSequence that = (MoveSequence) o;

        return Objects.equals(moves, that.moves);
    }

    @Override
    public int hashCode() {
        return moves != null ? moves.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Move move : moves) {
            sb.append(move.toString()).append("\n");
        }
        return sb.toString();
    }
}
