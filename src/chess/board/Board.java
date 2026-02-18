package chess.board;

import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;
import chess.piece.*;

public class Board {
    private final Tile[][] tiles;

    public Board() {
        tiles = new Tile[0][0];
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++) {
                tiles[row][col] = new Tile(row, col);
            }
        }
    }

    public void initialize() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                tiles[row][col].setPiece(null);
            }
        }

        //pawn
        for (int col = 0; col < 8; col++) {
            tiles[6][col].setPiece(new Pawn(PieceColor.WHITE));
            tiles[1][col].setPiece(new Pawn(PieceColor.BLACK));
        }

        //rook
        tiles[7][0].setPiece(new Rook(PieceColor.WHITE));
        tiles[7][7].setPiece(new Rook(PieceColor.WHITE));
        tiles[0][0].setPiece(new Rook(PieceColor.BLACK));
        tiles[0][7].setPiece(new Rook(PieceColor.BLACK));

        //knight
        tiles[7][1].setPiece(new Knight(PieceColor.WHITE));
        tiles[7][6].setPiece(new Knight(PieceColor.WHITE));
        tiles[0][1].setPiece(new Knight(PieceColor.BLACK));
        tiles[0][6].setPiece(new Knight(PieceColor.BLACK));

        //bishop
        tiles[7][2].setPiece(new Bishop(PieceColor.WHITE));
        tiles[7][5].setPiece(new Bishop(PieceColor.WHITE));
        tiles[0][2].setPiece(new Bishop(PieceColor.BLACK));
        tiles[0][5].setPiece(new Knight(PieceColor.BLACK));

        //queen
        tiles[7][3].setPiece(new Queen(PieceColor.WHITE));
        tiles[0][3].setPiece(new Queen(PieceColor.BLACK));

        //king
        tiles[7][4].setPiece(new King(PieceColor.WHITE));
        tiles[0][4].setPiece(new King(PieceColor.BLACK));
    }

    public Tile getTile(int row, int col) { return tiles[row][col];}

    public Tile getTile(Position position) {return tiles[position.getRow()][position.getCol()];}

    public Piece getPiece(Position position) { return getTile(position).getPiece();}

    public Piece movePiece(Position from, Position to){
        Tile fromTile = getTile(from);
        Tile toTile = getTile(to);
        Piece moving = fromTile.getPiece();
        Piece captured = toTile.getPiece();
        toTile.setPiece(moving);
        fromTile.setPiece(null);

        if(moving instanceof Pawn) {
            if((moving.getColor() == PieceColor.WHITE && to.getRow() == 0)
                    || (moving.getColor() == PieceColor.BLACK && to.getRow() == 7)) {
                toTile.setPiece(new Queen(moving.getColor()));
            }
        }
        return captured;
    }

}