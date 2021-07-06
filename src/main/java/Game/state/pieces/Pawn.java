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
    private ArrayList<cell> twoCellsMoves = new ArrayList<>();


    @Override
    public void validateMove(Move move, Board board) {

         if(this.twoCellsMoves.contains(move.getToCell())){
             Board.lastMoveType = Move.moveType.REGULAR;
             board.setEnPassant(move.getToCell());
         } else {
             Board.lastMoveType = Move.moveType.REGULAR;
             board.setEnPassant(null);
         }
        getPossibleMoves(move, board);
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
        this.RegularMoves.clear();
        this.attackMoves.clear();
        this.twoCellsMoves.clear();
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
                    this.twoCellsMoves.add(possibleCell);
            }
        // capture Right move with respect to the capturing pawn
        if((isWithinTheRange(fromCellRank + direction,fromCellFile + 1)))
        {
            cell possibleCell = board.getCells()[fromCellRank + direction][fromCellFile + 1];
            this.RegularMoves.add(possibleCell);
            if(possibleCell.getPiece() != null )
            {
                if(possibleCell.getPiece().getColor() != currentPlayerColor)
                    this.possibleMoves.add(possibleCell);
                    this.attackMoves.add(possibleCell);
            } else{
                if(board.getEnPassant() != null){
                    System.out.println(board.getEnPassant());
                    if(board.getEnPassant().getFile() - fromCellFile == 1 && board.getEnPassant().getRank() == fromCellRank){
                        this.possibleMoves.add(possibleCell);
                    }
                }
            }
        }

        // capture Left move with respect to the capturing pawn
        if((isWithinTheRange(fromCellRank + direction,fromCellFile - 1)))

        {
            cell possibleCell = board.getCells()[fromCellRank + direction][fromCellFile - 1];
            this.RegularMoves.add(possibleCell);
            if(possibleCell.getPiece() != null )
            {
                if(possibleCell.getPiece().getColor() != currentPlayerColor)
                    this.possibleMoves.add(possibleCell);
                    this.attackMoves.add(possibleCell);
            } else {
                if(board.getEnPassant() != null){
                    if(fromCellFile - board.getEnPassant().getFile() == 1 && board.getEnPassant().getRank() == fromCellRank){
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
