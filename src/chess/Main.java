package chess;

import chess.game.Game;

/**
 * Entry point for the terminal-based two-player chess game.
 *
 * <p>This class is intentionally very small. It delegates all application
 * startup logic to the {@link Game} class in order to keep responsibilities
 * well separated.</p>
 */
public class Main {

    /**
     * Starts the chess game.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}

