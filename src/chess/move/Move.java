package chess.move;

import chess.model.MoveStatus;
import chess.model.Position;
import chess.pieces.Piece;

/**
 * Value object representing a single move in the game.
 *
 * <p>A move is immutable once created and may contain references to the moved
 * piece, any captured piece, and its validation status.</p>
 */
public class Move {

    private final Position from;
    private final Position to;
    private final Piece movedPiece;
    private final Piece capturedPiece;
    private final MoveStatus status;

    /**
     * Creates a move with default {@link MoveStatus#VALID}.
     *
     * @param from        origin position
     * @param to          destination position
     * @param movedPiece  piece that was moved
     * @param capturedPiece captured piece, if any
     */
    public Move(Position from, Position to, Piece movedPiece, Piece capturedPiece) {
        this(from, to, movedPiece, capturedPiece, MoveStatus.VALID);
    }

    /**
     * Overloaded constructor for specifying a particular {@link MoveStatus}.
     *
     * @param from         origin
     * @param to           destination
     * @param movedPiece   piece that was moved
     * @param capturedPiece captured piece, if any
     * @param status       validation status
     */
    public Move(Position from, Position to, Piece movedPiece, Piece capturedPiece, MoveStatus status) {
        this.from = from;
        this.to = to;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.status = status;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public MoveStatus getStatus() {
        return status;
    }

    /**
     * Returns a simple string representation such as "e2-e4" or "e5xf6".
     *
     * @return algebraic-like notation for the move
     */
    public String getNotation() {
        String separator = capturedPiece == null ? "-" : "x";
        return from.toAlgebraic() + separator + to.toAlgebraic();
    }

    @Override
    public String toString() {
        return getNotation();
    }
}

