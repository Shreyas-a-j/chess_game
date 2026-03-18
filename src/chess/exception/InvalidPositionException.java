package chess.exception;

/**
 * Thrown when a requested board position is invalid or outside the board.
 */
public class InvalidPositionException extends GameException {

    public InvalidPositionException(String message) {
        super(message);
    }
}

