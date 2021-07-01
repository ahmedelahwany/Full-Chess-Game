package Game.state.Board;

import Game.state.pieces.Piece;

import java.util.Objects;


/**
 * Class representing the cells in the board
 */
public class cell {

    /**
     *  fields of the cell class.
     *  rank field represents the the row of the cell in the board
     *  file field represents the the column of the cell in the board
     */


     private int file;
     private int rank;
     public Piece piece;

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public cell(int rank, int file, Piece piece) {
        this.file = file;
        this.rank = rank;
        this.piece = piece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof cell)) return false;
        cell cell = (cell) o;
        return getFile() == cell.getFile() &&
               getRank() == cell.getRank();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFile(), getRank());
    }

    public cell(int rank, int file) {
        this.file = file;
        this.rank = rank;
        this.piece = null;
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    public String toString() {


        String pieceName = "";
        if(piece != null)
       { switch (piece.getColor()) {
            case WHITE:
                pieceName += "white_";
                break;
            case BLACK:
                pieceName += "black_";
                break;
        }
        switch (piece.getType()) {
            case KING:
                pieceName += "king";
                break;
            case ROOK:
                pieceName += "rook";
                break;
            case BISHOP:
                pieceName += "bishop";
                break;
            case QUEEN:
                pieceName += "queen";
                break;
            case KNIGHT:
                pieceName += "knight";
                break;
            case PAWN:
                pieceName += "pawn";
                break;

        }
       } else {
             pieceName = "Empty";
        }

        return pieceName + getRank() + getFile();

    }

}
