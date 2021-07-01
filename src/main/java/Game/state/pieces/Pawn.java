package Game.state.pieces;

import Game.state.Board.Board;
import Game.state.Board.cell;
import Game.state.Move;

import java.util.ArrayList;

public class Pawn extends Piece {

    public Pawn(Color color) {
        super(color);
        this.setType(Type.PAWN);
    }

    private ArrayList<cell> attackMoves = new ArrayList<>();

    @Override
    public boolean validateMove(Move move, Board board) {
        return false;
    }

    public ArrayList<cell> getAttackMoves() {
        return attackMoves;
    }

    @Override
    public ArrayList<cell> getPossibleMoves(Move move, Board board) {
        Color currentPlayerColor;
        int fromCellFile = move.getFromCell().getFile();
        int fromCellRank = move.getFromCell().getRank();

        int direction = move.getFromCell().getPiece().getColor() == Color.WHITE ? -1 : 1;
        int defaultStartRank = move.getFromCell().getPiece().getColor() == Color.WHITE ? 6 : 1;
        currentPlayerColor = move.getFromCell().getPiece().getColor();
        this.possibleMoves.clear();

        // one cell forward move
        if((isWithinTheRange(fromCellRank + direction,fromCellFile)))
       {
           cell possibleCell = board.getCells()[fromCellRank + direction][fromCellFile];
           if(possibleCell.getPiece() == null)
            this.possibleMoves.add(possibleCell);
       }
        // Two-cell forward move
        if((isWithinTheRange(fromCellRank + 2 * direction,fromCellFile)))
            {
                cell possibleCell = board.getCells()[fromCellRank + 2 * direction][fromCellFile];
                if(possibleCell.getPiece() == null && fromCellRank == defaultStartRank)
                    this.possibleMoves.add(possibleCell);
            }
        // capture Left move with respect to the capturing pawn
        if((isWithinTheRange(fromCellRank + direction,fromCellFile - direction)))
        {
            cell possibleCell = board.getCells()[fromCellRank + direction][fromCellFile - direction];
            if(possibleCell.getPiece() != null )
            {
                if(possibleCell.getPiece().getColor() != currentPlayerColor)
                    this.possibleMoves.add(possibleCell);
                    this.attackMoves.add(possibleCell);
            } else{
                if(board.getEnPassant() != null){
                    if(fromCellFile - board.getEnPassant().getFile() == direction ){
                        this.possibleMoves.add(possibleCell);
                    }
                }
            }
        }

        // capture Right move with respect to the capturing pawn
        if((isWithinTheRange(fromCellRank + direction,fromCellFile + direction)))

        {
            cell possibleCell = board.getCells()[fromCellRank + direction][fromCellFile + direction];
            if(possibleCell.getPiece() != null )
            {
                if(possibleCell.getPiece().getColor() != currentPlayerColor)
                    this.possibleMoves.add(possibleCell);
                    this.attackMoves.add(possibleCell);
            } else {
                if(board.getEnPassant() != null){
                    if(fromCellFile - board.getEnPassant().getFile() == direction ){
                        this.possibleMoves.add(possibleCell);
                    }
                }
            }
        }


        return this.possibleMoves;

    }

    @Override
    public int getCode() {
        return this.getColor() == Color.WHITE ? 0 : 6;
    }

}
