package Game.state.pieces;

import Game.state.Board.Board;
import Game.state.Board.cell;
import Game.state.Move;

import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(Color color) {
        super(color);
        this.setType(Type.QUEEN);
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
        int iteratorX = 1;
        int iteratorY = 1;
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
            else if(Math.abs(getPinningPiece().getRank() - fromCellRank) == Math.abs(getPinningPiece().getFile() - fromCellFile)){
                rankPinningDirection = getPinningPiece().getRank() - fromCellRank > 0 ? 1 : -1;
                filePinningDirection = getPinningPiece().getFile() - fromCellFile > 0 ? 1 : -1;
            }
            else {  // pinning piece is not in not is the same Rank or File as the pinned piece ; this means no possible moves for the pinned piece
                rankPinningDirection = 2;
                filePinningDirection = 2;
            }
        }



        // checking all 8 directions for possible moves , considering the fact that the piece may be pinned
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

        if (rankPinningDirection * filePinningDirection == 1 || rankPinningDirection == 3)
        {
            while(iteratorX + fromCellRank < Board.DIMENSION && iteratorY + fromCellFile < Board.DIMENSION){

                cell possibleCell = board.getCells()[fromCellRank + iteratorX][fromCellFile + iteratorY];
                if ( possibleCell.getPiece() != null) {
                    this.RegularMoves.add(possibleCell);
                    if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                        this.possibleMoves.add(possibleCell);
                    }
                    break;
                }
                this.RegularMoves.add(possibleCell);
                this.possibleMoves.add(possibleCell);
                iteratorX++;
                iteratorY++;
            }
            iteratorX = 1;
            iteratorY = 1;
            while (fromCellRank - iteratorX >= 0 && fromCellFile - iteratorY >= 0) {

                cell possibleCell = board.getCells()[fromCellRank - iteratorX][fromCellFile - iteratorY];
                this.RegularMoves.add(possibleCell);
                if (possibleCell.getPiece() != null) {
                    if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                        this.possibleMoves.add(possibleCell);
                    }
                    break;
                }
                this.RegularMoves.add(possibleCell);
                this.possibleMoves.add(possibleCell);
                iteratorX++;
                iteratorY++;
            }
        }
        iteratorX = 1;
        iteratorY = 1;


        if (rankPinningDirection * filePinningDirection == -1 || rankPinningDirection == 3) {
            while (fromCellRank - iteratorX >= 0 && fromCellFile + iteratorY < Board.DIMENSION) {

                cell possibleCell = board.getCells()[fromCellRank - iteratorX][fromCellFile + iteratorY];
                this.RegularMoves.add(possibleCell);
                if (possibleCell.getPiece() != null) {
                    if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                        this.possibleMoves.add(possibleCell);
                    }
                    break;
                }
                this.RegularMoves.add(possibleCell);
                this.possibleMoves.add(possibleCell);
                iteratorX++;
                iteratorY++;
            }

            iteratorX = 1;
            iteratorY = 1;

            while (fromCellRank + iteratorX < Board.DIMENSION && fromCellFile - iteratorY >= 0) {

                cell possibleCell = board.getCells()[fromCellRank + iteratorX][fromCellFile - iteratorY];
                if (possibleCell.getPiece() != null) {
                    this.RegularMoves.add(possibleCell);
                    if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                        this.possibleMoves.add(possibleCell);
                    }
                    break;
                }
                this.RegularMoves.add(possibleCell);
                this.possibleMoves.add(possibleCell);
                iteratorX++;
                iteratorY++;
            }
        }
        return this.possibleMoves;
    }

    @Override
    public void checkPinnedPieces(cell position, Board board) {
        cell opponentKing = this.getColor() == Color.WHITE ? board.getbKingPosition() :board.getwKingPosition();
        int OpponentKingRank = opponentKing.getRank();
        int OpponentKingFile = opponentKing.getFile();

        if (Math.abs(OpponentKingRank - position.getRank()) == Math.abs(OpponentKingFile - position.getFile())){
            setPinnedPiecesForBishop(position,board);
        }
        else if (OpponentKingRank == position.getRank() || OpponentKingFile == position.getFile()){
             setPinnedPiecesForRock(position,board);
        }
    }

    @Override
    public int getCode() {
        return this.getColor() == Color.WHITE ? 4 : 10;
    }
}
