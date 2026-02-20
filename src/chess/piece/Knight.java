package chess.piece;

import chess.board.Board;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece{
    public Knight(PieceColor color){ super(color);}

    @Override
    public PieceType getType() {
        return PieceType.KNIGHT;
    }

    @Override
    public char getSymbol() {
        return getColor() == PieceColor.WHITE ? 'K' : 'k';
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        List<Position> moves = new ArrayList<>();

        int[][] jumps = {
                {-2,-1},{-2,1},{2,-1},{2,1},{-1,-2},{-1,2},{1,-2},{1,2}
        };

        for(int[] j : jumps) {
            int newRow = from.getRow() + j[0];
            int newCol = from.getCol() + j[1];
            if(newRow < 0 || newRow > 7 || newCol < 0 || newCol > 7) {
                continue;
            }
            Position to = new Position(newRow, newCol);
            if(board.getPiece(to) == null || board.getPiece(to).getColor() != getColor()){
                moves.add(to);
            }
        }
        return moves;
    }
}
