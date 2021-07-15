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

    private ArrayList<cell> twoCellsMoves = new ArrayList<>();
    private cell enpassantCell;

    @Override
    public void validateMove(Move move, Board board) {

         if(this.twoCellsMoves.contains(move.getToCell())){
             board.lastMoveType = Move.moveType.REGULAR;
             board.setEnPassant(move.getToCell());
         } else if (move.getToCell().equals(enpassantCell)) {
             board.lastMoveType = Move.moveType.PAWN_EN_PASSANT;
         } else {
             board.lastMoveType = Move.moveType.REGULAR;
             board.setEnPassant(null);
         }
    }


    @Override
    public ArrayList<cell> getPossibleMoves(Move move, Board board , boolean possible) {
        Color currentPlayerColor;
        int fromCellFile = move.getFromCell().getFile();
        int fromCellRank = move.getFromCell().getRank();

        int direction = move.getFromCell().getPiece().getColor() == Color.WHITE ? -1 : 1;
        int defaultStartRank = move.getFromCell().getPiece().getColor() == Color.WHITE ? 6 : 1;
        currentPlayerColor = move.getFromCell().getPiece().getColor();
        if(possible)this.possibleMoves.clear();
        this.RegularMoves.clear();
        this.twoCellsMoves.clear();
        enpassantCell = null;
        // pinning logic setup
        boolean filePinned = false; // pawn can't move on the files because of pinning

        if(this.getPinningPiece() != null){
            if( getPinningPiece().getFile() - fromCellFile != 0) // checking if the pinning piece is on the same file as the Rook
            { // pinning piece is not in not is the same File as the pawn; this means no possible moves for the pawn
                filePinned = true ;
            }
        }

        // one cell forward move
        if(!filePinned) // if file pinned base of the previous logic , pawn can't move forward
       {
           if((isWithinTheRange(fromCellRank + direction,fromCellFile)))
       {
           cell possibleCell = board.getCells()[fromCellRank + direction][fromCellFile];
           if(possibleCell.getPiece() == null)
               this.AddPossibleMove(board,move,possibleCell,possible);
       }
        // Two-cell forward move
        if((isWithinTheRange(fromCellRank + 2 * direction,fromCellFile)))
            {
                cell possibleCell = board.getCells()[fromCellRank + 2 * direction][fromCellFile];
                cell oneCellForward = board.getCells()[fromCellRank + direction][fromCellFile]; // next cell in the forward direction has to be empty
                if(possibleCell.getPiece() == null && oneCellForward.getPiece() == null && fromCellRank == defaultStartRank)
                    this.AddPossibleMove(board,move,possibleCell,possible);
                    this.twoCellsMoves.add(possibleCell);
            }
       }
        // capture Right move with respect to the capturing pawn
        if(isWithinTheRange(fromCellRank + direction,fromCellFile + 1) && this.getPinningPiece()== null ||(filePinned )) // if a pawn is pinned to its king , it can't attack left or right
        {
            cell possibleCell = board.getCells()[fromCellRank + direction][fromCellFile + 1];
            this.RegularMoves.add(possibleCell);
            if(possibleCell.getPiece() != null )
            {
                if(( this.getPinningPiece() == null && possibleCell.getPiece().getColor() != currentPlayerColor) ||
                   (filePinned && possibleCell.getPiece().getType() == this.getPinningPiece().getPiece().getType()))// to count for the case when the pawn is pinned by Bishop or a queen on the diagonal direction(right)
                    this.AddPossibleMove(board,move,possibleCell,possible);
            } else{
                if(board.getEnPassant() != null && (this.getPinningPiece() == null || // to check if the enpassant move is in the same diagonal as the pinning piece . if so, the enpassant is valid move
                                                    Math.abs(possibleCell.getRank() - getPinningPiece().getRank()) == Math.abs(possibleCell.getFile() - getPinningPiece().getFile()))){
                    if(board.getEnPassant().getFile() - fromCellFile == 1 && board.getEnPassant().getRank() == fromCellRank){
                        enpassantCell = possibleCell;
                        this.AddPossibleMove(board,move,possibleCell,possible);
                    }
                }
            }
        }

        // capture Left move with respect to the capturing pawn
        if(isWithinTheRange(fromCellRank + direction,fromCellFile - 1)  && this.getPinningPiece() == null ||(filePinned))

        {
            cell possibleCell = board.getCells()[fromCellRank + direction][fromCellFile - 1];
            this.RegularMoves.add(possibleCell);
            if(possibleCell.getPiece() != null )
            {
                if(( this.getPinningPiece() == null && possibleCell.getPiece().getColor() != currentPlayerColor) ||
                   (filePinned && possibleCell.getPiece().getType()== this.getPinningPiece().getPiece().getType()))// to count for the case when the pawn is pinned by Bishop or a queen on the diagonal direction(left))
                    this.AddPossibleMove(board,move,possibleCell,possible);
            } else {
                if(board.getEnPassant() != null && (this.getPinningPiece() == null || // to check if the enpassant move is in the same diagonal as the pinning piece . if so, the enpassant is valid move
                                                    Math.abs(possibleCell.getRank() - getPinningPiece().getRank()) == Math.abs(possibleCell.getFile() - getPinningPiece().getFile()))){
                    if(fromCellFile - board.getEnPassant().getFile() == 1 && board.getEnPassant().getRank() == fromCellRank){
                        enpassantCell = possibleCell;
                        this.AddPossibleMove(board,move,possibleCell,possible);
                    }
                }
            }
        }


        return this.possibleMoves;

    }

    public ArrayList<cell> getTwoCellsMoves() {
        return twoCellsMoves;
    }

    public void setTwoCellsMoves(ArrayList<cell> twoCellsMoves) {
        this.twoCellsMoves = twoCellsMoves;
    }

    public cell getEnpassantCell() {
        return enpassantCell;
    }

    public void setEnpassantCell(cell enpassantCell) {
        this.enpassantCell = enpassantCell;
    }

    @Override
    public Piece clone() {
        Pawn Pawn = new Pawn(this.getColor());
        Pawn.setFirstMove(this.isFirstMove());
        Pawn.setPinningPiece(this.getPinningPiece().clonei());
        Pawn.setEnpassantCell(this.enpassantCell.clonei());
        return Pawn;
    }

    @Override
    public int getCode() {
        return this.getColor() == Color.WHITE ? 0 : 6;
    }

    @Override
    public int getValue() {
        return 100;
    }

}
