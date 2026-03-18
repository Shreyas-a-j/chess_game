package chess.player;

import chess.model.PieceColor;

/**
 * Represents a human player in the terminal chess game.
 *
 * <p>A player is primarily characterized by their name and color.</p>
 */
public class Player {

    private final String name;
    private final PieceColor color;

    /**
     * Creates a new player with the given name and color.
     *
     * @param name  display name
     * @param color piece color controlled by this player
     */
    public Player(String name, PieceColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public PieceColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return name + " (" + color + ")";
    }
}

