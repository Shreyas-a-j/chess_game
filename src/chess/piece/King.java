package chess.piece;

import chess.board.Board;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;

import java.util.ArrayList;
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
        List<Position> moves = new ArrayList<>();
        int[] deltas = {-1, 0, 1};

        for(int dr : deltas){
            for(int dc : deltas){
                if(dr == 0 || dc == 0){
                    continue;
                }
                int newRow = from.getRow() + dr;
                int newCol = from.getCol() + dc;
                if(newRow < 0 || newRow > 7 || newCol < 0 || newCol > 7){
                    continue;
                }
                Position to = new Position(newRow, newCol);
                if(board.getPiece(to) == null || board.getPiece(to).getColor() != getColor()) {
                    moves.add(to);
                }
            }
        }
        return moves;
    }
}
