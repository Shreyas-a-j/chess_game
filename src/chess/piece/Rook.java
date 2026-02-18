package chess.piece;

import chess.board.Board;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;

import java.util.List;

public class Rook extends Piece{
    public Rook(PieceColor color) { super(color); }

    @Override
    public PieceType getType() {
        return PieceType.ROOK;
    }

    @Override
    public char getSymbol() {
        return getColor() == PieceColor.WHITE ? 'R' : 'r';
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        return List.of();
    }

}
