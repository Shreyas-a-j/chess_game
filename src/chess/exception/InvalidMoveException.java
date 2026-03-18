package chess.exception;

/**
 * Thrown when a requested move cannot be executed.
 */
public class InvalidMoveException extends GameException {

    public InvalidMoveException(String message) {
        super(message);
    }
}

