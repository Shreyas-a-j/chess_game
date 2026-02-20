package chess.board; //This class belongs to chess.board package

//Import required classes from other packages
import chess.model.PieceColor;
import chess.model.PieceType;
import chess.model.Position;
import chess.piece.*;

public class Board {

    //2D array of Tile objects representing 8*8 chess board
    private final Tile[][] tiles;

    //Constructor - creates the board
    public Board() {
        tiles = new Tile[0][0];
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++) {
                tiles[row][col] = new Tile(row, col);//Create tile at(row, col)
            }
        }
    }

    //Method to initialize chess pieces on board
    public void initialize() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                tiles[row][col].setPiece(null);//Remove piece from tile
            }
        }

        //pawn
        for (int col = 0; col < 8; col++) {
            tiles[6][col].setPiece(new Pawn(PieceColor.WHITE));
            tiles[1][col].setPiece(new Pawn(PieceColor.BLACK));
        }

        //rook
        tiles[7][0].setPiece(new Rook(PieceColor.WHITE));//White left rook
        tiles[7][7].setPiece(new Rook(PieceColor.WHITE));//white right rook
        tiles[0][0].setPiece(new Rook(PieceColor.BLACK));//Black left rook
        tiles[0][7].setPiece(new Rook(PieceColor.BLACK));//Black right rook

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

    //Return tile at specific row and column
    public Tile getTile(int row, int col) { return tiles[row][col];}

    //Return tile using position object
    public Tile getTile(Position position) {return tiles[position.getRow()][position.getCol()];}

    //Get piece from given position
    public Piece getPiece(Position position) { return getTile(position).getPiece();}

    public Piece movePiece(Position from, Position to){
        Tile fromTile = getTile(from);//Source tile
        Tile toTile = getTile(to);//Destination tile


        Piece moving = fromTile.getPiece();//Piece that is moving
        Piece captured = toTile.getPiece();//Piece that may be captured

        //Move piece
        toTile.setPiece(moving);
        fromTile.setPiece(null);

        if(moving instanceof Pawn) {
            if((moving.getColor() == PieceColor.WHITE && to.getRow() == 0)
                    || (moving.getColor() == PieceColor.BLACK && to.getRow() == 7)) {
                toTile.setPiece(new Queen(moving.getColor()));//Replace pawn with queen
            }
        }
        return captured;//Return captured
    }

    public void print(){
        System.out.println();
        for(int row = 0; row < 8; row++){
            int displayRank = 8 - row;
            System.out.println(displayRank + "  ");
            for(int col = 0; col < 8; col++){
                Piece piece = tiles[row][col].getPiece();
                if(piece == null) {
                    System.out.println(". ");
                } else {
                    System.out.println(piece.getSymbol() + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("   a b c d e f g h");
        System.out.println();
    }
}