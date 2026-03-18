package chess.board;

import chess.pieces.Piece;

/**
 * Represents a single square (tile) on the chess board.
 *
 * <p>A tile knows its coordinates and the piece currently occupying it, if any.
 * The board composes 64 tiles to represent the entire game state.</p>
 */
public class Tile {

    private final int row;
    private final int col;
    private Piece piece;

    /**
     * Creates an empty tile at the given coordinates.
     *
     * @param row zero-based row index (0-7)
     * @param col zero-based column index (0-7)
     */
    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Indicates whether this tile currently holds a piece.
     *
     * @return true if occupied, false otherwise
     */
    public boolean isOccupied() {
        return piece != null;
    }
}

