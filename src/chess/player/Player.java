package chess.player;

import chess.model.PieceColor;
import chess.piece.Piece;

public class Player {
    private final String name;
    private final PieceColor color;

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
    public String toString() { return name + " (" + color + ")";}
}
