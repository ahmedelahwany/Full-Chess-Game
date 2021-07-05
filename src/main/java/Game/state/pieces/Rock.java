package Game.state.pieces;

import Game.state.Board.Board;
import Game.state.Board.cell;
import Game.state.Move;
import com.google.inject.internal.cglib.core.$MethodInfoTransformer;

import java.util.ArrayList;

public class Rock extends Piece{
    public Rock(Color color) {
        super(color);
        this.setType(Type.ROOK);
    }

    @Override
    public boolean validateMove(Move move, Board board) {
        Board.lastMoveType = Move.moveType.REGULAR;
        board.setEnPassant(null);
        return getPossibleMoves(move,board).contains(move.getToCell());

    }

    @Override
    public ArrayList<cell> getPossibleMoves(Move move, Board board) {
        Color currentPlayerColor;
        int fromCellFile = move.getFromCell().getFile();
        int fromCellRank = move.getFromCell().getRank();
        int iterator = 1;
        this.possibleMoves.clear();
        this.RegularMoves.clear();
        currentPlayerColor = move.getFromCell().getPiece().getColor();

       while(iterator + fromCellFile < Board.DIMENSION ){

           cell possibleCell = board.getCells()[fromCellRank][fromCellFile+iterator];
           if ( possibleCell.getPiece() != null) {
               this.RegularMoves.add(possibleCell);
               if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                   this.possibleMoves.add(possibleCell);
               }
               break;
           }
           this.RegularMoves.add(possibleCell);
           this.possibleMoves.add(possibleCell);
           iterator++;
       }
       iterator = 1;

        while(fromCellFile - iterator >= 0){

            cell possibleCell = board.getCells()[fromCellRank][fromCellFile-iterator];
            if ( possibleCell.getPiece() != null) {
                this.RegularMoves.add(possibleCell);
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.RegularMoves.add(possibleCell);
            this.possibleMoves.add(possibleCell);
            iterator++;
        }

        iterator = 1;

        while(fromCellRank + iterator < Board.DIMENSION){

            cell possibleCell = board.getCells()[fromCellRank + iterator][fromCellFile];
            if ( possibleCell.getPiece() != null) {
                this.RegularMoves.add(possibleCell);
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.RegularMoves.add(possibleCell);
            this.possibleMoves.add(possibleCell);
            iterator++;
        }
        iterator = 1;

        while(fromCellRank - iterator >= 0){

            cell possibleCell = board.getCells()[fromCellRank - iterator][fromCellFile];
            if ( possibleCell.getPiece() != null) {
                this.RegularMoves.add(possibleCell);
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.RegularMoves.add(possibleCell);
            this.possibleMoves.add(possibleCell);
            iterator++;
        }

        return this.possibleMoves;
    }

    @Override
    public ArrayList<cell> checkPinnedPieces(cell position,Board board) {

        cell opponentKing = this.getColor() == Color.WHITE ? board.getbKingPosition() :board.getwKingPosition();
        int OpponentKingRank = opponentKing.getRank();
        int OpponentKingFile = opponentKing.getFile();
// check pinned pieces in straight directions (Row And Column)
        if (OpponentKingRank == position.getRank() || OpponentKingFile == position.getFile() ){
             return getPinnedPieces(position,board);
        } else {
            return null;
        }
    }

    @Override
    public int getCode() {
        return this.getColor() == Color.WHITE ? 3 : 9;
    }
}
