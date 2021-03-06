package Game.state.pieces;

import Game.state.Board.Board;
import Game.state.Board.cell;
import Game.state.Move;
import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(Color color) {
        super(color);
        this.setType(Type.BISHOP);
    }

    @Override
    public void validateMove(Move move, Board board) {
        board.lastMoveType = Move.moveType.REGULAR;
        board.setEnPassant(null);
    }

    @Override
    public ArrayList<cell> getPossibleMoves(Move move, Board board ,boolean possible) {
        Color currentPlayerColor;
        int fromCellFile = move.getFromCell().getFile();
        int fromCellRank = move.getFromCell().getRank();
        int iteratorX = 1;
        int iteratorY = 1;

        currentPlayerColor = move.getFromCell().getPiece().getColor();
        cell OpponentKing = this.getColor() == Color.WHITE ? board.getbKingPosition() : board.getwKingPosition();

       if(possible) this.possibleMoves.clear();
        this.RegularMoves.clear();

        int rankPinningDirection = 0;
        int filePinningDirection = 0;

        if(this.getPinningPiece() != null){
            if( Math.abs(getPinningPiece().getRank() - fromCellRank) == Math.abs(getPinningPiece().getFile() - fromCellFile) ) // checking if the pinning piece is on the same diagonal as the Bishop
             {
                 rankPinningDirection = getPinningPiece().getRank() - fromCellRank > 0 ? 1 : -1;
                 filePinningDirection = getPinningPiece().getFile() - fromCellFile > 0 ? 1 : -1;
             }

            else {  // pinning piece is not in not is the same diagonal as the pinned piece ; thi means no possible moves for the pinned piece
                rankPinningDirection = 2;
                filePinningDirection = 2;
            }
        }
        if (rankPinningDirection * filePinningDirection == 1 || rankPinningDirection == 0)
        {
            while(iteratorX + fromCellRank < Board.DIMENSION && iteratorY + fromCellFile < Board.DIMENSION){
                if(this.addPiece(board, fromCellRank + iteratorX, fromCellFile + iteratorY, currentPlayerColor, OpponentKing, possible, move)) break;
                iteratorX++;
                iteratorY++;
            }
            iteratorX = 1;
            iteratorY = 1;
            while (fromCellRank - iteratorX >= 0 && fromCellFile - iteratorY >= 0) {
                if(this.addPiece(board, fromCellRank - iteratorX, fromCellFile - iteratorY, currentPlayerColor, OpponentKing, possible, move)) break;
                iteratorX++;
                iteratorY++;
            }
        }
        iteratorX = 1;
        iteratorY = 1;
        if (rankPinningDirection * filePinningDirection == -1 || rankPinningDirection == 0) {
            while (fromCellRank - iteratorX >= 0 && fromCellFile + iteratorY < Board.DIMENSION) {
                if(this.addPiece(board, fromCellRank - iteratorX, fromCellFile + iteratorY, currentPlayerColor, OpponentKing, possible, move)) break;
                iteratorX++;
                iteratorY++;
            }

            iteratorX = 1;
            iteratorY = 1;

            while (fromCellRank + iteratorX < Board.DIMENSION && fromCellFile - iteratorY >= 0) {
                if(this.addPiece(board, fromCellRank + iteratorX, fromCellFile - iteratorY, currentPlayerColor, OpponentKing, possible, move)) break;
                iteratorX++;
                iteratorY++;
            }
        }
        return this.possibleMoves;
    }

    @Override
    public Piece clone() {
        Bishop bishop = new Bishop(this.getColor());
        bishop.setFirstMove(this.isFirstMove());
        bishop.setPinningPiece(this.getPinningPiece().clonei());
        return bishop;
    }

    public void checkPinnedPieces(cell position,Board board) {

        cell opponentKing = this.getColor() == Color.WHITE ? board.getbKingPosition() :board.getwKingPosition();
        int OpponentKingRank = opponentKing.getRank();
        int OpponentKingFile = opponentKing.getFile();
// check pinned pieces in the diagonal direction
        if (Math.abs(OpponentKingRank - position.getRank()) == Math.abs(OpponentKingFile - position.getFile()) ) {
            this.setPinnedPiecesForBishop(position,board);
        }
    }

    @Override
    public int getCode() {
        return this.getColor() == Color.WHITE ? 2 : 8;
    }


    @Override
    public int getValue() {
        return 300;
    }

}
