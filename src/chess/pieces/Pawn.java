package chess.pieces;

import chess.board.Board;
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete piece representing a pawn.
 *
 * <p>This implementation supports single and double forward moves from the
 * starting rank, and diagonal captures. En passant is omitted for clarity.</p>
 */
public class Pawn extends Piece {

    public Pawn(PieceColor color) {
        super(color);
    }

    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }

    @Override
    public char getSymbol() {
        return getColor() == PieceColor.WHITE ? 'P' : 'p';
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        List<Position> moves = new ArrayList<>();
        int direction = (getColor() == PieceColor.WHITE) ? -1 : 1;

        int row = from.getRow();
        int col = from.getCol();

        // Forward one
        int oneRow = row + direction;
        if (oneRow >= 0 && oneRow <= 7) {
            Position oneForward = new Position(oneRow, col);
            if (board.getPiece(oneForward) == null) {
                moves.add(oneForward);

                // Forward two from starting rank
                int startingRow = (getColor() == PieceColor.WHITE) ? 6 : 1;
                int twoRow = row + 2 * direction;
                if (row == startingRow && twoRow >= 0 && twoRow <= 7) {
                    Position twoForward = new Position(twoRow, col);
                    if (board.getPiece(twoForward) == null) {
                        moves.add(twoForward);
                    }
                }
            }
        }

        // Diagonal captures
        int[] dc = {-1, 1};
        for (int deltaC : dc) {
            int newRow = row + direction;
            int newCol = col + deltaC;
            if (newRow < 0 || newRow > 7 || newCol < 0 || newCol > 7) {
                continue;
            }
            Position diag = new Position(newRow, newCol);
            if (board.getPiece(diag) != null && board.getPiece(diag).getColor() != getColor()) {
                moves.add(diag);
            }
        }

        return moves;
    }
}

