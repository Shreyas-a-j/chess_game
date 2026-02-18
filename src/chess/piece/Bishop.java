package chess.piece;

import chess.board.Board;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;

import java.util.List;

public class Bishop extends Piece{
    public Bishop(PieceColor color) { super(color); }

    @Override
    public PieceType getType() {
        return PieceType.BISHOP;
    }

    @Override
    public char getSymbol() {
        return getColor() == PieceColor.WHITE ? 'B' : 'b';
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        return List.of();
    }

}
