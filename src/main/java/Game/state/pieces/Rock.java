package Game.state.pieces;

import Game.state.Board.Board;
import Game.state.Board.cell;
import Game.state.Move;

import java.util.ArrayList;

public class Rock extends Piece{
    public Rock(Color color) {
        super(color);
        this.setType(Type.ROOK);
    }

    @Override
    public void validateMove(Move move, Board board) {
        Board.lastMoveType = Move.moveType.REGULAR;
        board.setEnPassant(null);
        getPossibleMoves(move, board);

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

        int rankPinningDirection = 3;
        int filePinningDirection = 3;

        if(this.getPinningPiece() != null){
            if( getPinningPiece().getRank() - fromCellRank == 0) // checking if the pinning piece is on the same rank as the Rook
            {
                rankPinningDirection = 0;
                filePinningDirection = 2;
            } else if(getPinningPiece().getFile() - fromCellFile == 0){ // checking if the pinning piece is on the same File as the Rook
                filePinningDirection = 0;
                rankPinningDirection = 2;
            }
            else {  // pinning piece is not in not is the same Rank or File as the pinned piece ; this means no possible moves for the pinned piece
                rankPinningDirection = 2;
                filePinningDirection = 2;
            }
        }
        if(rankPinningDirection == 0 || rankPinningDirection == 3 ) {
            while (iterator + fromCellFile < Board.DIMENSION) {

                cell possibleCell = board.getCells()[fromCellRank][fromCellFile + iterator];
                if (possibleCell.getPiece() != null) {
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


            while (fromCellFile - iterator >= 0) {

                cell possibleCell = board.getCells()[fromCellRank][fromCellFile - iterator];
                if (possibleCell.getPiece() != null) {
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

        }

        if(filePinningDirection == 0 || rankPinningDirection == 3 ) {
            while (fromCellRank + iterator < Board.DIMENSION) {

                cell possibleCell = board.getCells()[fromCellRank + iterator][fromCellFile];
                if (possibleCell.getPiece() != null) {
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

            while (fromCellRank - iterator >= 0) {

                cell possibleCell = board.getCells()[fromCellRank - iterator][fromCellFile];
                if (possibleCell.getPiece() != null) {
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
        }

        return this.possibleMoves;
    }

    @Override
    public void checkPinnedPieces(cell position,Board board) {

        cell opponentKing = this.getColor() == Color.WHITE ? board.getbKingPosition() :board.getwKingPosition();
        int OpponentKingRank = opponentKing.getRank();
        int OpponentKingFile = opponentKing.getFile();
// check pinned pieces in straight directions (Row And Column)
        if (OpponentKingRank == position.getRank() || OpponentKingFile == position.getFile() ){
            System.out.println("ewed");
              this.setPinnedPiecesForRock(position,board);
        }
    }

    @Override
    public int getCode() {
        return this.getColor() == Color.WHITE ? 3 : 9;
    }
}
