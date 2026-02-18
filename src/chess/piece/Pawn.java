package chess.piece;

import chess.board.Board;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;

import java.util.List;

public class Pawn extends Piece {

    public Pawn(PieceColor color) { super(color); }

    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }

    @Override
    public char getSymbol() {
        return getColor() == PieceColor.WHITE ? 'P':'p';
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        return List.of();
    }

}
