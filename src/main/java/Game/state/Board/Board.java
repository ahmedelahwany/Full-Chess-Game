package Game.state.Board;

import Game.state.Move;
import Game.state.pieces.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    public static final int DIMENSION = 8;

    private static cell wKingPosition;
    private static cell bKingPosition;

    public static Move.moveType lastMoveType;

    private cell enPassant;

    private ArrayList<cell> attackedCellsByOpponent = new ArrayList<>();



    private cell[][] cells = new cell[8][8];

    public ArrayList<cell> getAttackedCellsByOpponent(Piece.Color playerColor) {
        this.attackedCellsByOpponent.clear();
        for ( cell[] row : cells){
            for (cell cell :row){
                if (cell.getPiece() != null)
               {
                   if(cell.getPiece().getColor() == playerColor){
                       if(cell.getPiece().getType() == Piece.Type.PAWN){
                           cell.getPiece().getPossibleMoves(new Move(cell,new cell (0,0)),this);
                           Pawn pawn = (Pawn) cell.getPiece();
                           this.attackedCellsByOpponent.addAll(pawn.getAttackMoves());
                    }
                    else if (cell.getPiece().getType() == Piece.Type.KING) {
                           King king = (King) cell.getPiece();
                           this.attackedCellsByOpponent.addAll(king.getRegularMoves(new Move(cell,new cell (0,0)),this));
                    } else {
                           cell.getPiece().getPossibleMoves(new Move(cell,new cell (0,0)),this);
                           this.attackedCellsByOpponent.addAll(cell.getPiece().getRegularMoves());
                       }
                }
               }
            }
        }

        return attackedCellsByOpponent;
    }


    public Board(int cellsCode[][]) {

        build(cellsCode);
    }

    public void executeMove(Move move) {
        move.getFromCell().getPiece().validateMove(move,this);
        // if it's a regular move



        if (lastMoveType == Move.moveType.REGULAR)
        {
           updateKingPositions(move);
           changePiecePosition(move.getFromCell(),move.getToCell());
        }
         else if (lastMoveType == Move.moveType.CASTLE_QUEEN_SIDE){
            updateKingPositions(move);
            changePiecePosition(move.getFromCell(),move.getToCell());
            changePiecePosition(cells[move.getFromCell().getRank()][move.getFromCell().getFile()-4],cells[move.getFromCell().getRank()][move.getFromCell().getFile()-1]);
        }
        else if (lastMoveType == Move.moveType.CASTLE_KING_SIDE){
            updateKingPositions(move);
            changePiecePosition(move.getFromCell(),move.getToCell());
            changePiecePosition(cells[move.getFromCell().getRank()][move.getFromCell().getFile()+3],cells[move.getFromCell().getRank()][move.getFromCell().getFile()+1]);
        }

        // TODO
        // check mate and stale mate ,pawn promotion , check , enpassant cases


        checkPinnedPieces();
    }



    private void build(int[][] cellsCode){
        Piece.Color color;
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                if(cellsCode[i][j]<12){
                    if(cellsCode[i][j]<6){
                       color = Piece.Color.WHITE;
                    } else{
                        color= Piece.Color.BLACK;
                    }
                    cells[i][j] = new cell (i,j, getPieceBasedOnCode(cellsCode[i][j],color));
                    if (cells[i][j].getPiece().getType() == Piece.Type.KING) {
                        if (cells[i][j].getPiece().getColor() == Piece.Color.WHITE) {
                            setwKingPosition(cells[i][j]);
                        } else {
                            setbKingPosition(cells[i][j]);
                        }
                    }
                } else{
                    cells[i][j] = new cell (i,j);
                }
            }
        }
    }

    // method to check if there are any pinned pieces for a every player ; this method should be executed after every move
    private void checkPinnedPieces (){
        restPinnedPiece();
        for ( cell[] row : cells) {
            for (cell cell : row) {
                if (cell.getPiece() != null) {
                             cell.getPiece().checkPinnedPieces(cell,this); // checking if there is any pinning piece for every piece after every move over the board
                }
            }
        }
    }

    private void restPinnedPiece(){
        for ( cell[] row : cells) {
            for (cell cell : row) {
                if (cell.getPiece() != null) {
                    cell.getPiece().setPinningPiece(null); // resetting pinning piece property for all pieces
                }
            }
        }
    }

    // updating the king positions props in the board class
    private void updateKingPositions(Move move) {
        if (move.getFromCell().getPiece().getType() == Piece.Type.KING) {
            if (move.getFromCell().getPiece().getColor() == Piece.Color.WHITE) {
                setwKingPosition(move.getToCell());
            } else {
                setbKingPosition(move.getToCell());
            }
        }
    }

    private void changePiecePosition ( cell fromPos , cell toPos){
        cells[fromPos.getRank()][fromPos.getFile()] = new cell(fromPos.getRank(),fromPos.getFile());
        fromPos.getPiece().setFirstMove(false);
        cells[toPos.getRank()][toPos.getFile()].setPiece(fromPos.getPiece());
    }

    private Piece getPieceBasedOnCode(int i, Piece.Color color) {
        if(i == 0 || i == 6) return new Pawn(color);
        if(i == 3 || i == 9) return new Rock(color);
        if(i == 2 || i == 8) return new Bishop(color);
        if(i == 4 || i == 10) return new Queen(color);
        if(i == 1 || i == 7) return new Knight(color);
        return new King(color);
    }


    public cell getwKingPosition() {
        return wKingPosition;
    }


    public cell getbKingPosition() {
        return bKingPosition;
    }


    public static void setwKingPosition(cell wKingPosition) {
        Board.wKingPosition = wKingPosition;
    }

    public static void setbKingPosition(cell bKingPosition) {
        Board.bKingPosition = bKingPosition;
    }


    public cell[][] getCells() {
        return cells;
    }


    public cell getEnPassant() {
        return enPassant;
    }

    public void setEnPassant(cell enPassant) {
        this.enPassant = enPassant;
    }
    @Override
    public String toString() {
            StringBuilder sb = new StringBuilder();
            for (cell[] row : this.getCells()) {
                for (cell cell : row) {
                    sb.append(cell).append(' ');
                }
                sb.append('\n');
            }
            return sb.toString();
    }





}
