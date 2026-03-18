package chess.model;

/**
 * Immutable value object representing a position on the chess board.
 *
 * <p>Positions are zero-based (row 0-7, column 0-7) internally but can be
 * converted to and from human-readable algebraic coordinates (e.g. "e2").</p>
 */
public final class Position {

    private final int row;
    private final int col;

    /**
     * Creates a new position.
     *
     * @param row zero-based row (0 at top, 7 at bottom)
     * @param col zero-based column (0 for 'a', 7 for 'h')
     * @throws IllegalArgumentException if the coordinates are outside the board
     */
    public Position(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            throw new IllegalArgumentException("Row and column must be between 0 and 7");
        }
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * Creates a {@link Position} from algebraic notation (e.g. "e2").
     *
     * @param notation two-character algebraic notation
     * @return a new {@link Position}
     * @throws IllegalArgumentException if the notation is invalid
     */
    public static Position fromAlgebraic(String notation) {
        if (notation == null || notation.length() != 2) {
            throw new IllegalArgumentException("Invalid position: " + notation);
        }
        char file = Character.toLowerCase(notation.charAt(0));
        char rank = notation.charAt(1);

        int col = file - 'a';
        int row = 8 - (rank - '0');

        return new Position(row, col);
    }

    /**
     * Converts this position to algebraic notation (e.g. "e2").
     *
     * @return the algebraic representation of the position
     */
    public String toAlgebraic() {
        char file = (char) ('a' + col);
        char rank = (char) ('0' + (8 - row));
        return "" + file + rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }

    @Override
    public String toString() {
        return toAlgebraic();
    }
}

