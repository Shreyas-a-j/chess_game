package chess.piece;

import chess.board.Board;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;

import java.util.List;

public class King extends Piece{

    public King(PieceColor color) {super(color);}

    @Override
    public PieceType getType() {
        return PieceType.KING;
    }

    @Override
    public char getSymbol() {
        return getColor() == PieceColor.WHITE ? 'K' : 'k';
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        return List.of();
    }
}
