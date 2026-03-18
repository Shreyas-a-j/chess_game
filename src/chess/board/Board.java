package chess.board;

import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the chess board and provides operations on it.
 *
 * <p>The board owns 64 {@link Tile} instances and manages the placement of
 * {@link Piece} objects. It is also responsible for high-level game rules such
 * as detecting check, checkmate, and stalemate.</p>
 */
public class Board {

    private final Tile[][] tiles;

    /**
     * Creates a new, empty board.
     */
    public Board() {
        tiles = new Tile[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                tiles[row][col] = new Tile(row, col);
            }
        }
    }

    /**
    * Initializes the board with the standard chess starting position.
    */
    public void initialize() {
        // Clear board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                tiles[row][col].setPiece(null);
            }
        }

        // Pawns
        for (int col = 0; col < 8; col++) {
            tiles[6][col].setPiece(new Pawn(PieceColor.WHITE));
            tiles[1][col].setPiece(new Pawn(PieceColor.BLACK));
        }

        // Rooks
        tiles[7][0].setPiece(new Rook(PieceColor.WHITE));
        tiles[7][7].setPiece(new Rook(PieceColor.WHITE));
        tiles[0][0].setPiece(new Rook(PieceColor.BLACK));
        tiles[0][7].setPiece(new Rook(PieceColor.BLACK));

        // Knights
        tiles[7][1].setPiece(new Knight(PieceColor.WHITE));
        tiles[7][6].setPiece(new Knight(PieceColor.WHITE));
        tiles[0][1].setPiece(new Knight(PieceColor.BLACK));
        tiles[0][6].setPiece(new Knight(PieceColor.BLACK));

        // Bishops
        tiles[7][2].setPiece(new Bishop(PieceColor.WHITE));
        tiles[7][5].setPiece(new Bishop(PieceColor.WHITE));
        tiles[0][2].setPiece(new Bishop(PieceColor.BLACK));
        tiles[0][5].setPiece(new Bishop(PieceColor.BLACK));

        // Queens
        tiles[7][3].setPiece(new Queen(PieceColor.WHITE));
        tiles[0][3].setPiece(new Queen(PieceColor.BLACK));

        // Kings
        tiles[7][4].setPiece(new King(PieceColor.WHITE));
        tiles[0][4].setPiece(new King(PieceColor.BLACK));
    }

    /**
     * Returns the tile at the specified position.
     *
     * @param row zero-based row index
     * @param col zero-based column index
     * @return the tile
     */
    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Overloaded convenience method to get the tile at a logical position.
     *
     * @param position logical board position
     * @return the tile
     */
    public Tile getTile(Position position) {
        return tiles[position.getRow()][position.getCol()];
    }

    /**
     * Gets the piece at a given position.
     *
     * @param position position
     * @return piece or null
     */
    public Piece getPiece(Position position) {
        return getTile(position).getPiece();
    }

    /**
     * Moves a piece from one tile to another without performing validation.
     * This is used internally by higher-level game logic.
     *
     * @param from origin position
     * @param to   destination position
     * @return captured piece or null
     */
    public Piece movePiece(Position from, Position to) {
        Tile fromTile = getTile(from);
        Tile toTile = getTile(to);
        Piece moving = fromTile.getPiece();
        Piece captured = toTile.getPiece();
        toTile.setPiece(moving);
        fromTile.setPiece(null);

        // Handle simple promotion (always promote to queen)
        if (moving instanceof Pawn) {
            if ((moving.getColor() == PieceColor.WHITE && to.getRow() == 0)
                    || (moving.getColor() == PieceColor.BLACK && to.getRow() == 7)) {
                toTile.setPiece(new Queen(moving.getColor()));
            }
        }

        return captured;
    }

    /**
     * Prints the board to the console using ASCII representation.
     */
    public void print() {
        System.out.println();
        for (int row = 0; row < 8; row++) {
            int displayRank = 8 - row;
            System.out.print(displayRank + "  ");
            for (int col = 0; col < 8; col++) {
                Piece piece = tiles[row][col].getPiece();
                if (piece == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(piece.getSymbol() + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("   a b c d e f g h");
        System.out.println();
    }

    /**
     * Locates the king of the given color on the board.
     *
     * @param color color of the king
     * @return king position or null if not found
     */
    public Position findKing(PieceColor color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = tiles[row][col].getPiece();
                if (piece != null && piece.getColor() == color && piece.getType() == PieceType.KING) {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }

    /**
     * Determines whether the given color's king is currently in check.
     *
     * @param color color whose king to test
     * @return true if in check, false otherwise
     */
    public boolean isInCheck(PieceColor color) {
        Position kingPos = findKing(color);
        if (kingPos == null) {
            return false;
        }
        PieceColor opponent = color.opposite();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = tiles[row][col].getPiece();
                if (piece != null && piece.getColor() == opponent) {
                    Position from = new Position(row, col);
                    List<Position> moves = piece.getPossibleMoves(this, from);
                    for (Position target : moves) {
                        if (target.equals(kingPos)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines whether the given color has any legal moves at all.
     *
     * @param color color to test
     * @return true if there is at least one legal move; false otherwise
     */
    public boolean hasAnyLegalMove(PieceColor color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = tiles[row][col].getPiece();
                if (piece != null && piece.getColor() == color) {
                    Position from = new Position(row, col);
                    List<Position> moves = piece.getPossibleMoves(this, from);
                    for (Position to : moves) {
                        if (isLegalMove(color, from, to)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Verifies if moving a piece from one position to another is legal with respect
     * to check rules for the specified color.
     *
     * @param color active color
     * @param from  origin
     * @param to    destination
     * @return true if move does not leave the king in check
     */
    public boolean isLegalMove(PieceColor color, Position from, Position to) {
        Piece moving = getPiece(from);
        Piece captured = getPiece(to);

        // Make move on a copy of the board state by temporarily updating tiles.
        getTile(to).setPiece(moving);
        getTile(from).setPiece(null);

        boolean inCheckAfterMove = isInCheck(color);

        // Undo temporary move
        getTile(from).setPiece(moving);
        getTile(to).setPiece(captured);

        return !inCheckAfterMove;
    }

    /**
     * Returns a list of all pieces currently on the board for the specified color.
     *
     * @param color the color of the pieces to return
     * @return list of pieces
     */
    public List<Piece> getPieces(PieceColor color) {
        List<Piece> result = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = tiles[row][col].getPiece();
                if (piece != null && piece.getColor() == color) {
                    result.add(piece);
                }
            }
        }
        return result;
    }
}

