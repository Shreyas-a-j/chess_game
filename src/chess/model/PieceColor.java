package chess.model;

/**
 * Enumeration representing the color of a chess piece or player.
 */
public enum PieceColor {
    WHITE,
    BLACK;

    /**
     * Returns the opposite color.
     *
     * @return WHITE if current is BLACK, and BLACK if current is WHITE
     */
    public PieceColor opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
}

