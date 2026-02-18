package chess.piece;

import chess.board.Board;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;

import java.util.List;

public class Queen extends Piece{

    public Queen(PieceColor color) {super(color);}

    @Override
    public PieceType getType() {
        return PieceType.QUEEN;
    }

    @Override
    public char getSymbol() {
        return getColor() == PieceColor.WHITE ? 'Q' : 'q';
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        return List.of();
    }
}
