package chess.piece;

import chess.board.Board;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;

import java.util.ArrayList;
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
        List<Position> moves = new ArrayList<>();

        int[][] directions = {
                {-1,-1}, // NW
                {-1, 1}, // NE
                {1, -1}, // SW
                {1 , 1}  // SE
        };

        for(int[] dir : directions) {
            int dr = dir[0];
            int dc = dir[1];
            int newRow = from.getRow() + dr;
            int newCol = from.getCol() + dc;

            while(newRow >= 0 && newRow <=7 && newCol >= 0 && newCol <= 7){
                Position to = new Position(newRow, newCol);
                if(board.getPiece(to) == null) {
                    moves.add(to);
                } else {
                    if(board.getPiece(to).getColor() != getColor()) {
                        moves.add(to);
                    }
                    break;
                }
                newRow += dr;
                newCol += dc;
            }
        }
        return moves;
    }
}
