package chess.pieces;

import chess.board.Board;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;

import java.util.List;

/**
 * Abstract base class for all chess pieces.
 *
 * <p>Concrete subclasses implement their own movement rules via the
 * {@link #getPossibleMoves(Board, Position)} method. This demonstrates
 * polymorphism and abstraction.</p>
 */
public abstract class Piece {

    private final PieceColor color;

    /**
     * Creates a new piece with the given color.
     *
     * @param color piece color
     */
    protected Piece(PieceColor color) {
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }

    /**
     * Returns the type of this piece.
     *
     * @return piece type
     */
    public abstract PieceType getType();

    /**
     * Returns a single-character symbol used for ASCII board display.
     *
     * @return symbol character
     */
    public abstract char getSymbol();

    /**
     * Calculates all pseudo-legal moves for this piece from the specified
     * position on the given board.
     *
     * @param board current game board
     * @param from  starting position
     * @return list of target positions
     */
    public abstract List<Position> getPossibleMoves(Board board, Position from);
}

